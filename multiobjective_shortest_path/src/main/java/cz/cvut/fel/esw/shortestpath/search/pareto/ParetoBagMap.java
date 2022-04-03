package cz.cvut.fel.esw.shortestpath.search.pareto;

import cz.cvut.fel.esw.shortestpath.search.Locatable;
import cz.cvut.fel.esw.shortestpath.util.Heap;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Map of pareto bags mapped by node ID
 *
 * @param <T>
 */
public class ParetoBagMap<T extends Locatable> implements ParetoBag<T> {

    private final ParetoBag<T>[] map;

    public ParetoBagMap(ParetoBag<T>[] map) {
        this.map = map;
    }

    public static <T extends Locatable> ParetoBagMap<T> create(int maxNodeId, Function<ParetoComparator<? super T>, ? extends ParetoBag<T>> factory, ParetoComparator<? super T> comparator) {
        @SuppressWarnings("unchecked") ParetoBag<T>[] maps = new ParetoBag[maxNodeId];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = factory.apply(comparator);
        }
        return new ParetoBagMap<>(maps);
    }

    public static <T extends Locatable> ParetoBagMap<T> create(int maxNodeId, Supplier<? extends ParetoBag<T>> factory) {
        @SuppressWarnings("unchecked") ParetoBag<T>[] maps = new ParetoBag[maxNodeId];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = factory.get();
        }
        return new ParetoBagMap<>(maps);
    }

    @Override
    public void remove(Heap.Entry<T> entry) {
        get(entry).remove(entry);
    }

    @Override
    public void addWithoutChecks(Heap.Entry<T> entry) {
        get(entry).addWithoutChecks(entry);
    }

    @Override
    public void addWithDominatedRemoval(Heap.Entry<T> entry) {
        get(entry).addWithDominatedRemoval(entry);
    }

    @Override
    public boolean addToBagAndHeapIfNotDominated(T element, Heap<T> heap) {
        return get(element).addToBagAndHeapIfNotDominated(element, heap);
    }

    @Override
    public boolean dominates(T element) {
        return get(element).dominates(element);
    }

    private ParetoBag<T> get(Heap.Entry<? extends T> entry) {
        return get(entry.getValue());
    }

    private ParetoBag<T> get(T value) {
        return map[value.getNodeId()];
    }

    /**
     * {@inheritDoc}
     * <p>
     * Be aware that the calculation of size does not have to be fast.
     *
     * @return {@inheritDoc}
     */
    @Override
    public int size() {
        return Arrays.stream(map).mapToInt(ParetoBag::size).sum();
    }

    @Override
    public String toString() {
        return "ParetoBagMap{" + "size=" + size() + '}';
    }
}
