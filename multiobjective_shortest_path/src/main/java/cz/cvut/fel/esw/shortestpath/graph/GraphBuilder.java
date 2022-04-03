/* This code was originally developed by Umotional s.r.o. (IN: 03974618). */
package cz.cvut.fel.esw.shortestpath.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This graph builder builds graphs where nodes should have IDs from 0 to n.
 *
 * @param <TNode>
 * @param <TEdge>
 */
public class GraphBuilder<TNode extends Node, TEdge extends Edge> {

	private final Map<Integer, TNode> nodesByNodeId = new LinkedHashMap<>();
	private final Map<Edge.EdgeId, TEdge> edgesByFromToNodeIds = new LinkedHashMap<>();
	private final Map<Integer, List<TEdge>> nodeOutgoingEdges = new HashMap<>();
	private final Map<Integer, List<TEdge>> nodeIncomingEdges = new HashMap<>();

	private final boolean subgraph;
	private final boolean multigraph;

	public GraphBuilder() {
		this(false, false);
	}

	public GraphBuilder(boolean subgraph, boolean multigraph) {
		this.subgraph = subgraph;
		this.multigraph = multigraph;
	}

	/**
	 * Adds the node to the builder
	 *
	 * @throws IllegalArgumentException If the node is already contained
	 */
	public void addNode(TNode node) {
		if (containsNode(node)) {
			throw new IllegalArgumentException("Node with this int id already present! Can not add node");
		}
		add(node);
	}

	/**
	 * Adds the nodes to the builder
	 *
	 * @throws IllegalArgumentException If any of the nodes is already contained
	 */
	public void addNodes(Collection<? extends TNode> nodes) {
		for (TNode node : nodes) {
			addNode(node);
		}
	}

	private void add(TNode node) {
		nodesByNodeId.put(node.getId(), node);
		nodeOutgoingEdges.put(node.getId(), new ArrayList<>());
		nodeIncomingEdges.put(node.getId(), new ArrayList<>());

	}

	/**
	 * Removes given node from the builder.
	 *
	 * @param node
	 *
	 * @throws IllegalArgumentException If the node is does not exist or if it has adjacent edges.
	 */
	public void removeNode(TNode node) {
		if (!containsNode(node)) {
			throw new IllegalArgumentException("Node with this id does not exist. Cannot be removed.");
		}
		if (!nodeOutgoingEdges.get(node.getId()).isEmpty() || !nodeIncomingEdges.get(node.getId()).isEmpty()) {
			throw new IllegalArgumentException("Node has adjacent edges. Cannot be removed.");
		}
		nodesByNodeId.remove(node.getId());
		nodeOutgoingEdges.remove(node.getId());
		nodeIncomingEdges.remove(node.getId());
	}

	public void removeEdge(TEdge edge) {
		if (!containsEdge(edge)) {
			throw new IllegalArgumentException("Edge is not in this builder. Cannot be removed.");
		}
		if (!nodeOutgoingEdges.get(edge.getFromId()).remove(edge)) {
			throw new IllegalArgumentException("Edge is not equal to the one in the builder");
		}
		if (!nodeIncomingEdges.get(edge.getToId()).remove(edge)) {
			throw new IllegalArgumentException("Edge is not equal to the one in the builder");
		}
		edgesByFromToNodeIds.remove(edge.getEdgeId());
	}

	private boolean containsNode(TNode node) {
		return containsNode(node.getId());
	}

	public boolean containsNode(int nodeId) {
		return getNode(nodeId) != null;
	}

	public TNode getNode(int nodeId) {
		return nodesByNodeId.get(nodeId);
	}

