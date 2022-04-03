package cz.cvut.fel.esw.shortestpath.search;

import cz.cvut.fel.esw.shortestpath.graph.Edge;
import cz.cvut.fel.esw.shortestpath.graph.Graph;
import cz.cvut.fel.esw.shortestpath.graph.GraphParser;
import cz.cvut.fel.esw.shortestpath.graph.Node;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DijkstraBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(DijkstraBenchmark.class.getName() + ".*").build();

        new Runner(options).run();
    }

    @Benchmark
    @Fork(value = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureDijkstra(SearchSetup setup) {
        Dijkstra distDijkstra = Dijkstra.create(setup.origin, setup.graph, Edge::getDistance, setup.direction);
        distDijkstra.calculate();
        Dijkstra timeDijkstra = Dijkstra.create(setup.origin, setup.graph, Edge::getTime, setup.direction);
        timeDijkstra.calculate();
    }

    @State(Scope.Benchmark)
    public static class SearchSetup {
        private Graph<Node, Edge> graph;

        @Param({"BACKWARD"})
        //@Param({"FORWARD", "BACKWARD"})
        private Dijkstra.Direction direction;

        private Random random;
        private int origin;

        @Setup(Level.Trial)
        public void loadGraph() throws IOException {
            String zipPath = "data/NY.zip";

            GraphParser parser = new GraphParser();
            this.graph = parser.parseGraph(zipPath);
        }

        @Setup(Level.Iteration)
        public void setRandom() {
            this.random = new Random(0);

        }

        @Setup(Level.Invocation)
        public void setOrigin() {
            this.origin = random.nextInt(graph.numberOfNodes());
        }
    }


}