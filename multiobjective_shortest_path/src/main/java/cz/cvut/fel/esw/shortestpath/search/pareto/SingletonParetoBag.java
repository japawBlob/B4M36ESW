package cz.cvut.fel.esw.shortestpath.search.pareto;

import cz.cvut.fel.esw.shortestpath.util.Heap;

public class SingletonParetoBag<E> implements ParetoBag<E> {

    private final ParetoComparator<? super E> comparator;

    private Heap.Entry<E> element;

    public SingletonParetoBag(ParetoComparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void addWithoutChecks(Heap.Entry<E> entry) {
        element = entry;
    }

    @Override
    public void addWithDominatedRemoval(Heap.Entry<E> toBeAdded) {
        assert element == null || comparator.isDominated(element.getValue(), toBeAdded.getValue());
        element = toBeAdded;
    }

    @Override
    public boolean addToBagAndHeapIfNotDominated(E toBeAdded, Heap<E> heap) {
        if (this.element == null) {
            this.element = heap.insert(toBeAdded);
        } else {
            if (comparator.isDominated(toBeAdded, this.element.getValue())) {
                return false;
            } else {
                heap.delete(this.element);
                this.element = heap.insert(toBeAdded);
            }
        }
        return false;
    }

    @Override
    public void remove(Heap.Entry<E> entry) {
        if (element != entry) throw new IllegalArgumentException();
        element = null;
    }

    @Override
    public boolean dominates(E element) {
        if (this.element == null) return false;
        return comparator.isDominated(element, this.element.getValue());
    }

    @Override
    public int size() {
        return element == null ? 0 : 1;
    }
}
