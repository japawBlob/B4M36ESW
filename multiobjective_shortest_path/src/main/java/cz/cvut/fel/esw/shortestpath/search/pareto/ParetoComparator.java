package cz.cvut.fel.esw.shortestpath.search.pareto;


/**
 * @param <T>
 */
@FunctionalInterface
public interface ParetoComparator<T> {

    /**
     * Returns true iff {@code o1} is dominated by {@code o2}.
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    boolean isDominated(T o1, T o2);

    @SuppressWarnings("unchecked")
    static <T extends ParetoComparable<? super T>> ParetoComparator<T> naturalOrder() {
        return (ParetoComparator<T>) NaturalParetoComparator.INSTANCE;
    }

    enum NaturalParetoComparator implements ParetoComparator<ParetoComparable<Object>> {
        INSTANCE;

        @Override
        public boolean isDominated(ParetoComparable<Object> c1, ParetoComparable<Object> c2) {
            return c1.isDominatedBy(c2);
        }
    }
}
