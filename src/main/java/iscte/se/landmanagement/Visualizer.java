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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static java.lang.Math.*;

public class Visualizer extends JFrame {
    private Graph<Property, DefaultEdge> graph;
    JFrame frame = new JFrame();

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

        frame.setUndecorated(true);
    }
    class DrawPane extends JPanel {
        Coordinates offset = new Coordinates(0, 0);
        double scale = 1.0 / 200.0;
        private boolean drawOutlines = false;
        private Coordinates min = new Coordinates(1e10, 1e10);
        private HashMap<Property, Coordinates> positions = new HashMap<Property, Coordinates>();
        Coordinates avg(ArrayList<Coordinates> coords) {
            Coordinates result = new Coordinates(0, 0);
            for (Coordinates coord : coords) {
                result.setX(result.getX() + coord.getX());
                result.setY(result.getY() + coord.getY());
            }
            result.setX(result.getX() / coords.size());
            result.setY(result.getY() / coords.size());
            return result;
        }
                    }
                }else {
                    if (e.isShiftDown()) {
                        // Horizontal scrolling
                        component.getHorizontalScrollBar().setValue(
                                component.getHorizontalScrollBar().getValue() + e.getWheelRotation() * 20
                        );
                    } else {
                        // Vertical scrolling
                        component.getVerticalScrollBar().setValue(
                                component.getVerticalScrollBar().getValue() + e.getWheelRotation() * 20
                        );
                    }
                }
            }
        });
// Display in a JFrame
        JFrame frame = new JFrame("Neighbour Visualisation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(component);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}