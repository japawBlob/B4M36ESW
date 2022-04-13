package cz.cvut.fel.esw.nonblock.map.vmlens;

import com.vmlens.api.AllInterleavings;
import cz.cvut.fel.esw.nonblock.map.StringSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vmlens test runner
 */
public class VmlensTestRunner {

    /**
     * Tasks that are executed sequentially before the parallel tasks.
     */
    private final List<Consumer<StringSet>> initTasks = new ArrayList<>();
    /**
     * Tasks to be executed in parallel in the test.
     */
    private final List<Consumer<StringSet>> parallelTasks = new ArrayList<>();

    /**
     * Asserts to be checked sequentially after all the parallel tasks are finished.
     */
    private final List<Consumer<StringSet>> asserts = new ArrayList<>();

    /**
     * Creator of the test to be tested.
     */
    private final Supplier<StringSet> testedSet;

    /**
     * The supplier should always provide new instance
     *
     * @param testedSet supplier of the set to be tested
     */
    public VmlensTestRunner(Supplier<StringSet> testedSet) {
        this.testedSet = testedSet;
    }


    public VmlensTestRunner taskAdd(String value) {
        return task((set) -> set.add(value));
    }

    /**
     * Adds a task to be executed in parallel. It runs after the initialization.
     */
    public VmlensTestRunner task(Consumer<StringSet> task) {
        parallelTasks.add(task);
        return this;
    }

    public VmlensTestRunner initAdd(String value) {
        return init((set) -> set.add(value));
    }

    /**
     * Adds a task to be executed as initialized of the set. It runs sequentially before the parallel tasks.
     */
    public VmlensTestRunner init(Consumer<StringSet> task) {
        initTasks.add(task);
        return this;
    }

    public VmlensTestRunner assertContains(String value) {
        return assrt((set) -> assertTrue(set.contains(value), "Assert contains " + value));
    }

    public VmlensTestRunner assertNotContained(String value) {
        return assrt((set) -> assertFalse(set.contains(value), "Assert not contains " + value));
    }

    public VmlensTestRunner assertSize(int expectedSize) {
        return assrt((set) -> assertEquals(expectedSize, set.size(), "Assert size " + expectedSize));
    }

    public VmlensTestRunner assrt(Consumer<StringSet> a) {
        asserts.add(a);
        return this;
    }

    /**
     * If you need correct method name to be calculated for the test, it must be used directly in the test method of
     * which name you want to be displayed in the results. Otherwise use {@link #executeVmlens(String)}
     */
    public void executeVmlens() throws InterruptedException {
        executeVmlens(getMethodName(3));
    }

    public void executeVmlens(String name) throws InterruptedException {
        try (AllInterleavings allInterleavings = new AllInterleavings(name)) {
            while (allInterleavings.hasNext()) {
                execute();
            }
        }
    }

    public void execute() throws InterruptedException {
        StringSet set = testedSet.get();

        initTasks.forEach(init -> init.accept(set));


        List<Thread> threads = parallelTasks.stream()
                                            .map(task -> new Thread(() -> task.accept(set)))
                                            .collect(Collectors.toList());
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }


        asserts.forEach(assrt -> assrt.accept(set));
    }

    public static String getMethodName(int stackLevel) {
        return Thread.currentThread().getStackTrace()[stackLevel].getMethodName();
    }
}
