package cz.cvut.fel.esw.shortestpath.search.pareto;

import cz.cvut.fel.esw.shortestpath.util.Heap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicParetoBag<E> implements ParetoBag<E> {

    private final ParetoComparator<? super E> comparator;

    private final List<Heap.Entry<E>> bag = new LinkedList<>();

    public BasicParetoBag(ParetoComparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void addWithoutChecks(Heap.Entry<E> entry) {
        bag.add(entry);
    }

    @Override
    public void addWithDominatedRemoval(Heap.Entry<E> toBeAdded) {
        bag.removeIf(entry -> comparator.isDominated(entry.getValue(), toBeAdded.getValue()));
        bag.add(toBeAdded);
    }

    @Override
    public boolean addToBagAndHeapIfNotDominated(E element, Heap<E> heap) {
        boolean elementDominated = false;
        for (Iterator<Heap.Entry<E>> iterator = bag.iterator(); iterator.hasNext(); ) {
            Heap.Entry<E> entry = iterator.next();

            //once element is dominated it cannot dominate anything
            if (!elementDominated) {
                if (comparator.isDominated(element, entry.getValue())) {
                    return false;
                }
            }

            if (comparator.isDominated(entry.getValue(), element)) {
                elementDominated = true;
                iterator.remove();
                heap.delete(entry);
            }
        }

        Heap.Entry<E> newEntry = heap.insert(element);
        addWithoutChecks(newEntry);
        return true;
    }

    @Override
    public void remove(Heap.Entry<E> entry) {
        bag.remove(entry);
    }

    @Override
    public boolean dominates(E element) {
        return bag.stream().anyMatch(entry -> comparator.isDominated(element, entry.getValue()));
    }

    @Override
    public int size() {
        return bag.size();
    }

    @Override
    public String toString() {
        return "BasicParetoBag{" + "size=" + bag.size() + '}';
    }
}
