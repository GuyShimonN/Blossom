import java.util.*;

public class EdmondsBlossomAlgorithm {
    private Graph graph;
    private Map<Integer, Integer> matching;
    private Set<Integer> free;
    private Map<Integer, Integer> label;
    private Map<Integer, Integer> parent;
    private Queue<Integer> queue;
    private GraphView graphView;
    private Map<Integer, Integer> base;
    private int maxIterations = 1000;
    private int maxPathLength = 100;
    private Set<Integer> currentBlossom;

    public EdmondsBlossomAlgorithm(Graph graph, GraphView graphView) {
        this.graph = graph;
        this.matching = new HashMap<>();
        this.free = new HashSet<>();
        this.label = new HashMap<>();
        this.parent = new HashMap<>();
        this.queue = new LinkedList<>();
        this.graphView = graphView;
        this.base = new HashMap<>();
        this.currentBlossom = new HashSet<>();
    }

    public Map<Integer, Integer> findMaximumMatching() {
        initialize();
        int iterations = 0;
        while (!free.isEmpty() && iterations < maxIterations) {
            int v = free.iterator().next();
            if (augment(v)) {
                iterations = 0;
            } else {
                free.remove(v);
                iterations++;
            }
            System.out.println("Current matching: " + matching);
            graphView.setMatchingEdges(matching);
            graphView.setForest(parent);
            graphView.repaint();
            sleep(1000);
        }
        return matching;
    }

    private void initialize() {
        matching.clear();
        free.addAll(graph.getVertices());
        graphView.setMatchingEdges(matching);
        graphView.setForest(parent);
        graphView.repaint();
    }

    private boolean augment(int root) {
        System.out.println("Starting augmentation from root: " + root);
        label.clear();
        parent.clear();
        base.clear();
        queue.clear();

        for (int v : graph.getVertices()) {
            base.put(v, v);
        }

        label.put(root, 0);
        queue.offer(root);
        graphView.setRoot(root);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            System.out.println("Processing vertex: " + v);
            graphView.setCurrentVertex(v);
            graphView.setForest(parent);
            graphView.repaint();
            sleep(500);

            for (int u : graph.getNeighbors(v)) {
                if (base.get(u).equals(base.get(v))) continue;
                if (!label.containsKey(base.get(u))) {
                    if (!matching.containsKey(u)) {
                        System.out.println("Augmenting path found: " + v + " - " + u);
                        graphView.setAugmentingPath(constructPath(v, u));
                        graphView.repaint();
                        sleep(1000);
                        if (augmentPath(v, u)) {
                            return true;
                        }
                    }
                    label.put(u, 1);
                    parent.put(u, v);
                    if (matching.containsKey(u)) {
                        label.put(matching.get(u), 0);
                        parent.put(matching.get(u), u);
                        queue.offer(matching.get(u));
                    }
                    graphView.setForest(parent);
                    graphView.repaint();
                    sleep(500);
                } else if (label.get(base.get(u)) == 0) {
                    int lca = findLowestCommonAncestor(v, u);
                    if (lca != -1) {
                        blossomShrink(v, u, lca);
                    }
                }
            }
        }
        return false;
    }

    private List<Integer> constructPath(int v, int u) {
        List<Integer> path = new ArrayList<>();
        int current = v;
        while (current != -1) {
            path.add(current);
            current = parent.getOrDefault(current, -1);
        }
        Collections.reverse(path);
        path.add(u);
        return path;
    }

    private boolean augmentPath(int v, int u) {
        List<Integer> path = constructPath(v, u);
        System.out.println("Augmenting path: " + path);

        for (int i = 0; i < path.size() - 1; i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);

            if (matching.containsKey(x)) {
                free.add(matching.get(x));
                matching.remove(matching.get(x));
            }
            if (matching.containsKey(y)) {
                free.add(matching.get(y));
                matching.remove(matching.get(y));
            }

            matching.put(x, y);
            matching.put(y, x);
            free.remove(x);
            free.remove(y);
            System.out.println("  Added to matching: " + x + " - " + y);
        }

        graphView.setAugmentingPath(null);
        return true;
    }

    private int findLowestCommonAncestor(int u, int v) {
        Set<Integer> ancestors = new HashSet<>();
        while (u != -1) {
            ancestors.add(base.get(u));
            u = parent.getOrDefault(u, -1);
        }
        while (v != -1) {
            if (ancestors.contains(base.get(v))) return base.get(v);
            v = parent.getOrDefault(v, -1);        }
        return -1;
    }

    private void blossomShrink(int u, int v, int lca) {
        System.out.println("Shrinking blossom with LCA: " + lca);
        currentBlossom.clear();
        List<Integer> path1 = getPath(u, lca);
        List<Integer> path2 = getPath(v, lca);
        currentBlossom.addAll(path1);
        currentBlossom.addAll(path2);

        graphView.highlightBlossom(new ArrayList<>(currentBlossom));
        graphView.repaint();
        sleep(1000);

        while (base.get(u) != lca) {
            int blosPair = matching.get(u);
            base.put(u, lca);
            base.put(blosPair, lca);
            u = parent.get(blosPair);
            if (label.get(base.get(u)) == 1) {
                queue.offer(u);
            }
        }
        while (base.get(v) != lca) {
            int blosPair = matching.get(v);
            base.put(v, lca);
            base.put(blosPair, lca);
            v = parent.get(blosPair);
            if (label.get(base.get(v)) == 1) {
                queue.offer(v);
            }
        }

        graphView.shrinkBlossom(lca, new ArrayList<>(currentBlossom));
        graphView.repaint();
        sleep(1000);
    }

    private List<Integer> getPath(int start, int end) {
        List<Integer> path = new ArrayList<>();
        int current = start;
        while (current != end) {
            path.add(current);
            current = parent.get(current);
        }
        path.add(end);
        return path;
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}