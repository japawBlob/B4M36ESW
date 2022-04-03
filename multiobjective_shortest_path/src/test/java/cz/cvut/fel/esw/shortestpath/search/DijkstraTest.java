package cz.cvut.fel.esw.shortestpath.search;

import cz.cvut.fel.esw.shortestpath.graph.Edge;
import cz.cvut.fel.esw.shortestpath.graph.Graph;
import cz.cvut.fel.esw.shortestpath.graph.GraphBuilder;
import cz.cvut.fel.esw.shortestpath.graph.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DijkstraTest {

    static Graph<Node, Edge> graph;

    @BeforeAll
    static void beforeAll() {
        GraphBuilder<Node, Edge> builder = GraphBuilder.createGraphBuilder();

        for (int i = 0; i < 8; i++) {
            builder.addNode(new Node(0, 0, 0));
        }

        builder.addEdge(new Edge(0, 1, 4, 0));

        builder.addEdge(new Edge(0, 1, 4, 0));
        builder.addEdge(new Edge(0, 1, 4, 0));
        builder.addEdge(new Edge(0, 1, 4, 0));
        builder.addEdge(new Edge(0, 1, 4, 0));
        builder.addEdge(new Edge(0, 1, 4, 0));

    }

    @Test
    void compute() {

    }
}