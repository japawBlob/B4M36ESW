package cz.cvut.fel.esw.shortestpath.search.pareto;

/**
 * @param <T>
 *
 * @see ParetoComparator
 */
public interface ParetoComparable<T> {

    /**
     * Returns true iff this is dominated by {@code other}.
     *
     * @param other
     *
     * @return
     */
    boolean isDominatedBy(T other);
}
