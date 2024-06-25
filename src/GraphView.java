import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GraphView extends JPanel {
    private Graph graph;
    private Map<Integer, Integer> matchingEdges;
    private List<Integer> augmentingPath;
    private Map<Integer, Color> vertexColors;

    public GraphView(Graph graph) {
        this.graph = graph;
        this.vertexColors = new HashMap<>();
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        if (this.matchingEdges != null) {
            this.matchingEdges.clear();
        }
        this.augmentingPath = null;
        this.vertexColors.clear();
        repaint();
    }

    public void setMatchingEdges(Map<Integer, Integer> matchingEdges) {
        this.matchingEdges = matchingEdges;
        repaint();
    }

    public void setAugmentingPath(List<Integer> augmentingPath) {
        this.augmentingPath = augmentingPath;
        repaint();
    }

    public void pickVertex(int vertexId, Color color) {
        vertexColors.put(vertexId, color);
        repaint();
    }

    public void clearPickedVertex(int vertexId) {
        vertexColors.remove(vertexId);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph(g);
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Integer vertexId : graph.getVertices()) {
            Vertex vertex = graph.getVertex(vertexId);
            Point p = vertex.getPosition();
            if (p.x == 0 && p.y == 0) continue;

            // Draw vertices
            Color vertexColor = vertexColors.getOrDefault(vertexId, Color.WHITE);
            g2.setColor(vertexColor);
            int nodeRadius = 15;
            g2.fillOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
            g2.drawString(String.valueOf(vertexId), p.x - nodeRadius / 2, p.y + nodeRadius / 2);

            // Draw edges
            for (Integer neighborId : graph.getNeighbors(vertexId)) {
                Vertex neighbor = graph.getVertex(neighborId);
                Point q = neighbor.getPosition();
                if (matchingEdges != null && matchingEdges.containsKey(vertexId) && matchingEdges.get(vertexId).equals(neighborId)) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(3));
                } else if (augmentingPath != null && isInAugmentingPath(vertexId, neighborId)) {
                    g2.setColor(Color.GREEN);
                    g2.setStroke(new BasicStroke(1));
                } else {
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawLine(p.x, p.y, q.x, q.y);
            }
        }
    }

    private boolean isInAugmentingPath(int u, int v) {
        if (augmentingPath == null) return false;
        for (int i = 0; i < augmentingPath.size() - 1; i++) {
            if ((augmentingPath.get(i) == u && augmentingPath.get(i + 1) == v) || (augmentingPath.get(i) == v && augmentingPath.get(i + 1) == u)) {
                return true;
            }
        }
        return false;
    }

    public int findVertexAt(Point point) {
        for (Integer vertexId : graph.getVertices()) {
            Vertex vertex = graph.getVertex(vertexId);
            Point pos = vertex.getPosition();
            if (pos.distance(point) < 15) {
                return vertexId;
            }
        }
        return -1;
    }

    public void pickEdge(int vertexId) {
        repaint();
    }

    public Integer getPickedEdgeStart() {
        return null;
    }
}