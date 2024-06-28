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

    // Getter for the source vertex
    public int getFrom() {
        return from;
    }

    // Getter for the destination vertex
    public int getTo() {
        return to;
    }

    // Override equals method to compare edges
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return from == edge.from && to == edge.to;
    }

    // Override hashCode method for edge comparison
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    // Override toString method for edge representation
    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + '}';
    }
}
