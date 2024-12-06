package iscte.se.landmanagement;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static java.lang.Math.*;

public class Visualizer extends JFrame {


    private Graph<Property, DefaultEdge> graph;
    private JFrame frame = new JFrame();
    private Integer highlightedOwner = null;
    private OwnerGraphStructure.PropertyPair highlightedExchange = null;
    private boolean showOnlyHighlighted = false;
    public void setHighlightedExchange(OwnerGraphStructure.PropertyPair highlightedExchange) {
        this.highlightedExchange = highlightedExchange;
        this.showOnlyHighlighted = true;
    }
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
                panel.repaint();            }
        });
        button = new JButton("Hide Labels");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to be performed when the button is clicked
                panel.hideLabels = !panel.hideLabels;
                panel.revalidate();
                panel.repaint();            }
        });
        panel.revalidate();
        panel.repaint();
    }

    class DrawPane extends JPanel {
        Coordinates offset = new Coordinates(0, 0);
        double scale = 1.0 / 200.0;
        public boolean drawOutlines = false;
        public boolean hideLabels = false;
        private Coordinates min = new Coordinates(1e10, 1e10);
        private HashMap<Property, Coordinates> positions = new HashMap<Property, Coordinates>();

        private Graph<Property, DefaultEdge> graph;

        public DrawPane(Graph<Property, DefaultEdge> inputGraph) {
            graph = inputGraph;
            System.out.println(graph.vertexSet().size());
            for(Property p : graph.vertexSet()) {
                Coordinates pos = Coordinates.avg(p.getCorners());
                positions.put(p, pos);
                min.setX(min(min.getX(), pos.getX()));
                min.setY(min(min.getY(), pos.getY()));
            }
        }
        public Coordinates transform(Coordinates pos) {
            return new
                    Coordinates(((pos.getX() - min.getX()) * scale) + offset.getX() * scale + this.getWidth() / 2,
                    ((pos.getY() - min.getY()) * scale) + offset.getY() * scale + this.getHeight() / 2);
        }
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLUE);

            for(Property p : graph.vertexSet()) {
                Coordinates pos = transform(positions.get(p));
                float padding = 10.f;
                if(pos.getX() < -padding || pos.getY() < -padding || pos.getX() > this.getWidth() + padding || pos.getY() > this.getHeight() + padding) {
                    continue;
                }
                boolean isHighlighted =
                        (highlightedOwner != null && p.getOwnerID() == highlightedOwner) ||
                                (highlightedExchange != null && (p == highlightedExchange.getFirst() || p == highlightedExchange.getSecond()));
                if(isHighlighted) {
                    g.setColor(Color.MAGENTA);
                }else {
                    g.setColor(Color.BLUE);
                }
                if(showOnlyHighlighted && !isHighlighted) { continue; }
                if(drawOutlines) {
                    Coordinates prev = transform(p.getCorners().get(p.getCorners().size() - 1));
                    for(Coordinates v : p.getCorners()) {
                        Coordinates cur = transform(v);
                        g.drawLine((int) cur.getX(), (int) cur.getY(), (int) prev.getX(), (int) prev.getY());
                        prev = cur;
                    }
                }else {
                    int radius = clamp((int)(20 * scale), 4, 8) * (isHighlighted?2:1);
                    g.fillOval((int)pos.getX() - radius / 2, (int)pos.getY() - radius / 2, radius, radius);
                }
                if( scale > 1.0 && !hideLabels) {
                    g.drawString("ID:" + p.getPropertyID(), (int)pos.getX(), (int)pos.getY());
                    if(scale > 1.5) {
                        g.drawString("Own:" + p.getOwnerID(), (int)pos.getX(), (int)pos.getY() + g.getFontMetrics().getAscent());

                    }
                }

            }
            g.setColor(Color.RED);


            for(Property p : graph.vertexSet()) {
                boolean isHighlighted =
                        (highlightedOwner != null && p.getOwnerID() == highlightedOwner) ||
                                (highlightedExchange != null && (p == highlightedExchange.getFirst() || p == highlightedExchange.getSecond()));
                if(showOnlyHighlighted && !isHighlighted) { continue; }

                Coordinates pos = transform(positions.get(p));
                for (DefaultEdge edge : graph.edgesOf(p)) {
                    Property neighbour = graph.getEdgeTarget(edge);
                    Coordinates other = transform(positions.get(neighbour));
                    g.drawLine((int) pos.getX(), (int) pos.getY(), (int) other.getX(), (int) other.getY());
                }
            }
        }
    }

}
