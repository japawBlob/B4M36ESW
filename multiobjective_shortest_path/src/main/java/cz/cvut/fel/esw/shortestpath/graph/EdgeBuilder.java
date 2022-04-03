package cz.cvut.fel.esw.shortestpath.graph;

import java.util.Objects;

/**
 * @author Marek Cuchý (CVUT)
 */
public class EdgeBuilder {

	private final int fromId;
	private final int toId;

	private int distance;
	private int time;

	public EdgeBuilder(int fromId, int toId) {
		this.fromId = fromId;
		this.toId = toId;
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

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Edge build() {
		return new Edge(fromId, toId, distance, time);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EdgeBuilder that = (EdgeBuilder) o;
		return fromId == that.fromId && toId == that.toId && distance == that.distance && time == that.time;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fromId, toId, distance, time);
	}
}