	/**
	 * Adds all not contained edges.
	 */
	public void addEdges(Collection<? extends TEdge> edges) {
		edges.stream().filter(edge -> !containsEdge(edge)).forEach(this::addEdge);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException If built graph is not multigraph and an edge between the same two nodes already
	 *                                  contained or one of the end nodes is not contained.
	 */
	public void addEdge(TEdge edge) { // we may think about "creating edges" inside graph builder because of ids

		checkNodesExistence(edge);

		Edge.EdgeId edgeId = edge.getEdgeId();

		if (!multigraph && edgesByFromToNodeIds.containsKey(edgeId))
			throw new IllegalArgumentException("Edge has not to exist yet");

		List<TEdge> outgoingEdgesFromNode = nodeOutgoingEdges.get(edge.getFromId());
		List<TEdge> incomingEdgesToNode = nodeIncomingEdges.get(edge.getToId());

		outgoingEdgesFromNode.add(edge);
		incomingEdgesToNode.add(edge);

		edgesByFromToNodeIds.put(edgeId, edge);
		nodeOutgoingEdges.put(edge.getFromId(), outgoingEdgesFromNode);
		nodeIncomingEdges.put(edge.getToId(), incomingEdgesToNode);
	}

	protected void checkNodesExistence(TEdge edge) {
		if (!containsNode(edge.getFromId()) || !containsNode(edge.getToId()))
			throw new IllegalArgumentException("Node has to be in graph builder before inserting edge");
	}

	public void clear() {
		this.nodesByNodeId.clear();
		this.edgesByFromToNodeIds.clear();
		this.nodeOutgoingEdges.clear();
		this.nodeIncomingEdges.clear();
	}

	public boolean containsEdge(int fromId, int toId) {
		TEdge edge = getEdge(fromId, toId);
		return edge != null;
	}

	public boolean containsEdge(TEdge edge) {
		return edgesByFromToNodeIds.containsKey(edge.getEdgeId());
	}

	public TEdge getEdge(int fromId, int toId) {
		return edgesByFromToNodeIds.get(Edge.EdgeId.of(fromId, toId));
	}

	public List<TEdge> getInEdges(int nodeId) {
		return nodeIncomingEdges.get(nodeId);
	}

	public List<TEdge> getOutEdges(int nodeId) {
		return nodeOutgoingEdges.get(nodeId);
	}

	public Graph<TNode, TEdge> createGraph() {
		Graph<TNode, TEdge> graph = dumpCurrentGraph();
		clear();
		return graph;
	}

	/**
	 * Creates a final graph and keeps the structures ready for further building of the graph.
	 *
	 * @return current state of the <code>Graph</code>
	 */
	public Graph<TNode, TEdge> dumpCurrentGraph() {
		if (!subgraph) checkNodes();

		List<TNode> nodesByNodeIdList = createNodesById();

		int n = nodesByNodeIdList.size();
		int m = edgesByFromToNodeIds.size();

		int[] outgoingPositions = new int[n + 1];
		List<TEdge> outgoingEdges = new ArrayList<>(m);
		fillEdgeStructures(n, outgoingPositions, outgoingEdges, nodeOutgoingEdges);
		outgoingEdges = List.copyOf(outgoingEdges); // make edge list immutable
		List<List<TEdge>> outgoingEdgesCache = createEdgeCache(outgoingPositions, outgoingEdges);

		int[] incomingPositions = new int[n + 1];
		List<TEdge> incomingEdges = new ArrayList<>(m);
		fillEdgeStructures(n, incomingPositions, incomingEdges, nodeIncomingEdges);
		incomingEdges = List.copyOf(incomingEdges);
		List<List<TEdge>> incomingEdgesCache = createEdgeCache(incomingPositions, incomingEdges);

		List<TNode> allNodes = nodesByNodeIdList.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());
		if (allNodes.size() == nodesByNodeIdList.size()) {
			allNodes = nodesByNodeIdList;
		}

		return new Graph<>(nodesByNodeIdList, allNodes, outgoingPositions, outgoingEdges, outgoingEdgesCache, incomingEdgesCache);
	}

