package cz.cvut.fel.esw.shortestpath.graph;

import cz.cvut.fel.esw.shortestpath.util.FileParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.zip.ZipFile;

/**
 * Parser that parses graphs from a zip file containing three files in
 * <a href="http://users.diag.uniroma1.it/challenge9/format.shtml"> DIMACS format </a>.
 * One file contains nodes coordinates, second contains time attribute of edges and the third contains distance
 * attribute.
 *
 * @author Marek Cuchý (CVUT)
 */
public class GraphParser {

    private static final Logger logger = LogManager.getLogger(GraphParser.class);

    private List<Node> nodes = new ArrayList<>();
    private List<EdgeBuilder> edges = new ArrayList<>();

    private int readEdges;

    public Graph<Node, Edge> parseGraph(String zipPath) throws IOException {
        File file = new File(zipPath);
        ZipFile zip = new ZipFile(file);
        String instanceName = extractInstanceName(file);

        readNodes(zip, instanceName);
        readDistanceEdges(zip, instanceName);
        readTimeEdges(zip, instanceName);
        zip.close();

        GraphBuilder<Node, Edge> builder = GraphBuilder.createGraphBuilder();
        for (Node node : nodes) {
            //there can be duplicates in the data
            if (!builder.containsNode(node.getId())) {
                builder.addNode(node);
            }
        }

        for (EdgeBuilder edge : edges) {
            //there can be duplicates in the data
            if (!builder.containsEdge(edge.getFromId(), edge.getToId())) {
                builder.addEdge(edge.build());
            }
        }

        return builder.createGraph();
    }

    private void readTimeEdges(ZipFile zip, String instanceName) throws IOException {
        readEdges = 0;
        FileParser edgeTimeParser = new FileParser(zip, instanceName + "-t.gr", 3, this::parseTimeEdgeLine);
        edgeTimeParser.parse();
    }

    private void readDistanceEdges(ZipFile zip, String instanceName) throws IOException {
        FileParser edgeParser = new FileParser(zip, instanceName + "-d.gr", 3, this::parseDistanceEdgeLine);
        edgeParser.parse();
    }

    private void readNodes(ZipFile zip, String instanceName) throws IOException {
        FileParser nodeParser = new FileParser(zip, instanceName + ".co", 4, this::parseNodeLine);
        nodeParser.parse();
    }

    private void parseTimeEdgeLine(String[] lineSplit) {
        parseEdgeLine(lineSplit, EdgeBuilder::setTime);
    }

    private void parseDistanceEdgeLine(String[] lineSplit) {
        parseEdgeLine(lineSplit, EdgeBuilder::setDistance);
    }

    private void parseEdgeLine(String[] lineSplit, BiConsumer<EdgeBuilder, Integer> costSetter) {
        if (!"a".equals(lineSplit[0])) {
            throw new IllegalStateException("Unknown line start: " + lineSplit[0]);
        }
        int fromId = Integer.parseInt(lineSplit[1]) - 1; //minus one to have ids started from 0
        int toId = Integer.parseInt(lineSplit[2]) - 1; //minus one to have ids started from 0
        int cost = Integer.parseInt(lineSplit[3]);

        EdgeBuilder edge = getOrCreateEdge(fromId, toId);
        costSetter.accept(edge, cost);
    }

    private String extractInstanceName(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.indexOf("."));
    }

    private void parseNodeLine(String[] lineSplit) {
        if (!"v".equals(lineSplit[0])) {
            throw new IllegalStateException("Unknown line start: " + lineSplit[0]);
        }
        int id = Integer.parseInt(lineSplit[1]) - 1; //minus one to have ids started from 0
        int latE6 = Integer.parseInt(lineSplit[2]);
        int lonE6 = Integer.parseInt(lineSplit[3]);
        nodes.add(new Node(id, latE6, lonE6));
    }

    private EdgeBuilder getOrCreateEdge(int fromId, int toId) {

        if (edges.size() <= readEdges) {
            edges.add(new EdgeBuilder(fromId, toId));
        }

        EdgeBuilder edge = edges.get(readEdges++);
        assert edge.getFromId() == fromId && edge.getToId() == toId;
        return edge;
    }
}
