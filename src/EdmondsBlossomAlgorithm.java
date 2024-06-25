import java.util.*;

public class EdmondsBlossomAlgorithm {
    private Graph graph;
    private Map<Integer, Integer> matching;
    private Set<Integer> free;
    private Map<Integer, Integer> label;
    private Map<Integer, Integer> parent;
    private Queue<Integer> queue;
    private GraphView graphView;

    public EdmondsBlossomAlgorithm(Graph graph, GraphView graphView) {
        this.graph = graph;
        this.matching = new HashMap<>();
        this.free = new HashSet<>();
        this.label = new HashMap<>();
        this.parent = new HashMap<>();
        this.queue = new LinkedList<>();
        this.graphView = graphView;
    }

    public Map<Integer, Integer> findMaximumMatching() {
        try {
            initialize();
            while (!free.isEmpty()) {
                int root = free.iterator().next();
                if (bfs(root)) {
                    free.remove(root);
                } else {
                    free.remove(root);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in findMaximumMatching: " + e.getMessage());
            e.printStackTrace();
        }
        return matching;
    }

    private void initialize() {
        matching.clear();
        free.addAll(graph.getVertices());
        updateVisualization();
    }

    private boolean bfs(int root) {
        label.clear();
        parent.clear();
        queue.clear();

        for (int v : graph.getVertices()) {
            label.put(v, v == root ? 0 : -1);
        }

        queue.offer(root);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u : graph.getNeighbors(v)) {
                if (findOrGrowAugmentingPath(v, u)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findOrGrowAugmentingPath(int v, int u) {
        if (label.get(u) == null) {
            System.err.println("Error: Vertex " + u + " not found in label map");
            return false;
        }

        if (label.get(u) == -1) {
            if (!matching.containsKey(u)) {
                augmentPath(v, u);
                return true;
            } else {
                int w = matching.get(u);
                label.put(u, 1);
                label.put(w, 0);
                parent.put(w, v);
                parent.put(u, w);
                queue.offer(w);
            }
        } else if (label.get(u) == 0) {
            Integer parentV = parent.get(v);
            if (parentV == null || !parentV.equals(u)) {
                int lca = findLowestCommonAncestor(v, u);
                if (lca != -1) {
                    blossomShrink(v, u, lca);
                    blossomShrink(u, v, lca);
                }
            }
        }
        return false;
    }

    private void augmentPath(int v, int u) {
        List<Integer> path = new ArrayList<>();
        int current = v;
        while (current != -1) {
            path.add(current);
            current = parent.getOrDefault(current, -1);
        }
        Collections.reverse(path);
        path.add(u);

        graphView.setAugmentingPath(path);
        updateVisualization();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < path.size() - 1; i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);
            matching.put(x, y);
            matching.put(y, x);
            free.remove(x);
            free.remove(y);
        }

        graphView.setAugmentingPath(null);
        updateVisualization();
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
        List<Integer> blossom = new ArrayList<>();
        while (u != lca) {
            blossom.add(u);
            u = parent.getOrDefault(u, lca);
        }
        blossom.add(lca);
        Collections.reverse(blossom);
        while (v != lca) {
            blossom.add(v);
            v = parent.getOrDefault(v, lca);
        }

        graphView.highlightBlossom(blossom);
        updateVisualization();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int vertex : blossom) {
            if (vertex != lca) {
                label.put(vertex, 1);
                queue.offer(vertex);
            }
        }
        graphView.highlightBlossom(null);
    }

    private void updateVisualization() {
        graphView.setMatchingEdges(matching);
        graphView.repaint();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}