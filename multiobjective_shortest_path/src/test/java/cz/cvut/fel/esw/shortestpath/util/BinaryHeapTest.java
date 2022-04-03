package cz.cvut.fel.esw.shortestpath.util;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BinaryHeapTest {

    enum Operation {ADD, DELETE, EXTRACT_MIN, DECREASE}

    @Test
    void insert() {
        Heap<Integer> heap = createHeap();
        heap.insert(5);
        heap.insert(-1);
        heap.insert(3);
        heap.insert(5);

        assertEquals(4, heap.size());
    }

    @Test
    void extractMinimum() {
        Heap<Integer> heap = createHeap();
        heap.insert(5);
        heap.insert(-1);
        heap.insert(3);
        heap.insert(5);

        assertEquals(-1, heap.extractMinimum().getValue());
        assertEquals(3, heap.extractMinimum().getValue());
        assertEquals(5, heap.extractMinimum().getValue());
        assertEquals(5, heap.extractMinimum().getValue());

        assertTrue(heap.isEmpty());
    }

    @Test
    void delete() {
        Heap<Integer> heap = createHeap();

        heap.insert(5);
        heap.insert(-1);
        Heap.Entry<Integer> entry = heap.insert(3);
        heap.insert(5);

        heap.delete(entry);

        assertEquals(3, heap.size());

        assertEquals(-1, heap.extractMinimum().getValue());
        assertEquals(5, heap.extractMinimum().getValue());
        assertEquals(5, heap.extractMinimum().getValue());

        assertTrue(heap.isEmpty());
    }

    @Test
    void size() {
        Heap<Integer> heap = createHeap();

        heap.insert(5);
        heap.insert(-1);
        heap.insert(3);
        heap.insert(5);

        assertEquals(4, heap.size());
    }


    @Test
    void decrease() {
        BinaryHeap<Integer> heap = createHeap();

        heap.insert(5);
        heap.insert(-1);
        Heap.Entry<Integer> e3 = heap.insert(3);
        Heap.Entry<Integer> e5 = heap.insert(5);
        heap.insert(2);
        heap.insert(8);

        heap.decrease(e3, 0);
        checkValidity(heap);


        assertEquals(-1, heap.extractMinimum().getValue());
        assertEquals(0, heap.extractMinimum().getValue());

        heap.decrease(e5, -10);

        checkValidity(heap);
        assertEquals(-10, heap.extractMinimum().getValue());

        checkValidity(heap);
    }

    @Test
    void complexTest() {
        Random rnd = new Random(0);
        BinaryHeap<Integer> heap = createHeap();
        int initialSize = 5000;

        List<Integer> toBeAdded = rnd.ints(initialSize).boxed().collect(Collectors.toList());
        List<Heap.Entry<Integer>> entries = new ArrayList<>();
        for (Integer integer : toBeAdded) {
            entries.add(heap.insert(integer));
            checkValidity(heap);
        }

        Operation[] operations = Operation.values();
        while (!heap.isEmpty()) {
            Operation operation = operations[rnd.nextInt(operations.length)];
            int previousSize = heap.size();
            switch (operation) {
                case ADD:
                    entries.add(heap.insert(rnd.nextInt()));
                    assertEquals(previousSize + 1, heap.size());
                    break;
                case DELETE:
                    int index = rnd.nextInt(entries.size());
                    Heap.Entry<Integer> entry = entries.remove(index);
                    heap.delete(entry);
                    assertEquals(previousSize - 1, heap.size());
                    assertFalse(heap.entries().contains(entry));
                    break;
                case EXTRACT_MIN:
                    Heap.Entry<Integer> min = heap.extractMinimum();
                    assertEquals(previousSize - 1, heap.size());
                    Optional<Heap.Entry<Integer>> minEntry = entries.stream().min(Comparator.comparingInt(Heap.Entry::getValue));
                    assertTrue(minEntry.isPresent());
                    assertSame(min, minEntry.get());
                    entries.remove(min);
                    break;
                case DECREASE:
                    Heap.Entry<Integer> decreasedEntry = entries.get(rnd.nextInt(entries.size()));

                    int oldValue = decreasedEntry.getValue();
                    int newValue;

                    if (oldValue <= 0) {
                        newValue = oldValue - rnd.nextInt(oldValue - Integer.MIN_VALUE);
                    } else {
                        newValue = oldValue - rnd.nextInt(Integer.MAX_VALUE);
                    }

                    heap.decrease(decreasedEntry, newValue);
                    assertEquals(newValue, decreasedEntry.getValue());
                    assertEquals(previousSize, heap.size());
                    break;
            }
            checkValidity(heap);
        }
    }

    /**
     * This test checks behaviour when the element class implements IndexedEntry
     */
    @Test
    void elementAsEntries() {
        BinaryHeap<EntryValue> heap = createHeap();

        EntryValue e5 = new EntryValue(5);
        EntryValue e3 = new EntryValue(3);
        EntryValue e0 = new EntryValue(0);
        EntryValue e8 = new EntryValue(8);

        assertSame(e5, heap.insert(e5));
        assertSame(e3, heap.insert(e3));
        assertSame(e0, heap.insert(e0));
        assertSame(e8, heap.insert(e8));

        assertSame(e0, heap.extractMinimum());

        checkValidity(heap);

        EntryValue e4 = new EntryValue(4);
        assertSame(e4, heap.decrease(e8, e4));

        assertEquals(3, heap.size());
        checkValidity(heap);

        assertSame(e3, heap.extractMinimum());
        assertSame(e4, heap.extractMinimum());


    }

    private <T extends Comparable<T>> BinaryHeap<T> createHeap() {
        return new BinaryHeap<>(Comparator.<T>naturalOrder());
    }


    private static <E> void checkValidity(BinaryHeap<E> heap) {
        checkEntryIndexes(heap);
        checkHeapProperty(heap);
    }

    private static <E> void checkEntryIndexes(BinaryHeap<E> heap) {
        for (int i = 0; i <= heap.size(); i++) {
            BinaryHeap.IndexedEntry<E> entry = heap.get(i);
            if (entry != null && i != entry.getIndex()) {
                throw new IllegalStateException("Heap index is not equal " + "entry index.");
            }
        }
    }

    private static <T> void checkHeapProperty(BinaryHeap<T> heap) {
        for (int i = heap.size(); i >= 2; i--) {
            BinaryHeap.IndexedEntry<T> child = heap.get(i);
            BinaryHeap.IndexedEntry<T> parent = heap.getParent(child);
            if (heap.getComparator().compare(parent.getValue(), child.getValue()) > 0) {
                throw new IllegalStateException("Heap property violated: " + "parent=" + parent + ", child=" + child);
            }
        }
    }

    private static class EntryValue implements BinaryHeap.IndexedEntry<EntryValue>, Comparable<EntryValue> {

        private final int value;
        private int heapIndex;

        public EntryValue(int value) {
            this.value = value;
        }

        @Override
        public Integer getIndex() {
            return heapIndex;
        }

        @Override
        public void setIndex(Integer index) {
            this.heapIndex = index;
        }

        @Override
        public EntryValue getValue() {
            return this;
        }

        @Override
        public void setValue(EntryValue value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(EntryValue o) {
            return Integer.compare(value, o.value);
        }
    }
}