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
import java.util.concurrent.TimeUnit;

public class MultiObjectiveAStarBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(MultiObjectiveAStarBenchmark.class.getName() + ".*").build();

        new Runner(options).run();
    }

    @Benchmark
    @Fork(value = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureMultiObjectiveAStar(SearchSetup setup) {
        MultiObjectiveAStar search = MultiObjectiveAStar.createDefault(setup.graph, setup.origin, setup.goal);
        if (search.calculate().size() != setup.resultNumber) throw new IllegalStateException();
    }


    @State(Scope.Benchmark)
    public static class SearchSetup {
        private Graph<Node, Edge> graph;

        private int origin = 0;
        private int goal = 50000;

        private int resultNumber = 278;

        @Setup(Level.Trial)
        public void loadGraph() throws IOException {
            String zipPath = "data/NY.zip";
            GraphParser parser = new GraphParser();
            this.graph = parser.parseGraph(zipPath);
        }
    }


}