package cz.cvut.fel.esw.nonblock.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    private final static int MAX_THREADS = 10;
    private final static int TOTAL_BENCHMARK_OPERATIONS = 1_000_000;
    private final static int REPEAT_BENCHMARK = 5;

    private final static int STRING_LENGTH = 4;

    private static long test(int threads, StringSet set) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        List<StringSetWriter> writers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            writers.add(new StringSetWriter(TOTAL_BENCHMARK_OPERATIONS / threads, set, STRING_LENGTH, ThreadLocalRandom.current()));
        }

        long t1 = System.nanoTime();

        List<Future<List<String>>> futures = executor.invokeAll(writers);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
        long t2 = System.nanoTime();

        Set<String> words = new HashSet<>();
        for (Future<List<String>> future : futures) {
            words.addAll(future.get());
        }

        int actualSize = set.size();
        int expectedSize = words.size();


        if (actualSize == expectedSize) {
            System.out.println("CORRECT: " + actualSize);
        } else {
            System.out.println("!!!!!!!INCORRECT!!!!!!!!!: expected=" + expectedSize + ", actual=" + actualSize);
        }

        System.out.println(threads + " thread(s) " + set.getClass()
                                                        .getSimpleName() + " TIME: " + (t2 - t1) / 1_000_000 + " ms");
        return t2 - t1;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        for (int i = 1; i < MAX_THREADS; i++) {
            for (int j = 0; j < REPEAT_BENCHMARK; j++) {
                long time1 = test(i, new SynchronizedStringSet(10000));
                //long time2 = test(i, new NonblockDictionary(10000));
            }
        }
    }
}
