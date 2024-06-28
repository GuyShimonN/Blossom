import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class GraphView extends JPanel {
    private Graph graph;
    private Map<Integer, Integer> matchingEdges;
    private List<Integer> augmentingPath;
    private List<Integer> blossomVertices;
    private Map<Integer, Color> vertexColors;
    private Map<Integer, Integer> forest;
    private int root;
    private int currentVertex;
    private Map<Integer, List<Integer>> shrunkBlossoms;
    private Map<Integer, Point> blossomCenters;

    public GraphView(Graph graph) {
        this.graph = graph;
        this.vertexColors = new HashMap<>();
        this.forest = new HashMap<>();
        this.matchingEdges = new HashMap<>();
        this.shrunkBlossoms = new HashMap<>();
        this.blossomCenters = new HashMap<>();
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.matchingEdges.clear();
        this.augmentingPath = null;
        this.blossomVertices = null;
        this.vertexColors.clear();
        this.forest.clear();
        this.shrunkBlossoms.clear();
        this.blossomCenters.clear();
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

    public void setForest(Map<Integer, Integer> forest) {
        this.forest = forest;
        repaint();
    }

    public void setRoot(int root) {
        this.root = root;
        repaint();
    }

    public void setCurrentVertex(int currentVertex) {
        this.currentVertex = currentVertex;
        repaint();
    }

    public void highlightBlossom(List<Integer> blossomVertices) {
        this.blossomVertices = blossomVertices;
        repaint();
    }

    public void shrinkBlossom(int lcaVertex, List<Integer> blossomVertices) {
        shrunkBlossoms.put(lcaVertex, blossomVertices);
        calculateBlossomCenter(lcaVertex, blossomVertices);
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

    private void calculateBlossomCenter(int lcaVertex, List<Integer> blossomVertices) {
        int sumX = 0, sumY = 0;
        for (int v : blossomVertices) {
            Point p = graph.getVertex(v).getPosition();
            sumX += p.x;
            sumY += p.y;
        }
        int centerX = sumX / blossomVertices.size();
        int centerY = sumY / blossomVertices.size();
        blossomCenters.put(lcaVertex, new Point(centerX, centerY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph(g);
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Draw forest edges
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
        for (Map.Entry<Integer, Integer> entry : forest.entrySet()) {
            Vertex child = graph.getVertex(entry.getKey());
            Vertex parent = graph.getVertex(entry.getValue());
            g2.drawLine(child.getPosition().x, child.getPosition().y, parent.getPosition().x, parent.getPosition().y);
        }

        // Draw regular edges
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        for (Integer vertexId : graph.getVertices()) {
            Vertex vertex = graph.getVertex(vertexId);
            Point p = vertex.getPosition();
            for (Integer neighborId : graph.getNeighbors(vertexId)) {
                Vertex neighbor = graph.getVertex(neighborId);
                Point q = neighbor.getPosition();
                g2.drawLine(p.x, p.y, q.x, q.y);
            }
        }

        // Draw matching edges
        if (matchingEdges != null) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            for (Map.Entry<Integer, Integer> entry : matchingEdges.entrySet()) {
                if (entry.getKey() < entry.getValue()) {
                    Vertex v1 = graph.getVertex(entry.getKey());
                    Vertex v2 = graph.getVertex(entry.getValue());
                    g2.drawLine(v1.getPosition().x, v1.getPosition().y, v2.getPosition().x, v2.getPosition().y);
                }
            }
        }

        // Draw augmenting path
        if (augmentingPath != null) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < augmentingPath.size() - 1; i++) {
                Vertex v1 = graph.getVertex(augmentingPath.get(i));
                Vertex v2 = graph.getVertex(augmentingPath.get(i + 1));
                g2.drawLine(v1.getPosition().x, v1.getPosition().y, v2.getPosition().x, v2.getPosition().y);
            }
        }

        // Draw shrunk blossoms
        g2.setColor(new Color(255, 200, 200, 100));
        for (Map.Entry<Integer, List<Integer>> entry : shrunkBlossoms.entrySet()) {
            int lcaVertex = entry.getKey();
            Point center = blossomCenters.get(lcaVertex);
            int blossomRadius = 30;
            g2.fillOval(center.x - blossomRadius, center.y - blossomRadius, 2 * blossomRadius, 2 * blossomRadius);
            g2.setColor(Color.BLACK);
            g2.drawString("B" + lcaVertex, center.x - 5, center.y + 5);
            g2.setColor(new Color(255, 200, 200, 100));
        }

        // Draw vertices
        for (Integer vertexId : graph.getVertices()) {
            Vertex vertex = graph.getVertex(vertexId);
            Point p = vertex.getPosition();

            if (!isVertexInShrunkBlossom(vertexId)) {
                if (vertexColors.containsKey(vertexId)) {
                    g2.setColor(vertexColors.get(vertexId));
                } else if (vertexId == root) {
                    g2.setColor(Color.GREEN);
                } else if (vertexId == currentVertex) {
                    g2.setColor(Color.ORANGE);
                } else if (forest.containsKey(vertexId)) {
                    g2.setColor(Color.CYAN);
                } else {
                    g2.setColor(Color.WHITE);
                }

                int nodeRadius = 15;
                g2.fillOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - nodeRadius, p.y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
                g2.drawString(String.valueOf(vertexId), p.x - 5, p.y + 5);
            }
        }

        // Draw blossom (before shrinking)
        if (blossomVertices != null) {
            g2.setColor(new Color(255, 200, 200, 100));
            for (Integer vertexId : blossomVertices) {
                Vertex vertex = graph.getVertex(vertexId);
                Point p = vertex.getPosition();
                int blossomRadius = 25;
                g2.fillOval(p.x - blossomRadius, p.y - blossomRadius, 2 * blossomRadius, 2 * blossomRadius);
            }
        }
    }

    private boolean isVertexInShrunkBlossom(int vertexId) {
        for (List<Integer> blossom : shrunkBlossoms.values()) {
            if (blossom.contains(vertexId)) {
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
        // This method is left empty as it's not used in the current implementation
        repaint();
    }

    public Integer getPickedEdgeStart() {
        // This method is left empty as it's not used in the current implementation
        return null;
    }
}