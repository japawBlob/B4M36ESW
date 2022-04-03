package cz.cvut.fel.esw.shortestpath.search;

import cz.cvut.fel.esw.shortestpath.graph.Edge;
import cz.cvut.fel.esw.shortestpath.graph.Graph;
import cz.cvut.fel.esw.shortestpath.graph.Node;
import cz.cvut.fel.esw.shortestpath.util.BinaryHeap;
import cz.cvut.fel.esw.shortestpath.util.Heap;

import java.util.*;
import java.util.function.ToIntFunction;

/**
 * Implementation of Dijkstra's algorithm. New instance must be created for each search.
 *
 * @author Marek Cuchý (CVUT)
 */
public class Dijkstra {

    private final Graph<Node, Edge> graph;
    private final ToIntFunction<Edge> objectiveFunction;
    private final Direction direction;
    private final int origin;

    private final Heap<SingleObjState> heap;
    private final Map<Integer, Heap.Entry<SingleObjState>> seen;
    private final Set<Integer> closed;


    Dijkstra(int origin, Graph<Node, Edge> graph, ToIntFunction<Edge> objectiveFunction, Direction direction) {
        this.graph = graph;
        this.objectiveFunction = objectiveFunction;
        this.direction = direction;
        this.origin = origin;
        this.heap = new BinaryHeap<>(Comparator.<SingleObjState>naturalOrder());
        this.seen = new HashMap<>();
        this.closed = new HashSet<>();
    }

    /**
     * Calculate objective values required to get from the origin to all nodes.
     *
     * @return
     */
    public int[] calculate() {
        int[] result = new int[graph.numberOfNodes()];
        heap.insert(SingleObjState.of(origin, 0));
        while (!heap.isEmpty()) {
            Heap.Entry<SingleObjState> entry = heap.extractMinimum();
            SingleObjState value = entry.getValue();
            int nodeId = value.getNodeId();
            int objective = value.getObjective();
            close(nodeId);
            expand(nodeId, objective);
            result[nodeId] = objective;
        }
        return result;
    }

    private void expand(int currentNodeId, int currentObjective) {
        for (Edge edge : getExpandEdges(currentNodeId)) {
            int successorNodeId = successorNodeId(edge);
            if (isClosed(successorNodeId)) continue;
            int successorObjective = currentObjective + objectiveFunction.applyAsInt(edge);

            SingleObjState successor = SingleObjState.of(successorNodeId, successorObjective);
            if (isInHeap(successorNodeId)) {
                encounterAgain(successor);
            } else {
                encounter(successor);
            }
        }
    }

    private int successorNodeId(Edge edge) {
        return direction.successorNodeId(edge);
    }

    private List<Edge> getExpandEdges(int nodeId) {
        return direction.getExpandEdges(graph, nodeId);
    }

    private void encounter(SingleObjState state) {
        Heap.Entry<SingleObjState> successorEntry = heap.insert(state);
        seen.put(state.getNodeId(), successorEntry);
    }

    private void encounterAgain(SingleObjState successor) {
        Heap.Entry<SingleObjState> seenEntry = seen.get(successor.getNodeId());
        if (successor.getObjective() < seenEntry.getValue().getObjective()) {
            Heap.Entry<SingleObjState> newEntry = heap.decrease(seenEntry, successor);
            if (newEntry != seenEntry) {
                seen.put(successor.getNodeId(), newEntry);
            }

        }
    }

    private boolean isInHeap(int successorId) {
        return seen.containsKey(successorId);
    }

    private void close(int nodeId) {
        closed.add(nodeId);
    }

    private boolean isClosed(int nodeId) {
        return closed.contains(nodeId);
    }

    public static Dijkstra createForward(int origin, Graph<Node, Edge> graph, ToIntFunction<Edge> objectiveFunction) {
        return create(origin, graph, objectiveFunction, Direction.FORWARD);

    }

    public static Dijkstra createBackward(int origin, Graph<Node, Edge> graph, ToIntFunction<Edge> objectiveFunction) {
        return create(origin, graph, objectiveFunction, Direction.BACKWARD);
    }

    public static Dijkstra create(int origin, Graph<Node, Edge> graph, ToIntFunction<Edge> objectiveFunction, Direction direction) {
        return new Dijkstra(origin, graph, objectiveFunction, direction);
    }

    /**
     * Represents one state during during the single objective search.
     */
    private static class SingleObjState implements BinaryHeap.IndexedEntry<SingleObjState>, Comparable<SingleObjState> {

        private final int nodeId;

        private final int objective;

        private int heapIndex;

        private SingleObjState(int nodeId, int objective) {
            this.nodeId = nodeId;
            this.objective = objective;
        }


        public int getNodeId() {
            return nodeId;
        }

        public int getObjective() {
            return objective;
        }


        @Override
        public Integer getIndex() {
            return heapIndex;
        }

        @Override
        public void setIndex(Integer index) {
            this.heapIndex = index;
        }

        @Override
        public SingleObjState getValue() {
            return this;
        }

        @Override
        public void setValue(SingleObjState value) {
            throw new UnsupportedOperationException();
        }

        public static SingleObjState of(int nodeId, int objective) {
            return new SingleObjState(nodeId, objective);
        }

        @Override
        public int compareTo(SingleObjState o) {
            return Integer.compare(objective, o.objective);
        }
    }

    /**
     * Direction of the search
     */
    public enum Direction {

        /**
         * Forward search traverses edges according to the direction of the edges.
         */
        FORWARD {
            @Override
            <T extends Edge> List<T> getExpandEdges(Graph<?, T> graph, int nodeId) {
                return graph.getOutEdges(nodeId);
            }

            @Override
            int successorNodeId(Edge edge) {
                return edge.getToId();
            }
        },
        /**
         * Backward direction - from goal to goal(s)
         */
        BACKWARD {
            @Override
            <T extends Edge> List<T> getExpandEdges(Graph<?, T> graph, int nodeId) {
                return graph.getInEdges(nodeId);
            }

            @Override
            int successorNodeId(Edge edge) {
                return edge.getFromId();
            }
        };

        /**
         * Returns a list of edges by which a state located at {@code nodeId} can be expanded
         *
         * @param graph  graph from which the edges are extracted
         * @param nodeId node id of the starting node
         * @param <T>    type of the edges
         *
         * @return list of the edges
         */
        abstract <T extends Edge> List<T> getExpandEdges(Graph<?, T> graph, int nodeId);

        /**
         * Returns id of the node at which the traversal of the {@code edge} ends.
         *
         * @param edge the traversed edge
         *
         * @return id of the end node
         */
        abstract int successorNodeId(Edge edge);
    }

}



















