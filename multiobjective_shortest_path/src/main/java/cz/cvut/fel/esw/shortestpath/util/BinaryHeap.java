package cz.cvut.fel.esw.shortestpath.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Implementation of binary heap inspired by org.teneighty.heap.BinaryHeap.
 * <p>
 * The heap can use its elements as entries if they implement {@link IndexedEntry} interface. However, if you use this
 * option be aware that the entries must be manipulated with care to avoid corrupted state of the heap and thus
 * unpredictable behaviour.
 *
 * @author Marek Cuchý (CVUT)
 */
public class BinaryHeap<E> implements Heap<E> {


    private static final Logger logger = LogManager.getLogger(BinaryHeap.class);

    private static final int INITIAL_SIZE = 20;

    private final Comparator<E> comparator;

    /**
     * Backing array of the heap. Heap starts at 1st index
     */
    private IndexedEntry<E>[] heap;

    private int size;

    @SuppressWarnings("unchecked")
    public BinaryHeap(Comparator<E> comparator) {
        this.comparator = comparator;
        this.heap = new IndexedEntry[INITIAL_SIZE];
        this.size = 0;
    }

    @Override
    public IndexedEntry<E> insert(E value) {
        IndexedEntry<E> entry = getEntry(value);
        if (isEmpty()) {
            insertAtTheEnd(entry);
        } else {
            insertAtTheEnd(entry);
            bubbleUp(entry);
        }
        return entry;
    }

    @Override
    public IndexedEntry<E> extractMinimum() {
        IndexedEntry<E> min = getMinimum();
        delete(min);
        return min;
    }

    @Override
    public IndexedEntry<E> getMinimum() {
        if (isEmpty()) throw new NoSuchElementException();
        return get(1);
    }

    @Override
    public void delete(Entry<E> e) {
        checkEntryContained(e);
        IndexedEntry<E> toBeDeleted = (IndexedEntry<E>) e;
        if (toBeDeleted.getIndex() == size) {
            deleteLast();
        } else {
            IndexedEntry<E> lastEntry = get(size);
            set(toBeDeleted.getIndex(), lastEntry);
            deleteLast();
            if (parentIsGreater(lastEntry)) {
                bubbleUp(lastEntry);
            } else {
                bubbleDown(lastEntry);
            }
        }
    }

    private boolean parentIsGreater(IndexedEntry<E> entry) {
        return existsAndIsGreater(getParent(entry), entry);
    }

    /**
     * Return true iff {@code e1} is not null and is smaller than e2
     *
     * @param e1
     * @param e2
     *
     * @return
     *
     * @throws NullPointerException if e2 is null
     */
    private boolean existsAndIsSmaller(IndexedEntry<E> e1, IndexedEntry<E> e2) {
        return e1 != null && comparator.compare(e1.getValue(), e2.getValue()) < 0;
    }

    /**
     * Return true iff {@code e1} is not null and is greater than e2
     *
     * @param e1
     * @param e2
     *
     * @return
     *
     * @throws NullPointerException if e2 is null
     */
    private boolean existsAndIsGreater(IndexedEntry<E> e1, IndexedEntry<E> e2) {
        return e1 != null && comparator.compare(e1.getValue(), e2.getValue()) > 0;
    }


    private void checkEntryContained(Entry<E> e) {
        if (!(e instanceof IndexedEntry) || get(((IndexedEntry<E>) e).getIndex()) != e) {
            throw new IllegalArgumentException("Entry not contained in this heap: " + e);
        }
    }

    private void bubbleUp(IndexedEntry<E> entry) {
        IndexedEntry<E> parent = getParent(entry);
        while (existsAndIsGreater(parent, entry)) {
            swap(entry, parent);
            parent = getParent(entry);
        }
        set(entry.getIndex(), entry);
    }


    private void bubbleDown(IndexedEntry<E> entry) {
        IndexedEntry<E> child = getSmallestChild(entry);
        while (existsAndIsSmaller(child, entry)) {
            swap(entry, child);
            child = getSmallestChild(entry);
        }
    }

    private void swap(IndexedEntry<E> e1, IndexedEntry<E> e2) {
        int tmpIndex = e2.getIndex();
        set(e1.getIndex(), e2);
        set(tmpIndex, e1);
    }