	void fillEdgeStructures(int n, int[] incomingPositions, List<TEdge> incomingEdges, Map<Integer, List<TEdge>> edgeMap) {
		// iterate over all node IDs.
		int l = 0; // edges id
		for (int k = 0; k < n; k++) {

			// assign position
			incomingPositions[k] = l;

			if (!edgeMap.containsKey(k)) continue;
			// iterate over edges
			for (TEdge edge : edgeMap.get(k)) {
				incomingEdges.add(edge);
				l++;
			}
		}
		// set numberOfNodes+1 of positions to current edge id as indentation
		incomingPositions[n] = l;
	}

	List<List<TEdge>> createEdgeCache(int[] edgePositions, List<TEdge> edges) {
		List<List<TEdge>> outgoingEdgesCache = new ArrayList<>(edgePositions.length - 1);
		for (int i = 0; i < edgePositions.length - 1; i++) {
			if (!nodesByNodeId.containsKey(i)) {
				outgoingEdgesCache.add(null);
			} else {
				outgoingEdgesCache.add(edges.subList(edgePositions[i], edgePositions[i + 1]));
			}
		}
		return outgoingEdgesCache;
	}

	List<TNode> createNodesById() {
		if (nodesByNodeId.isEmpty()) return Collections.emptyList();
		int maxNodeId = nodesByNodeId.keySet().stream().max(Integer::compare).get();
		List<TNode> nodesByNodeIdList = new ArrayList<>(maxNodeId + 1);
		for (int i = 0; i < maxNodeId + 1; i++) {
			nodesByNodeIdList.add(nodesByNodeId.get(i));
		}
		return List.copyOf(nodesByNodeIdList);
	}

	/**
	 * Check if all nodes with IDs from 0 to (n-1) are contained.
	 */
	private void checkNodes() {
		// check node ids sequence
		for (int i = 0; i < nodesByNodeId.keySet().size(); i++) {
			TNode node = nodesByNodeId.get(i);
			if (node == null) {
				throw new NoSuchElementException(" Node with id " + i + " not present! The sequence of nodes id must" + " " + "start with 0 and end with 'numOfNodes-1'");
			}
		}
	}

	public Collection<TNode> getAllNodes() {
		return Collections.unmodifiableCollection(nodesByNodeId.values());
	}

	public Collection<TEdge> getAllEdges() {
		return Collections.unmodifiableCollection(edgesByFromToNodeIds.values());
	}

	/**
	 * Creates induced subgraph of the {@code graph} containing only nodes with IDs from the input set.
	 *
	 * @param graph
	 * @param nodes
	 */
	public static <TNode extends Node, TEdge extends Edge> Graph<TNode, TEdge> induceSubGraph(Graph<TNode, TEdge> graph, Set<Integer> nodes) {
		GraphBuilder<TNode, TEdge> builder = new GraphBuilder<>(true, true);
		graph.getAllNodes().stream().filter(n -> nodes.contains(n.getId())).forEach(builder::addNode);
		graph.getAllEdges().stream().filter(e -> nodes.contains(e.getFromId()) && nodes.contains(e.getToId())).forEach(builder::addEdge);
		return builder.createGraph();
	}

	/**
	 * Creates builder that requires to use nodes with IDs in range [0,n). If the create methods are invoked and the
	 * resulting graph wouldn't satisfy this condition a {@code NoSuchElementException} is thrown.
	 *
	 * @param <TNode>
	 * @param <TEdge>
	 *
	 * @return
	 */
	public static <TNode extends Node, TEdge extends Edge> GraphBuilder<TNode, TEdge> createGraphBuilder() {
		return new GraphBuilder<>(false, false);
	}

	/**
	 * Creates builder that have no specific conditions on the graph. But do not make the IDs unnecessarily large. The
	 * larger the max ID is the larger memory requirements will be.
	 *
	 * @param <TNode>
	 * @param <TEdge>
	 *
	 * @return
	 */
	public static <TNode extends Node, TEdge extends Edge> GraphBuilder<TNode, TEdge> createSubGraphBuilder() {
		return new GraphBuilder<>(true, false);
	}
}
