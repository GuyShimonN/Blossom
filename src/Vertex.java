import java.awt.*;
import java.util.Objects;

/**
 * Represents a vertex in a graph with a unique identifier and a position.
 */
class Vertex {
    private final int id;
    private int x;  // X coordinate of the vertex position
    private int y;  // Y coordinate of the vertex position
    private boolean marked;  // Tracks if the vertex is part of an augmenting path

    /**
     * Constructs a vertex with an identifier and position coordinates.
     * @param id the unique identifier of the vertex
     * @param x the x-coordinate of the vertex position
     * @param y the y-coordinate of the vertex position
     */
    public Vertex(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.marked = false;
    }

    public Vertex(int id, Point point) {
        this(id, point.x, point.y);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex vertex = (Vertex) obj;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vertex{" + "id=" + id + ", x=" + x + ", y=" + y + '}';
    }
}