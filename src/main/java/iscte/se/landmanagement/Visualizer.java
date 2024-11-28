package iscte.se.landmanagement;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class Visualizer extends JFrame {
    private Graph<Property, DefaultEdge> graph;
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
    public Visualizer(Graph<Property, DefaultEdge> inputGraph) {
        System.out.println("staring visualizer");
        // Create a JGraphT graph
        graph = inputGraph;
// Create and add vertices
        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

// Apply a layout
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        mxGraphComponent component = new mxGraphComponent(graphAdapter);
        component.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        component.zoomIn(); // Zoom in
                    } else if(e.getWheelRotation() > 0) {
                        component.zoomOut(); // Zoom out
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