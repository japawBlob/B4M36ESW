package cz.cvut.fel.esw.shortestpath.search;

/**
 * Heuristics estimate objective value required from given node to the goal.
 */
public interface Heuristic {


    /**
     * @param nodeId
     *
     * @return
     */
    int compute(int nodeId);


}
