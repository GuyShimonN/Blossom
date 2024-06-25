import java.util.*;

public class EdmondsBlossomAlgorithm {
    private Graph graph;
    private Map<Integer, Integer> matching;
    private Map<Integer, Integer> label;
    private Map<Integer, Integer> parent;
    private Queue<Integer> queue;
    private int root;
    private Set<Integer> visited;
    private static final int MAX_ITERATIONS = 1000;

    public EdmondsBlossomAlgorithm(Graph graph) {
        this.graph = graph;
        this.matching = new HashMap<>();
        this.label = new HashMap<>();
        this.parent = new HashMap<>();
        this.queue = new LinkedList<>();
        this.visited = new HashSet<>();
    }

    public Map<Integer, Integer> findMaximumMatching() {
        matching.clear();
        boolean improvementMade;
        do {
            improvementMade = false;
            for (int v : graph.getVertices()) {
                if (!matching.containsKey(v)) {
                    System.out.println("Starting new alternating tree from vertex " + v);
                    if (growAlternatingTree(v)) {
                        improvementMade = true;
                        break;
                    }
                }
            }
        } while (improvementMade);

        // Remove duplicate entries
        Map<Integer, Integer> cleanedMatching = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : matching.entrySet()) {
            if (!cleanedMatching.containsKey(entry.getValue())) {
                cleanedMatching.put(entry.getKey(), entry.getValue());
            }
        }

        System.out.println("Final matching: " + cleanedMatching);
        return cleanedMatching;
    }

    private boolean growAlternatingTree(int root) {
        this.root = root;
        label.clear();
        parent.clear();
        queue.clear();
        visited.clear();

        for (int v : graph.getVertices()) {
            label.put(v, 0);
        }

        label.put(root, 1);
        queue.offer(root);
        visited.add(root);

        int iterations = 0;
        while (!queue.isEmpty() && iterations < MAX_ITERATIONS) {
            iterations++;
            int v = queue.poll();
            System.out.println("Processing vertex " + v);

            for (int u : graph.getNeighbors(v)) {
                System.out.println("  Examining neighbor " + u);
                if (label.get(u) == 0) {
                    if (!matching.containsKey(u) && u != root) {
                        System.out.println("  Augmenting path found");
                        augmentPath(v, u);
                        return true;
                    } else if (matching.containsKey(u)) {
                        int w = matching.get(u);
                        parent.put(w, v);
                        label.put(u, 2);
                        label.put(w, 1);
                        if (!visited.contains(w)) {
                            queue.offer(w);
                            visited.add(w);
                            System.out.println("  Adding " + w + " to queue");
                        }
                    }
                } else if (label.get(u) == 1 && u != parent.getOrDefault(v, -1) && u != v) {
                    System.out.println("  Blossom found between " + u + " and " + v);
                    int lca = findLowestCommonAncestor(u, v);
                    if (lca != -1) {
                        blossomShrink(u, v, lca);
                        blossomShrink(v, u, lca);
                    }
                }
            }
        }
        if (iterations >= MAX_ITERATIONS) {
            System.out.println("Max iterations reached. Terminating.");
        }
        return false;
    }

    private void augmentPath(int v, int u) {
        System.out.println("Augmenting path:");
        List<Integer> path = new ArrayList<>();
        int current = v;
        while (current != root) {
            path.add(current);
            current = parent.get(current);
        }
        path.add(root);
        path.add(u);

        for (int i = 0; i < path.size() - 1; i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);
            matching.put(x, y);
            matching.put(y, x);
            System.out.println(x + " - " + y);
        }
    }

    private int findLowestCommonAncestor(int u, int v) {
        Set<Integer> ancestors = new HashSet<>();
        while (u != -1) {
            ancestors.add(u);
            u = parent.getOrDefault(u, -1);
        }
        while (v != -1) {
            if (ancestors.contains(v)) return v;
            v = parent.getOrDefault(v, -1);
        }
        return -1;
    }

    private void blossomShrink(int u, int v, int lca) {
        Set<Integer> blossomVertices = new HashSet<>();
        while (u != lca) {
            blossomVertices.add(u);
            u = parent.getOrDefault(u, -1);
        }
        while (v != lca) {
            blossomVertices.add(v);
            v = parent.getOrDefault(v, -1);
        }

        for (int vertex : blossomVertices) {
            label.put(vertex, 2);
            if (!visited.contains(vertex)) {
                queue.offer(vertex);
                visited.add(vertex);
            }
            parent.put(vertex, lca);
        }
    }
}
