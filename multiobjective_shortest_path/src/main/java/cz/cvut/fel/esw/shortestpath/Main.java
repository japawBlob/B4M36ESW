package cz.cvut.fel.esw.shortestpath;

import cz.cvut.fel.esw.shortestpath.graph.Edge;
import cz.cvut.fel.esw.shortestpath.graph.Graph;
import cz.cvut.fel.esw.shortestpath.graph.GraphParser;
import cz.cvut.fel.esw.shortestpath.graph.Node;
import cz.cvut.fel.esw.shortestpath.search.Dijkstra;
import cz.cvut.fel.esw.shortestpath.search.MultiObjectiveAStar;
import cz.cvut.fel.esw.shortestpath.search.SearchState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.util.List;

/**
 * @author Marek Cuchý (CVUT)
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);

        String zipPath = "data/NY.zip";


        GraphParser parser = new GraphParser();
        Graph<Node, Edge> graph = parser.parseGraph(zipPath);


        Dijkstra dijkstra = Dijkstra.createForward(0, graph, Edge::getDistance);
        dijkstra.calculate();

        //try also different start/goal pairs
        int start = 0;
        int goal = 50000;

        //loop for "infinite" run
        for (int i = 0; i < 100000000; i++) {

            long t1 = System.currentTimeMillis();
            MultiObjectiveAStar search = MultiObjectiveAStar.createDefault(graph, start, goal);
            List<SearchState> results = search.calculate();
            long t2 = System.currentTimeMillis();

            logger.info("Search time: " + (t2 - t1) + "ms");
            logger.info("Number of results: " + results.size());
        }
    }


}
