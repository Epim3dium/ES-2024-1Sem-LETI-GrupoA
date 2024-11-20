package iscte.se.landmanagement;


import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GraphStructure {

    private Graph<Property, DefaultEdge> graph;
    private ArrayList<Property> properties;
    private int threshold;

    public static void main(String[] args) throws IOException {
        PropertiesFileReader p=new PropertiesFileReader("src/main/resources/Madeira-Moodle.csv");



        GraphStructure g= new GraphStructure(p.getProperties());



        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>((g.getG()));

        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());


        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);


        graphComponent.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {

                    graphComponent.zoomIn();
                } else {

                    graphComponent.zoomOut();
                }
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(graphComponent);
        frame.setSize(1400, 1400);
        frame.setVisible(true);





    }




    public GraphStructure(ArrayList<Property> properties) {
        this.properties = properties;
        this.threshold=4;
        this.graph = formGraph();

    }

    public Graph<Property,DefaultEdge> getG(){
        return this.graph;
    }


    private Graph<Property, DefaultEdge> formGraph() {
        Graph<Property, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        for (Property property : properties) {
            g.addVertex(property);
        }
        for (int i = 0; i < properties.size(); i++) {
            for (int j = i + 1; j < properties.size(); j++) {
                Property p1 = properties.get(i);
                Property p2 = properties.get(j);


                if (areAdjacentByDistance(p1, p2, threshold)) {
                    g.addEdge(p1, p2);
                }
            }
        }
        System.out.println(g.vertexSet());
        System.out.println(g.edgeSet());
        return g;
    }

    public static boolean areAdjacentByDistance(Property p1, Property p2, double threshold) {
        List<Coordinates> corners1 = p1.getCorners();
        List<Coordinates> corners2 = p2.getCorners();


        for (Coordinates c1 : corners1) {
            for (Coordinates c2 : corners2) {
                double distance = calculateDistance(c1, c2);
                if (distance <= threshold) {
                    return true;
                }
            }
        }
        return false;
    }


    private static double calculateDistance(Coordinates c1, Coordinates c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }







}