package cz.cvut.fel.esw.shortestpath.search.pareto;

import cz.cvut.fel.esw.shortestpath.util.Heap;


/**
 * Data structure storing non-dominated set of elements. It does not store the element itself but the heap entries that
 * holds the element. Some of the methods adds or removes elements to/from the heap.
 *
 * @param <E>
 */
public interface ParetoBag<E> {

    /**
     * Adds {@code entry} into the set without any dominance checks. This method serves only as a speed-up. Use
     *
     * @param entry entry to be added
     */
    void addWithoutChecks(Heap.Entry<E> entry);

    void addWithDominatedRemoval(Heap.Entry<E> entry);

    /**
     * Adds {@code element} to the {@code heap} iff this bag does not contain any element that dominates given {@code
     * element}. The entry from the heap is stored in this bag. All entries in this bag that are dominated by the {@code
     * element} are removed from this bag and also from the {@code heap}.
     * <p>
     * Be aware that this method DOES manipulate with the heap.
     *
     * @param element element to be added to the {@code heap} and this bag
     * @param heap    heap to which the {@code element} is supposed to be added
     *
     * @return true iff the element is successfully added to the heap and the bag
     */
    boolean addToBagAndHeapIfNotDominated(E element, Heap<E> heap);

    /**
     * Removes {@code entry} from this bag.
     *
     * @param entry entry to be removed
     */
    void remove(Heap.Entry<E> entry);

    /**
     * Returns true iff this pareto bag contains an element that dominates the given {@code element}.
     *
     * @param element
     *
     * @return
     */
    boolean dominates(E element);


    /**
     * Returns number of elements in this bag
     *
     * @return number of elements in this bag
     */
    int size();

}
