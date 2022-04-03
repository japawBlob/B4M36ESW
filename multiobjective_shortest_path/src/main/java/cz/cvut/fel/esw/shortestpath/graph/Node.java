package cz.cvut.fel.esw.shortestpath.graph;

/**
 * @author Marek Cuchý (CVUT)
 */
public class Node {
    private final int id;
    private final int latE6;
    private final int lonE6;

    public Node(int id, int latE6, int lonE6) {
        this.id = id;
        this.latE6 = latE6;
        this.lonE6 = lonE6;
    }

    public int getId() {
        return id;
    }

    public int getLatE6() {
        return latE6;
    }

    public int getLonE6() {
        return lonE6;
    }

    public double getLat() {
        return latE6 / 1.0E6;
    }

    public double getLon() {
        return lonE6 / 1.0E6;
    }
}
