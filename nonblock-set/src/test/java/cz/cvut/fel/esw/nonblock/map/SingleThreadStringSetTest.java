package cz.cvut.fel.esw.nonblock.map;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SingleThreadStringSetTest {

    static final Random RND = new Random(0);

    protected abstract StringSet createSet();

    public static class NonblockStringSetTest extends SingleThreadStringSetTest {

        @Override
        protected StringSet createSet() {
            return new NonblockStringSet(4);
        }
    }

    public static class SynchronizedStringSetTest extends SingleThreadStringSetTest {

        @Override
        protected StringSet createSet() {
            return new SynchronizedStringSet(4);
        }
    }

    @Test
    void testSizeAllDistinct() {
        StringSet set = createSet();
        List<String> strings = Utils.generateDistinctRandomStrings(4, 100, RND);
        set.addAll(strings);
        assertEquals(100, set.size());
    }

    @Test
    void testSizeAllRepeated() {
        StringSet set = createSet();
        List<String> strings = Utils.generateDistinctRandomStrings(4, 100, RND);
        set.addAll(strings);
        set.addAll(strings);
        assertEquals(100, set.size());
    }

    @Test
    void testContains() {
        StringSet set = createSet();
        List<String> strings = Utils.generateDistinctRandomStrings(4, 100, RND);
        List<String> added = strings.subList(0, 50);
        List<String> notAdded = strings.subList(added.size(), strings.size());
        set.addAll(added);
        for (String word : added) {
            assertTrue(set.contains(word));
        }
        for (String word : notAdded) {
            assertFalse(set.contains(word));
        }
    }
}