    private IndexedEntry<E> getSmallestChild(IndexedEntry<E> entry) {
        IndexedEntry<E> leftChild = getLeftChild(entry);
        IndexedEntry<E> rightChild = getRightChild(entry);
        if (existsAndIsSmaller(rightChild, leftChild)) {
            return rightChild;
        } else {
            return leftChild;
        }
    }

    IndexedEntry<E> getParent(IndexedEntry<E> entry) {
        return get(entry.getIndex() / 2);
    }

    IndexedEntry<E> getLeftChild(IndexedEntry<E> entry) {
        return get(entry.getIndex() * 2);
    }

    IndexedEntry<E> getRightChild(IndexedEntry<E> entry) {
        return get(entry.getIndex() * 2 + 1);
    }

    private void deleteLast() {
        heap[size--] = null;
    }

    private void set(int index, IndexedEntry<E> entry) {
        entry.setIndex(index);
        heap[index] = entry;
    }

    IndexedEntry<E> get(int index) {
        if (index > size) return null;
        return heap[index];
    }

    /**
     * Puts the entry at the end of the heap. After this function returns, the heap is most likely in an inconsistent
     * state (its heap property is violated).
     *
     * @param entry
     */
    private void insertAtTheEnd(IndexedEntry<E> entry) {
        ensureCapacity();
        entry.setIndex(++size);
        set(entry.getIndex(), entry);
    }

    @SuppressWarnings("unchecked")
    private IndexedEntry<E> getEntry(E value) {
        IndexedEntry<E> entry;
        if (value instanceof IndexedEntry) {
            entry = (IndexedEntry<E>) value;
        } else {
            entry = new DefaultIndexedEntry<>(value);
        }
        return entry;
    }

    private void ensureCapacity() {
        if (size + 1 >= heap.length) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        IndexedEntry<E>[] resizedHeap = new IndexedEntry[heap.length * 2];
        System.arraycopy(heap, 1, resizedHeap, 1, heap.length - 1);
        this.heap = resizedHeap;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Collection<Entry<E>> entries() {
        return Collections.unmodifiableList(Arrays.asList(heap).subList(1, size + 1));
    }

    @Override
    public Comparator<E> getComparator() {
        return comparator;
    }

    @Override
    public IndexedEntry<E> decrease(Entry<E> e, E newValue) {
        checkEntryContained(e);
        if (comparator.compare(newValue, e.getValue()) > 0) {
            throw new IllegalArgumentException("New value is greater: " + newValue + " > " + e.getValue());
        }
        IndexedEntry<E> indexedEntry = resolveEntry(e, newValue);
        bubbleUp(indexedEntry);
        return indexedEntry;
    }


    /**
     * Returns entry that will be used for the modification and update it if necessary This function is necessary
     * because the heap supports also the states as heap entries if they implement {@link IndexedEntry}.
     *
     * @param entry
     * @param newValue
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private IndexedEntry<E> resolveEntry(Entry<E> entry, E newValue) {
        IndexedEntry<E> indexedEntry;
        if (newValue instanceof BinaryHeap.IndexedEntry) {
            indexedEntry = (IndexedEntry<E>) newValue;
            //replace the old entry with the new value
            set(((IndexedEntry<E>) entry).getIndex(), indexedEntry);
        } else {
            indexedEntry = (IndexedEntry<E>) entry;
            indexedEntry.setValue(newValue);
        }
        return indexedEntry;
    }

    public interface IndexedEntry<TValue> extends Heap.Entry<TValue> {

        int getIndex();

        void setIndex(int index);
    }

    private static class DefaultIndexedEntry<TValue> implements IndexedEntry<TValue> {

        private TValue value;
        private int heapIndex;

        public DefaultIndexedEntry(TValue value) {
            this.value = value;
        }

        @Override
        public TValue getValue() {
            return value;
        }

        @Override
        public void setValue(TValue value) {
            this.value = value;
        }

        @Override
        public int getIndex() {
            return heapIndex;
        }

        @Override
        public void setIndex(int index) {
            this.heapIndex = index;
        }

        @Override
        public String toString() {
            return "BinaryHeapEntry{" + "value=" + value + '}';
        }
    }
}
