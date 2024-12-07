package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static java.lang.Math.*;

/**
 * A visual representation of a graph of properties using JGraphT.
 * Each property is a vertex, and edges are based on adjacency conditions between properties.
 * <p>
 * The graph is visualized in a GUI with various interactive controls for zooming, panning,
 * and highlighting specific properties or owners.
 */
public class Visualizer extends JFrame {

    private Graph<Property, DefaultEdge> graph;
    private JFrame frame = new JFrame();
    private Integer highlightedOwner = null;
    private OwnerGraphStructure.PropertyPair highlightedExchange = null;
    private boolean showOnlyHighlighted = false;

    /**
     * Sets a highlighted property pair for visual emphasis.
     *
     * @param highlightedExchange The property pair to highlight.
     */
    public void setHighlightedExchange(OwnerGraphStructure.PropertyPair highlightedExchange) {
        this.highlightedExchange = highlightedExchange;
        //this.showOnlyHighlighted = true;
    }

    /**
     * Creates a new instance of {@code Visualizer} for a given graph.
     *
     * @param inputGraph The graph to visualize, with properties as vertices and relationships as edges.
     */
    public Visualizer(Graph<Property, DefaultEdge> inputGraph) {

        System.out.println("staring visualizer");
        // Create a JGraphT graph
        graph = inputGraph;
        frame.setSize(400, 400);
        frame.setVisible(true);
        DrawPane panel = new DrawPane(graph);
        final Point dragPoint = new Point();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint.setLocation(e.getPoint());
            }
        });
        panel.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                panel.scale += e.getWheelRotation() * 0.001; // Positive for down, negative for up
                panel.scale = clamp(panel.scale, 0.0, 3.0);
                frame.repaint();

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                panel.offset.setX(panel.offset.getX() + (e.getX() - dragPoint.x) / panel.scale);
                panel.offset.setY(panel.offset.getY() + (e.getY() - dragPoint.y) / panel.scale);
                dragPoint.setLocation(e.getPoint());

                System.out.println("drag point: " + panel.offset.getX() + ", " + panel.offset.getY());
                frame.repaint();
            }
        });
        frame.setContentPane(panel);
        JLabel label = new JLabel("Filter for owner:");
        panel.add(label);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        panel.add(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Optional: Prevent default behavior of adding a newline
                    String text = textArea.getText();
                    highlightedOwner = Integer.parseInt(text);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });


        JButton button = new JButton("Show Outlines");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to be performed when the button is clicked
                panel.drawOutlines = !panel.drawOutlines;
                panel.revalidate();
                panel.repaint();
            }
        });
        button = new JButton("Hide Labels");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to be performed when the button is clicked
                panel.hideLabels = !panel.hideLabels;
                panel.revalidate();
                panel.repaint();
            }
        });
        panel.revalidate();
        panel.repaint();
    }

    /**
     * A custom panel for rendering the graph visualization.
     */
    class DrawPane extends JPanel {
        Coordinates offset = new Coordinates(0, 0);
        double scale = 1.0 / 200.0;
        public boolean drawOutlines = false;
        public boolean hideLabels = false;
        private Coordinates min = new Coordinates(1e10, 1e10);
        private HashMap<Property, Coordinates> positions = new HashMap<Property, Coordinates>();
        private Graph<Property, DefaultEdge> graph;

        /**
         * Initializes the panel with a graph and computes vertex positions.
         *
         * @param inputGraph The graph to be visualized.
         */
        public DrawPane(Graph<Property, DefaultEdge> inputGraph) {
            graph = inputGraph;
            System.out.println(graph.vertexSet().size());
            for (Property p : graph.vertexSet()) {
                Coordinates pos = Coordinates.avg(p.getCorners());
                positions.put(p, pos);
                min.setX(min(min.getX(), pos.getX()));
                min.setY(min(min.getY(), pos.getY()));
            }
        }

        /**
         * Transforms a coordinate for rendering on the panel based on scale and offset.
         *
         * @param pos The original coordinate.
         * @return The transformed coordinate.
         */
        public Coordinates transform(Coordinates pos) {
            return new
                    Coordinates(((pos.getX() - min.getX()) * scale) + offset.getX() * scale + this.getWidth() / 2,
                    ((pos.getY() - min.getY()) * scale) + offset.getY() * scale + this.getHeight() / 2);
        }

        /**
         * Paints the graph onto the panel, including vertices and edges.
         *
         * @param g The graphics context.
         */
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLUE);

            for (Property p : graph.vertexSet()) {
                Coordinates pos = transform(positions.get(p));
                float padding = 10.f;
                if (pos.getX() < -padding || pos.getY() < -padding || pos.getX() > this.getWidth() + padding || pos.getY() > this.getHeight() + padding) {
                    continue;
                }
                boolean isHighlighted =
                        (highlightedOwner != null && p.getOwnerID() == highlightedOwner) ||
                                (highlightedExchange != null && (p == highlightedExchange.getFirst() || p == highlightedExchange.getSecond()));

                g.setColor(Color.BLUE);
                if (showOnlyHighlighted && !isHighlighted) {
                    continue;
                }
                if (drawOutlines) {
                    Coordinates prev = transform(p.getCorners().get(p.getCorners().size() - 1));
                    for (Coordinates v : p.getCorners()) {
                        Coordinates cur = transform(v);
                        g.drawLine((int) cur.getX(), (int) cur.getY(), (int) prev.getX(), (int) prev.getY());
                        prev = cur;
                    }
                } else {
                    int radius = clamp((int) (20 * scale), 4, 8);
                    g.fillOval((int) pos.getX() - radius / 2, (int) pos.getY() - radius / 2, radius, radius);
                }
                if (scale > 1.0 && !hideLabels) {
                    g.drawString("ID:" + p.getPropertyID(), (int) pos.getX(), (int) pos.getY());
                    if (scale > 1.5) {
                        g.drawString("Own:" + p.getOwnerID(), (int) pos.getX(), (int) pos.getY() + g.getFontMetrics().getAscent());

                    }
                }

            }
            g.setColor(Color.DARK_GRAY);


            for (Property p : graph.vertexSet()) {
                boolean isHighlighted =
                        (highlightedOwner != null && p.getOwnerID() == highlightedOwner) ||
                                (highlightedExchange != null && (p == highlightedExchange.getFirst() || p == highlightedExchange.getSecond()));
                if (showOnlyHighlighted && !isHighlighted) {
                    continue;
                }

                Coordinates pos = transform(positions.get(p));
                for (DefaultEdge edge : graph.edgesOf(p)) {
                    Property neighbour = graph.getEdgeTarget(edge);
                    Coordinates other = transform(positions.get(neighbour));
                    g.drawLine((int) pos.getX(), (int) pos.getY(), (int) other.getX(), (int) other.getY());
                }
            }
            g.setColor(Color.RED);
            for (Property p : graph.vertexSet()) {
                boolean isPartOfExchange = (highlightedExchange != null && (p == highlightedExchange.getFirst() || p == highlightedExchange.getSecond()));

                boolean isHighlighted =
                        (highlightedOwner != null && p.getOwnerID() == highlightedOwner) || isPartOfExchange;
                if (!isHighlighted) {
                    continue;
                }
                if (isPartOfExchange) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.RED);
                }
                Coordinates pos = transform(positions.get(p));
                float padding = 10.f;
                if (pos.getX() < -padding || pos.getY() < -padding || pos.getX() > this.getWidth() + padding || pos.getY() > this.getHeight() + padding) {
                    continue;
                }

                if (showOnlyHighlighted && !isHighlighted) {
                    continue;
                }
                if (drawOutlines) {
                    Coordinates prev = transform(p.getCorners().get(p.getCorners().size() - 1));
                    for (Coordinates v : p.getCorners()) {
                        Coordinates cur = transform(v);
                        g.drawLine((int) cur.getX(), (int) cur.getY(), (int) prev.getX(), (int) prev.getY());
                        prev = cur;
                    }
                } else {
                    int radius = clamp((int) (20 * scale), 4, 8) * 2;
                    g.fillOval((int) pos.getX() - radius / 2, (int) pos.getY() - radius / 2, radius, radius);
                }
                if (scale > 1.0 && !hideLabels) {
                    g.drawString("ID:" + p.getPropertyID(), (int) pos.getX(), (int) pos.getY());
                    if (scale > 1.5) {
                        g.drawString("Own:" + p.getOwnerID(), (int) pos.getX(), (int) pos.getY() + g.getFontMetrics().getAscent());

                    }
                }

            }
        }
    }

}
