import java.util.*;

public class Graph {
    private Map<Integer, Vertex> vertices;
    private Map<Integer, List<Edge>> adjacencyList;

    public Graph() {
        vertices = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getId(), vertex);
        adjacencyList.put(vertex.getId(), new ArrayList<>());
    }

    public void addEdge(int from, int to) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            throw new IllegalArgumentException("Both vertices must exist in the graph.");
        }
        Edge edge = new Edge(from, to);
        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from)); // For undirected graph
    }

    public void removeEdge(int from, int to) {
        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).removeIf(e -> e.getTo() == to);
        }
        if (adjacencyList.containsKey(to)) {
            adjacencyList.get(to).removeIf(e -> e.getTo() == from);
        }
    }

    public void removeVertex(int id) {
        if (!vertices.containsKey(id)) {
            return;
        }
        vertices.remove(id);
        adjacencyList.remove(id);
        for (List<Edge> edges : adjacencyList.values()) {
            edges.removeIf(e -> e.getTo() == id);
        }
    }

    public boolean hasEdge(int from, int to) {
        if (!adjacencyList.containsKey(from)) {
            return false;
        }
        return adjacencyList.get(from).stream().anyMatch(e -> e.getTo() == to);
    }

    public Set<Integer> getVertices() {
        return new HashSet<>(vertices.keySet());
    }

    public Vertex getVertex(int id) {
        return vertices.get(id);
    }

    public List<Integer> getNeighbors(int id) {
        if (!adjacencyList.containsKey(id)) {
            return Collections.emptyList();
        }
        return adjacencyList.get(id).stream().map(Edge::getTo).collect(java.util.stream.Collectors.toList());
    }

    public int getNumVertices() {
        return vertices.size();
    }
}