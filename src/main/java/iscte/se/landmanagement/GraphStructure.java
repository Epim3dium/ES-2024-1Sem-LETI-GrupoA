package iscte.se.landmanagement;


import com.mxgraph.swing.mxGraphComponent;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class GraphStructure {

    private Graph<Property, DefaultEdge> graph;
    private ArrayList<Property> properties;
    private int threshold;

    public static void main(String[] args) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("Madeira-Moodle-1.1.csv");
        if (url == null) {
            System.out.println("Arquivo CSV não encontrado!");
            return;
        }

        Path path = Paths.get(url.toURI());
        PropFileReader propFileReader = new PropFileReader(path);

        propFileReader.readFile();
        propFileReader.convertToPropertiy();

        GraphStructure g= new GraphStructure(propFileReader.getProperties(),1);
        //g.visualizeGraph();



//        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>((g.getG()));
//
//        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());
//
//
//        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
//
//
//        graphComponent.addMouseWheelListener(new MouseWheelListener() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                if (e.getWheelRotation() < 0) {
//
//                    graphComponent.zoomIn();
//                } else {
//
//                    graphComponent.zoomOut();
//                }
//            }
//        });
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(graphComponent);
//        frame.setSize(1400, 1400);
//        frame.setVisible(true);





    }

//    public void visualizeGraph() {
//        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>(this.graph);
//        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());
//
//        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
//        graphComponent.addMouseWheelListener(new MouseWheelListener() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                if (e.getWheelRotation() < 0) {
//                    graphComponent.zoomIn();
//                } else {
//                    graphComponent.zoomOut();
//                }
//            }
//        });
//
//        JFrame frame = new JFrame("Graph Visualization");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(graphComponent);
//        frame.setSize(1400, 1400);
//        frame.setVisible(true);
//    }


    public GraphStructure(ArrayList<Property> properties, int threshold) {
        this.properties = properties;
        this.threshold=threshold;
        System.out.println("w");
        this.graph = formGraph();

    }




    /**
     * Creates a graph where each vertex represents a `Property` object, and edges are added
     * between vertices if the corresponding properties are adjacent based on a defined distance condition.
     *
     * @return
     */
    private Graph<Property, DefaultEdge> formGraph() {
        Graph<Property, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        int t=0;
        for (Property property : properties) {

            g.addVertex(property);
        }
        double max_prop_size = 0.f;
        List<AABB> aabbs = new ArrayList<>();
        AABB range_all = new AABB();
        for (Property property : properties) {
            List<Coordinates> corners = property.getCorners();
            AABB current = new AABB();
            for ( Coordinates c : corners) {
                current.expandToContain(c);
                range_all.expandToContain(c);
            }
            max_prop_size = Math.max(current.area().getX(), max_prop_size);
            max_prop_size = Math.max(current.area().getY(), max_prop_size);
            aabbs.add(current);
        }
        for (int i = 0; i < properties.size(); i++) {
            for (int j = i + 1; j < properties.size(); j++) {
                Property p1 = properties.get(i);
                Property p2 = properties.get(j);


                if (areAdjacentByDistance(p1, p2)){
                    System.out.println(t+" ");
                    t++;
                    g.addEdge(p1, p2);
                    addNeighbours(p1,p2);
                }
            }
        }
//        System.out.println(g.vertexSet());
//
//        System.out.println(g.edgeSet());

        System.out.println(g.vertexSet().size());
        System.out.println(g.edgeSet().size());

        return g;
    }


    /**
     *Establishes a bidirectional neighbor relationship between two properties
     * @param p1 p1 The first `Property` object.
     * @param p2 p2 The second `Property` object.
     */
    private void addNeighbours(Property p1, Property p2) {
        p1.addNeighbour(p2);
        p2.addNeighbour(p1);
    }

    /**
     * Determines if two properties are adjacent based on the distance between their corners.
     * @param p1 p1 The first `Property` object to compare.
     * @param p2 p2 The second `Property` object to compare.
     * @return `true` if the properties are adjacent (i.e., at least one pair of corners has a distance
     *  *         less than or equal to the threshold), otherwise `false`.
     */
    public boolean areAdjacentByDistance(Property p1, Property p2) {
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

    /**
     * Calculates the Euclidean distance between two points represented by `Coordinates`.
     * @param c1 c1 The first point as a `Coordinates` object.
     * @param c2 c2 The second point as a `Coordinates` object.
     * @return The Euclidean distance between `c1` and `c2`.
     */
    public static double calculateDistance(Coordinates c1, Coordinates c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Graph<Property,DefaultEdge> getG(){
        return this.graph;
    }







}