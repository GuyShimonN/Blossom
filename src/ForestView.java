//import javax.swing.*;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//class ForestView extends JPanel {
//    private Set<Integer> forest;
//    private Map<Integer, Integer> matching;
//    private Map<Integer, Integer> labels;
//    private Map<Integer, Integer> parents;
//    private List<Integer> blossom;
//
//    public ForestView() {
//        this.forest = new HashSet<>();
//        this.matching = new HashMap<>();
//        this.labels = new HashMap<>();
//        this.parents = new HashMap<>();
//        this.blossom = new ArrayList<>();
//    }
//
//    public void setForest(Set<Integer> forest) {
//        this.forest = forest;
//    }
//
//    public void setMatching(Map<Integer, Integer> matching) {
//        this.matching = matching;
//    }
//
//    public void setLabels(Map<Integer, Integer> labels) {
//        this.labels = labels;
//    }
//
//    public void setParents(Map<Integer, Integer> parents) {
//        this.parents = parents;
//    }
//
//    public void setBlossom(List<Integer> blossom) {
//        this.blossom = blossom;
//    }
//
//    public void clear() {
//        forest.clear();
//        matching.clear();
//        labels.clear();
//        parents.clear();
//        blossom.clear();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//
//        int yOffset = 30;
//        int xOffset = 50;
//        int vertexRadius = 15;
//
//        // Draw forest structure
//        for (int v : forest) {
//            drawVertex(g2d, v, xOffset, yOffset, vertexRadius);
//            yOffset += 60;
//
//            // Draw children
//            int childXOffset = xOffset + 100;
//            for (int u : parents.keySet()) {
//                if (parents.get(u) == v) {
//                    drawVertex(g2d, u, childXOffset, yOffset, vertexRadius);
//                    g2d.drawLine(xOffset + vertexRadius, yOffset - 30 + vertexRadius,
//                            childXOffset + vertexRadius, yOffset + vertexRadius);
//                    childXOffset += 100;
//                }
//            }
//        }
//
//        // Draw matching information
//        g2d.setColor(Color.BLACK);
//        g2d.drawString("Matching: " + matching.toString(), 10, getHeight() - 40);
//
//// Draw blossom information
//        if (!blossom.isEmpty()) {
//            g2d.setColor(Color.BLUE);
//            g2d.drawString("Blossom: " + blossom.toString(), 10, getHeight() - 20);
//        }
//    }
//
//    private void drawVertex(Graphics2D g2d, int v, int x, int y, int radius) {
//        g2d.setColor(Color.WHITE);
//        g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
//        g2d.setColor(Color.BLACK);
//        g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
//        g2d.drawString(String.valueOf(v), x - 5, y + 5);
//
//        // Draw label
//        if (labels.containsKey(v)) {
//            g2d.setColor(Color.RED);
//            g2d.drawString("L:" + labels.get(v), x - radius, y - radius - 5);
//        }
//    }
//}