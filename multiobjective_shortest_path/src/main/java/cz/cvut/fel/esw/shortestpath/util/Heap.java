package cz.cvut.fel.esw.shortestpath.util;

import java.util.Collection;
import java.util.Comparator;

/**
 * @param <E> type of the elements in the heap
 *
 * @author Marek Cuchý (CVUT)
 */
public interface Heap<E> {

    /**
     * Inserts the value into the heap.
     *
     * @param value to be inserted into the heap
     *
     * @return entry holding the value
     */
    Entry<E> insert(E value);

    /**
     * Extracts entry holding the minimum value. It DOES delete the entry from the heap as opposed to {@link
     * #getMinimum()}.
     *
     * @return the entry holding the minimum value and deleted from this heap
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     * @see #getMinimum()
     */
    Entry<E> extractMinimum();

    /**
     * Gets entry holding the minimum value. It does NOT delete the entry from the heap as opposed to {@link
     * #extractMinimum()}.
     *
     * @return the entry holding the minimum value
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     * @see #extractMinimum()
     */
    Entry<E> getMinimum();

    /**
     * Deletes {@code entry} from the heap.
     *
     * @param entry to be deleted
     *
     * @throws IllegalArgumentException if the @{@code entry} is not contained in this heap
     */
    void delete(Entry<E> entry);

    /**
     * Gets the number of elements in the heap.
     *
     * @return the number of elements in the heap
     */
    int size();


    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    Collection<Entry<E>> entries();

    Comparator<E> getComparator();

    /**
     * Decreases the {@code entry} with {@code newValue} and moves it in this heap.
     *
     * @param entry
     * @param newValue
     *
     * @return
     */
    Entry<E> decrease(Entry<E> entry, E newValue);

    interface Entry<E> {
        E getValue();

        void setValue(E value);
    }
}
