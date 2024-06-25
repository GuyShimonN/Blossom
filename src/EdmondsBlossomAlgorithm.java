import java.util.*;

public class EdmondsBlossomAlgorithm {
    private Graph graph;
    private Map<Integer, Integer> mate;
    private Map<Integer, Integer> label;
    private Map<Integer, Integer> parent;
    private Queue<Integer> queue;
    private Set<Integer> inQueue;
    private Set<Integer> inBlossom;
    private int root;

    public EdmondsBlossomAlgorithm(Graph graph) {
        this.graph = graph;
        this.mate = new HashMap<>();
        this.label = new HashMap<>();
        this.parent = new HashMap<>();
        this.queue = new LinkedList<>();
        this.inQueue = new HashSet<>();
        this.inBlossom = new HashSet<>();
    }

    public Map<Integer, Integer> findMaximumMatching() {
        for (int v : graph.getVertices()) {
            if (!mate.containsKey(v)) {
                root = v;
                blossom();
            }
        }
        return mate;
    }

    private void blossom() {
        label.clear();
        parent.clear();
        queue.clear();
        inQueue.clear();
        inBlossom.clear();

        for (int v : graph.getVertices()) {
            label.put(v, 0);
        }

        label.put(root, 1);
        queue.offer(root);
        inQueue.add(root);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            inQueue.remove(v);

            for (int u : graph.getNeighbors(v)) {
                if (label.get(u) == 0) {
                    if (!mate.containsKey(u)) {
                        augmentPath(v, u);
                        return;
                    } else {
                        parent.put(mate.get(u), v);
                        label.put(mate.get(u), 2);
                        label.put(u, 1);
                        queue.offer(mate.get(u));
                        inQueue.add(mate.get(u));
                    }
                } else if (label.get(u) == 1 && findBase(u) != findBase(v)) {
                    int base = findLowestCommonAncestor(u, v);
                    Set<Integer> blossom = new HashSet<>();
                    markBlossom(v, base, u, blossom);
                    markBlossom(u, base, v, blossom);
                    for (int b : blossom) {
                        if (!inBlossom.contains(b)) {
                            inBlossom.add(b);
                            if (!inQueue.contains(b)) {
                                queue.offer(b);
                                inQueue.add(b);
                            }
                        }
                    }
                }
            }
        }
    }

    private void augmentPath(int v, int u) {
        while (v != 0) {
            Integer pv = parent.get(v);
            if (pv == null) {
                break; // Exit the loop if there's no parent
            }
            Integer nv = mate.get(pv);
            mate.put(v, pv);
            mate.put(pv, v);
            v = nv;
        }
        mate.put(u, root);
        mate.put(root, u);
    }

    private int findBase(int v) {
        if (inBlossom.contains(v)) {
            return findBase(parent.getOrDefault(v, v));
        }
        return v;
    }

    private int findLowestCommonAncestor(int u, int v) {
        Set<Integer> ancestors = new HashSet<>();
        while (u != 0) {
            u = findBase(u);
            ancestors.add(u);
            if (!parent.containsKey(u)) break;
            u = parent.get(u);
        }
        while (v != 0) {
            v = findBase(v);
            if (ancestors.contains(v)) return v;
            if (!parent.containsKey(v)) break;
            v = parent.get(v);
        }
        return 0;
    }

    private void markBlossom(int v, int base, int child, Set<Integer> blossom) {
        while (findBase(v) != base) {
            blossom.add(v);
            blossom.add(mate.getOrDefault(v, v));
            parent.put(v, child);
            child = mate.getOrDefault(v, v);
            v = parent.getOrDefault(mate.getOrDefault(v, v), v);
        }
    }
}