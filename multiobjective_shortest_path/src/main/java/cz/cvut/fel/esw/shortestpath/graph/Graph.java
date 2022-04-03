/* This code was originally developed by Umotional s.r.o. (IN: 03974618). */
package cz.cvut.fel.esw.shortestpath.graph;

import java.util.Collection;
import java.util.List;

/**
 * Immutable representation of a directed graph with nodes indexed from 0 to n.
 *
 * @param <TNode>
 * @param <TEdge>
 */
public class Graph<TNode extends Node, TEdge extends Edge> {


    /**
     * Array of nodes indexed by node IDs (size = max(nodeId) + 1). Positions set to {@code null} means that there is no
     * such node with the id.
     */
    private final List<TNode> allNodesByNodeId;

    /**
     * List of all nodes (size = number of nodes).
     */
    private final List<TNode> allNodes;

    /**
     * Starting positions of outgoing edges in the edge list (size = max(nodeId) + 1).
     */
    private final int[] outgoingPositions;

    /**
     * Array of outgoing edges indexed by starting positions array (size = numberOfEdges).
     */
    private final List<TEdge> outgoingEdges;

    /**
     * Cache of edge lists for outgoing edges (size = max(nodeId) + 1).
     */
    private final List<List<TEdge>> outgoingEdgesCache;

    /**
     * Cache of edge lists for incoming edges (size = max(nodeId) + 1).
     */
    private final List<List<TEdge>> incomingEdgesCache;

    Graph(List<TNode> allNodesByNodeId, List<TNode> allNodes, int[] outgoingPositions, List<TEdge> outgoingEdges, List<List<TEdge>> outgoingEdgesCache, List<List<TEdge>> incomingEdgesCache) {
        this.allNodesByNodeId = allNodesByNodeId;
        this.allNodes = allNodes;
        this.outgoingPositions = outgoingPositions;
        this.outgoingEdges = outgoingEdges;
        this.outgoingEdgesCache = outgoingEdgesCache;
        this.incomingEdgesCache = incomingEdgesCache;
    }

    public boolean containsNode(int nodeId) {
        return getNode(nodeId) != null;
    }

    public TNode getNode(int nodeId) {
        return ((0 <= nodeId) && (nodeId < allNodesByNodeId.size())) ? allNodesByNodeId.get(nodeId) : null;
    }

    public boolean containsEdge(TEdge edge) {
        return containsEdge(edge.getFromId(), edge.getToId());
    }

    public boolean containsEdge(int fromId, int toId) {
        return getEdge(fromId, toId) != null;
    }

    public TEdge getEdge(int fromNodeId, int toNodeId) {
        // scan edges from outgoingPositions[fromNodeId] to (outgoingPositions[fromNodeId+1]-1)
        // sequential approach still faster than map, operation not used very often
        for (int j = outgoingPositions[fromNodeId]; j < (outgoingPositions[fromNodeId + 1]); j++) {
            if (outgoingEdges.get(j).getToId() == toNodeId) {
                return outgoingEdges.get(j);
            }
        }
        return null;
    }

    public List<TEdge> getInEdges(int nodeId) {
        return incomingEdgesCache.get(nodeId);
    }

    public List<TEdge> getOutEdges(int nodeId) {
        return outgoingEdgesCache.get(nodeId);
    }

    public Collection<TNode> getAllNodes() {
        return allNodes;
    }

    public Collection<TEdge> getAllEdges() {
        return outgoingEdges;
    }

    public int numberOfNodes() {
        return allNodes.size();
    }

    @Override
    public String toString() {
        return "Graph [#nodes=" + allNodes.size() + ", #edges=" + outgoingEdges.size() + "]";
    }
}
