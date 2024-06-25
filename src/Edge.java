import java.util.Objects;

/**
 * Represents an edge in a graph with a source and destination.
 */
public class Edge {
    private final int from;
    private final int to;

    /**
     * Constructs an edge with a specified source and destination.
     * @param from the source vertex identifier
     * @param to the destination vertex identifier
     */
    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return from == edge.from && to == edge.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + '}';
    }
}
