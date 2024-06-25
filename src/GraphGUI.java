import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GraphGUI extends JFrame {
    private Graph graph;
    private GraphView graphView;
    private JButton addVertexButton, addEdgeButton, removeVertexButton, removeEdgeButton;
    private JButton setGraphButton, findMatchingButton, clearBoardButton;
    private JLabel statusLabel;
    private boolean addingEdge = false;
    private boolean removingVertex = false;
    private boolean removingEdge = false;
    private Integer edgeStart = null;
    private int vertexCount = 0;

    public GraphGUI() {
        graph = new Graph();
        setTitle("Interactive Graph Matching - Edmonds' Blossom Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        graphView = new GraphView(graph);
        add(graphView, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready to add vertices or edges.");
        add(statusLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        addVertexButton = new JButton("Add Vertex");
        addEdgeButton = new JButton("Add Edge");
        removeVertexButton = new JButton("Remove Vertex");
        removeEdgeButton = new JButton("Remove Edge");
        setGraphButton = new JButton("Set Graph");
        findMatchingButton = new JButton("Find Max Matching");
        clearBoardButton = new JButton("Clear Board");

        buttonPanel.add(addVertexButton);
        buttonPanel.add(addEdgeButton);
        buttonPanel.add(removeVertexButton);
        buttonPanel.add(removeEdgeButton);
        buttonPanel.add(setGraphButton);
        buttonPanel.add(findMatchingButton);
        buttonPanel.add(clearBoardButton);
        add(buttonPanel, BorderLayout.NORTH);

        addVertexButton.addActionListener(e -> {
            addingEdge = false;
            removingVertex = false;
            removingEdge = false;
            updateStatusLabel("Click on the panel to add a vertex.");
        });
        addEdgeButton.addActionListener(e -> {
            addingEdge = true;
            removingVertex = false;
            removingEdge = false;
            updateStatusLabel("Click on two vertices to add an edge.");
        });
        removeVertexButton.addActionListener(e -> {
            addingEdge = false;
            removingVertex = true;
            removingEdge = false;
            updateStatusLabel("Click on a vertex to remove it.");
        });
        removeEdgeButton.addActionListener(e -> {
            addingEdge = false;
            removingVertex = false;
            removingEdge = true;
            updateStatusLabel("Click on two vertices to remove the edge between them.");
        });
        setGraphButton.addActionListener(e -> setGraph());
        findMatchingButton.addActionListener(e -> findMatching());
        clearBoardButton.addActionListener(e -> clearBoard());

        findMatchingButton.setEnabled(false);

        graphView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (addingEdge) {
                    addEdge(e.getPoint());
                } else if (removingVertex) {
                    removeVertex(e.getPoint());
                } else if (removingEdge) {
                    removeEdge(e.getPoint());
                } else {
                    addVertex(e.getPoint());
                }
            }
        });
    }

    private void addVertex(Point point) {
        vertexCount++;
        Vertex newVertex = new Vertex(vertexCount, point);
        graph.addVertex(newVertex);
        updateStatusLabel("Vertex " + vertexCount + " added at " + point);
        graphView.repaint();
    }

    private void addEdge(Point point) {
        int vertex = graphView.findVertexAt(point);
        if (vertex == -1) {
            updateStatusLabel("No vertex at this position. Try again.");
            return;
        }
        if (edgeStart == null) {
            edgeStart = vertex;
            graphView.pickVertex(vertex, Color.GRAY);
            updateStatusLabel("Start vertex " + vertex + " selected. Select an end vertex.");
        } else {
            if (vertex != edgeStart && !graph.hasEdge(edgeStart, vertex)) {
                graph.addEdge(edgeStart, vertex);
                graphView.clearPickedVertex(edgeStart);
                updateStatusLabel("Edge added between " + edgeStart + " and " + vertex);
                edgeStart = null;
                graphView.repaint();
            } else {
                updateStatusLabel("Invalid operation or edge already exists.");
                graphView.clearPickedVertex(edgeStart);
                edgeStart = null;
            }
        }
    }

    private void removeVertex(Point point) {
        int vertex = graphView.findVertexAt(point);
        if (vertex != -1) {
            graph.removeVertex(vertex);
            updateStatusLabel("Vertex " + vertex + " removed.");
            graphView.repaint();
        } else {
            updateStatusLabel("No vertex at this position to remove.");
        }
    }

    private void removeEdge(Point point) {
        int vertex = graphView.findVertexAt(point);
        if (vertex == -1) {
            updateStatusLabel("No vertex at this position. Try again.");
            return;
        }
        if (edgeStart == null) {
            edgeStart = vertex;
            graphView.pickVertex(vertex, Color.GRAY);
            updateStatusLabel("Start vertex " + vertex + " selected. Select an end vertex.");
        } else {
            if (vertex != edgeStart && graph.hasEdge(edgeStart, vertex)) {
                graph.removeEdge(edgeStart, vertex);
                graphView.clearPickedVertex(edgeStart);
                updateStatusLabel("Edge removed between " + edgeStart + " and " + vertex);
                edgeStart = null;
                graphView.repaint();
            } else {
                updateStatusLabel("Invalid operation or edge does not exist.");
                graphView.clearPickedVertex(edgeStart);
                edgeStart = null;
            }
        }
    }

    private void setGraph() {
        printAdjacencyMatrix();
        disableTopButtons();
        findMatchingButton.setEnabled(true);
        updateStatusLabel("Graph set. Ready to find max matching.");
    }

    private void findMatching() {
        new Thread(() -> {
            System.out.println("Finding maximum matching...");
            EdmondsBlossomAlgorithm eba = new EdmondsBlossomAlgorithm(graph);
            System.out.println("v ");
            Map<Integer, Integer> matching = eba.findMaximumMatching();
            int maxMatching = matching.size();
            for (Map.Entry<Integer, Integer> entry : matching.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            updateStatusLabel("Maximum Matching: " + maxMatching);

            StringBuilder resultMessage = new StringBuilder("Matching Pairs:\n");
            Map<Integer, Integer> matchingEdges = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : matching.entrySet()) {
                resultMessage.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
                matchingEdges.put(entry.getKey(), entry.getValue());
                matchingEdges.put(entry.getValue(), entry.getKey());
                graphView.setMatchingEdges(matchingEdges);
                try {
                    Thread.sleep(1000); // Delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            resultMessage.append("\nMaximum Matching: ").append(maxMatching);
            JOptionPane.showMessageDialog(this, resultMessage.toString(), "Matching Result", JOptionPane.INFORMATION_MESSAGE);
        }).start();
    }

    private void clearBoard() {
        graph = new Graph();
        graphView.setGraph(graph);
        vertexCount = 0;
        edgeStart = null;
        enableTopButtons();
        findMatchingButton.setEnabled(false);
        updateStatusLabel("Board cleared.");
        graphView.repaint();
    }

    private void printAdjacencyMatrix() {
        Set<Integer> vertexIds = graph.getVertices();
        List<Integer> vertexList = new ArrayList<>(vertexIds);
        int size = vertexList.size();
        int[][] adjacencyMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (graph.hasEdge(vertexList.get(i), vertexList.get(j))) {
                    adjacencyMatrix[i][j] = 1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }

        System.out.println("Adjacency Matrix:");
        for (int[] row : adjacencyMatrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void disableTopButtons() {
        addVertexButton.setEnabled(false);
        addEdgeButton.setEnabled(false);
        removeVertexButton.setEnabled(false);
        removeEdgeButton.setEnabled(false);
    }

    private void enableTopButtons() {
        addVertexButton.setEnabled(true);
        addEdgeButton.setEnabled(true);
        removeVertexButton.setEnabled(true);
        removeEdgeButton.setEnabled(true);
    }

    public void updateStatusLabel(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphGUI::new);
    }
}