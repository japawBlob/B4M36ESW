package cz.cvut.fel.esw.shortestpath.search;

import cz.cvut.fel.esw.shortestpath.graph.Edge;
import cz.cvut.fel.esw.shortestpath.graph.Graph;
import cz.cvut.fel.esw.shortestpath.graph.Node;
import cz.cvut.fel.esw.shortestpath.search.pareto.*;
import cz.cvut.fel.esw.shortestpath.util.BinaryHeap;
import cz.cvut.fel.esw.shortestpath.util.Heap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of multi-objective variant of the well-known A* algorithm. New instance must be created for each
 * search.
 */
public class MultiObjectiveAStar {

    private static final Logger logger = LogManager.getLogger(MultiObjectiveAStar.class);

    /**
     * Lexicographical ordering of search states for the heap. First time including heuristic estimate, then distance
     * including heuristic estimate.
     */
    public static final Comparator<SearchState> HEAP_COMPARATOR = SearchState::lexicographicalOrder;

    /**
     * Dominance relation for opened states. It compares both objectives.
     */
    public static final ParetoComparator<SearchState> OPENED_PARETO_COMPARATOR = SearchState::dominatedByTimeAndDistance;

    /**
     * Dominance relation for closed pareto bag. Because we use lexicographical ordering for the heap, this dominance
     * relation can be simplified only to one of the objectives.
     */
    public static final ParetoComparator<SearchState> CLOSED_PARETO_COMPARATOR = SearchState::dominatedByDistance;


    /**
     * Dominance relation for solution pareto bag. Because we use lexicographical ordering for the heap, this dominance
     * relation can be simplified only to one of the objectives. Because it is used to check only with the already found
     * solutions and because the used heuristics are consistent we can use the heuristic for the check.
     */
    public static final ParetoComparator<SearchState> SOLUTION_PARETO_COMPARATOR = SearchState::dominatedByDistanceEstimate;

    private final Heap<SearchState> heap;

    private final ParetoBag<SearchState> opened;
    private final ParetoBag<SearchState> closed;
    private final ParetoBag<SearchState> solutions;

    private final Heuristic timeHeuristic;
    private final Heuristic distanceHeuristic;

    private final Graph<Node, Edge> graph;

    private final int start;
    private final int goal;

    MultiObjectiveAStar(Heap<SearchState> heap, ParetoBag<SearchState> opened, ParetoBag<SearchState> closed, ParetoBag<SearchState> solutions, Heuristic timeHeuristic, Heuristic distanceHeuristic, Graph<Node, Edge> graph, int start, int goal) {
        this.heap = heap;
        this.opened = opened;
        this.closed = closed;
        this.solutions = solutions;
        this.timeHeuristic = timeHeuristic;
        this.distanceHeuristic = distanceHeuristic;
        this.graph = graph;
        this.start = start;
        this.goal = goal;
    }


    public List<SearchState> calculate() {
        return calculate(Integer.MAX_VALUE);
    }

    public List<SearchState> calculate(int numberOfResults) {
        encounter(SearchState.of(start, 0, 0, null));

        int iterationCounter = 0;
        List<SearchState> results = new ArrayList<>();
        while (!heap.isEmpty()) {
            iterationCounter++;
            Heap.Entry<SearchState> minEntry = heap.extractMinimum();
            SearchState minState = minEntry.getValue();

// TODO Problematicky logger
            //logger.trace("Extracted state in " + iterationCounter + ". iteration: " + minState);

            opened.remove(minEntry);

            //checks if the extracted state is not already dominated by some of the found solutions
            if (solutions.dominates(minState)) continue;


            if (isGoal(minState)) {
                results.add(minState);
                solutions.addWithoutChecks(minEntry);
                //logger.trace(minState);
                if (results.size() >= numberOfResults) return results;
                continue;
            }
            closed.addWithoutChecks(minEntry);

            expand(minState);
        }

        logger.debug("Solutions found after " + iterationCounter + " iterations");
        return results;
    }

    private void expand(SearchState state) {
        for (Edge edge : graph.getOutEdges(state.getNodeId())) {
            //immediate return
            if (state.getPrevious() != null && state.getPrevious().getNodeId() == edge.getToId()) continue;

            SearchState successor = move(state, edge);
            encounter(successor);
        }
    }

    private SearchState move(SearchState state, Edge edge) {
        int newNodeId = edge.getToId();
        int newTime = state.getTime() + edge.getTime();
        int newDistance = state.getDistance() + edge.getDistance();

        int timeEstimate = timeHeuristic.compute(newNodeId);
        int distanceEstimate = distanceHeuristic.compute(newNodeId);


        return SearchState.of(newNodeId, newTime, newDistance, timeEstimate, distanceEstimate, state);
    }

    private boolean isGoal(SearchState state) {
        return state.getNodeId() == goal;
    }

    private void encounter(SearchState state) {
        if (solutions.dominates(state)) return;
        if (closed.dominates(state)) return;

        opened.addToBagAndHeapIfNotDominated(state, heap);
    }

    public static MultiObjectiveAStar createDefault(Graph<Node, Edge> graph, int start, int goal) {
        Heuristic timeHeuristic = createTimeHeuristic(graph, goal);
        Heuristic distanceHeuristic = createDistanceHeuristic(graph, goal);

        ParetoBag<SearchState> opened = ParetoBagMap.create(graph.numberOfNodes(), BasicParetoBag::new, OPENED_PARETO_COMPARATOR);

        //because we use for dominance checks only single objective (explanation why we can do it is in the documentation of used pareto comparators)
        //we know that in this "pareto-bags" will always be only one element
        ParetoBag<SearchState> closed = ParetoBagMap.create(graph.numberOfNodes(), SingletonParetoBag::new, CLOSED_PARETO_COMPARATOR);
        ParetoBag<SearchState> solutions = new SingletonParetoBag<>(SOLUTION_PARETO_COMPARATOR);

        return new MultiObjectiveAStar(new BinaryHeap<>(HEAP_COMPARATOR), opened, closed, solutions, timeHeuristic, distanceHeuristic, graph, start, goal);
    }
/// TODO dvakrat se tu vola dijkstra, jednou s time, jednou s distance, neslo by to najednou?
    private static Heuristic createTimeHeuristic(Graph<Node, Edge> graph, int goal) {
        int[] heuristic = Dijkstra.createBackward(goal, graph, Edge::getTime).calculate();
        return new PrecalculatedHeuristic(heuristic);
    }

    private static Heuristic createDistanceHeuristic(Graph<Node, Edge> graph, int goal) {
        int[] heuristic = Dijkstra.createBackward(goal, graph, Edge::getDistance).calculate();
        return new PrecalculatedHeuristic(heuristic);
    }

}
