package cz.cvut.fel.esw.shortestpath.search;

/**
 * Heuristic implementation storing precalculated heuristic values for each node in the graph.
 */
public class PrecalculatedHeuristic implements Heuristic {

    private final int[] heuristic;

    public PrecalculatedHeuristic(int[] heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public int compute(int nodeId) {
        return heuristic[nodeId];
    }

}
