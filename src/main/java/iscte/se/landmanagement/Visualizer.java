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

public class Visualizer<T> extends JFrame {
    public interface PositionCaller<T> {
        Coordinates getPos(T data);
    }
    public interface OutlineCaller<T> {
        List<Coordinates> getOutline(T data);
    }

    private OutlineCaller<T> outlineDrawer;
    private PositionCaller<T> positionCaller;
    private Graph<T, DefaultEdge> graph;
    private JFrame frame = new JFrame();

    public Visualizer(Graph<T, DefaultEdge> inputGraph, PositionCaller<T> positionCaller, OutlineCaller<T> outliner) {
        this.positionCaller = positionCaller;
        this.outlineDrawer = outliner;
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
                panel.scale = clamp(panel.scale, 0.0, 1.0);
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

    }
    class DrawPane extends JPanel {
        Coordinates offset = new Coordinates(0, 0);
        double scale = 1.0 / 200.0;
        private boolean drawOutlines = false;
        private Coordinates min = new Coordinates(1e10, 1e10);
        private HashMap<T, Coordinates> positions = new HashMap<T, Coordinates>();

        private Graph<T, DefaultEdge> graph;

        public DrawPane(Graph<T, DefaultEdge> inputGraph) {
            graph = inputGraph;
            System.out.println(graph.vertexSet().size());
            for(T p : graph.vertexSet()) {
                Coordinates pos = positionCaller.getPos(p);//avg(p.getCorners());
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

            for(T p : graph.vertexSet()) {
                if(drawOutlines && outlineDrawer != null) {
                    List<Coordinates> outline = outlineDrawer.getOutline(p);
                    Coordinates prev = transform(outline.get(outline.size() - 1));
                    for(Coordinates v : outline) {
                        Coordinates cur = transform(v);
                        g.drawLine((int) cur.getX(), (int) cur.getY(), (int) prev.getX(), (int) prev.getY());
                        prev = cur;
                    }
                }else {
                    int radius = clamp((int)(20 * scale), 4, 10);
                    Coordinates pos = transform(positions.get(p));
                    g.fillOval((int)pos.getX() - radius / 2, (int)pos.getY() - radius / 2, radius, radius);
                }

            }
            g.setColor(Color.RED);

            for(T p : graph.vertexSet()) {
                Coordinates pos = transform(positions.get(p));
                for (DefaultEdge edge : graph.edgesOf(p)) {
                    T neighbour = graph.getEdgeTarget(edge);
                    Coordinates other = transform(positions.get(neighbour));
                    g.drawLine((int) pos.getX(), (int) pos.getY(), (int) other.getX(), (int) other.getY());
                }
            }
        }
    }

}
