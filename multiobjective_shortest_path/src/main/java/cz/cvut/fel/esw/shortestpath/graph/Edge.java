package cz.cvut.fel.esw.shortestpath.graph;

import java.util.Objects;

public class Edge {

    private final int fromId;
    private final int toId;

    private final int distance;
    private final int time;

    public Edge(int fromId, int toId, int distance, int time) {
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
        this.time = time;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public EdgeId getEdgeId() {
        return EdgeId.of(fromId, toId);
    }

    public static class EdgeId {
        private final int fromId;
        private final int toId;

        private EdgeId(int fromId, int toId) {
            this.fromId = fromId;
            this.toId = toId;
        }

        public static EdgeId of(int fromId, int toId) {
            return new EdgeId(fromId, toId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EdgeId edgeId = (EdgeId) o;
            return fromId == edgeId.fromId && toId == edgeId.toId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromId, toId);
        }

        @Override
        public String toString() {
            return "EdgeId{" + "fromId=" + fromId + ", toId=" + toId + '}';
        }
    }
}
