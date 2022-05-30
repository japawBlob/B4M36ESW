package cz.cvut.fel.esw.nonblock.map;

import com.vmlens.api.AllInterleavings;
import cz.cvut.fel.esw.nonblock.map.vmlens.VmlensTestRunner;
import org.junit.jupiter.api.Test;

import java.util.Dictionary;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * It has to be run by 'mvn test' or with Eclipse plugin (I'm not aware of any other supported IDE)
 */
public class VmlensStringSetTest {

    Supplier<StringSet> testedSet = () -> new NonblockStringSet(1);
//    Supplier<StringSet> testedSet = () -> new SynchronizedStringSet(5);


    /**
     * This test is identical to {@link #testTwoAddsRunner()}
     */
    @Test
    public void testTwoAddsBasic() throws InterruptedException {
        try (AllInterleavings allInterleavings = new AllInterleavings(VmlensTestRunner.getMethodName(2))) {
            while (allInterleavings.hasNext()) {
                StringSet set = testedSet.get();
                set.add("a");

                Thread t1 = new Thread(() -> set.add("b"));
                Thread t2 = new Thread(() -> set.add("c"));

                t1.start();
                t2.start();

                t1.join();
                t2.join();

                assertTrue(set.contains("a"));
                assertTrue(set.contains("b"));
                assertTrue(set.contains("c"));
                assertEquals(3, set.size());
            }
        }
    }

    /**
     * This test is identical to {@link #testTwoAddsBasic()}
     */
    @Test
    public void testTwoAddsRunner() throws InterruptedException {
        VmlensTestRunner runner = new VmlensTestRunner(testedSet);
        runner.initAdd("a");

        runner.taskAdd("b");
        runner.taskAdd("c");


        runner.assertContains("a");
        runner.assertContains("b");
        runner.assertContains("c");
        runner.assertSize(3);

        runner.executeVmlens();
    }

    @Test
    public void testTwoAddsAlreadyContainedRunner() throws InterruptedException {
        VmlensTestRunner runner = new VmlensTestRunner(testedSet);

        runner.taskAdd("a");
        runner.taskAdd("a");
        runner.taskAdd("b");
        runner.taskAdd("b");


        runner.assertContains("a");
        runner.assertContains("a");
        runner.assertSize(2);

        runner.executeVmlens();
    }

}