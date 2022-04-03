package cz.cvut.fel.esw.shortestpath.search;

/**
 * Represents states during the search
 */
public class SearchState implements Locatable {

    private final int nodeId;
    private final int time;
    private final int distance;

    /**
     * Estimate of remaining time to reach the goal.
     */
    private final int timeHeuristic;
    /**
     * Estimate of remaining distance to reach the goal.
     */
    private final int distanceHeuristic;

    private final SearchState previous;

    SearchState(int nodeId, int time, int distance, int timeHeuristic, int distanceHeuristic, SearchState previous) {
        this.nodeId = nodeId;
        this.time = time;
        this.distance = distance;
        this.timeHeuristic = timeHeuristic;
        this.distanceHeuristic = distanceHeuristic;
        this.previous = previous;
    }

    public static int lexicographicalOrder(SearchState o1, SearchState o2) {
        int cmp = Integer.compare(o1.getEstimatedGoalTime(), o2.getEstimatedGoalTime());
        if (cmp != 0) {
            return cmp;
        } else {
            return Integer.compare(o1.getEstimatedGoalDistance(), o2.getEstimatedGoalDistance());
        }
    }

    public static boolean dominatedByTimeAndDistance(SearchState o1, SearchState o2) {
        return o1.getTime() >= o2.getTime() && o1.getDistance() >= o2.getDistance();
    }

    public static boolean dominatedByDistance(SearchState o1, SearchState o2) {
        return o1.getDistance() >= o2.getDistance();
    }

    public static boolean dominatedByDistanceEstimate(SearchState o1, SearchState o2) {
        return o1.getEstimatedGoalDistance() >= o2.getEstimatedGoalDistance();
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getTime() {
        return time;
    }

    public int getDistance() {
        return distance;
    }

    /**
     * Estimate of the time at the goal state via this state. It is the current {@code time} plus heuristic estimate of
     * time required to get from {@code nodeId} to {@code goal} = {@code time + h_time(nodeId, goal)}
     */
    public int getEstimatedGoalTime() {
        return time + timeHeuristic;
    }

    /**
     * Estimate of the distance at the goal state via this state. It is the current {@code distance} plus heuristic
     * estimate of distance required to get from {@code nodeId} to {@code goal} = {@code distance + h_distance(nodeId,
     * goal)}
     */
    public int getEstimatedGoalDistance() {
        return distance + distanceHeuristic;
    }

    public SearchState getPrevious() {
        return previous;
    }

    /**
     * Creates state WITHOUT heuristic estimates.
     */
    public static SearchState of(int nodeId, int time, int distance, SearchState previous) {
        return of(nodeId, time, distance, 0, 0, previous);
    }

    /**
     * Creates state WITH heuristic estimates.
     */
    public static SearchState of(int nodeId, int time, int distance, int timeHeuristic, int distanceHeuristic, SearchState previous) {
        return new SearchState(nodeId, time, distance, timeHeuristic, distanceHeuristic, previous);
    }

    @Override
    public String toString() {
        return "State{" + "nodeId=" + nodeId + ", time=" + time + ", distance=" + distance + ", estimatedGoalTime=" + getEstimatedGoalTime() + ", estimatedGoalDistance=" + getEstimatedGoalDistance() + ", pathLength=" + calculatePathLength() + '}';
    }

    private int calculatePathLength() {
        int count = 1;
        SearchState prev = previous;

        while (prev != null) {
            count++;
            prev = prev.previous;
        }
        return count;
    }
}
