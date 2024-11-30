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

        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 20);
        System.out.println(g.getG().vertexSet().size());
        System.out.println(g.getG().edgeSet().size());
        //g.visualizeGraph();
        g.visualizeGraph();


    }

    public void visualizeGraph() {
        Visualizer vis = new Visualizer(graph);
    }


    public GraphStructure(ArrayList<Property> properties, int threshold) {
        this.properties = properties;
        this.threshold = threshold;
        System.out.println("w");
        this.graph = formGraph();

    }


    /**
     * Creates a graph where each vertex represents a {@link Property}, and edges are added
     * between vertices if the corresponding properties are adjacent based on a defined distance condition.
     *
     * @return
     */
    private Graph<Property, DefaultEdge> formGraph() {

        Graph<Property, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        int t = 0;
        int sizeToRemove = properties.size() - 10000;
        // Remove from the beginning for simplicity
        properties.subList(sizeToRemove, properties.size()).clear();

        for (Property property : properties) {
            g.addVertex(property);
        }
        double max_prop_size = 0.f;
        List<AABB> aabbs = new ArrayList<>();
        AABB range_all = new AABB();
        int max_edges = 0;
        for (Property property : properties) {
            List<Coordinates> corners = property.getCorners();
            AABB current = new AABB();
            for (Coordinates c : corners) {
                current.expandToContain(c);
                range_all.expandToContain(c);
            }
            max_edges = Math.max(max_edges, property.getCorners().size());
            max_prop_size = Math.max(current.area().getX(), max_prop_size);
            max_prop_size = Math.max(current.area().getY(), max_prop_size);
            aabbs.add(current);
        }
        System.out.println("max edges: " + max_edges);

        double cell_size = max_prop_size;
        double maxIdxX = range_all.area().getX() / cell_size;
        double maxIdxY = range_all.area().getY() / cell_size;
        List<List<List<Integer>>> grid = new ArrayList<>();
        //creating the grid
        for (int i = 0; i < maxIdxY + 1; i++) {
            grid.add(new ArrayList<>());
            for (int ii = 0; ii < maxIdxX + 1; ii++) {
                grid.get(i).add(new ArrayList<>());
            }
        }
        //inserting to grid
        for (int i = 0; i < properties.size(); i++) {
            AABB aabb = aabbs.get(i);
            Property p = properties.get(i);
            Coordinates transformed = aabb.center();
            transformed.setX((transformed.getX() - range_all.getLeft()) / cell_size);
            transformed.setY((transformed.getY() - range_all.getBottom()) / cell_size);
            int indexX = (int) transformed.getX();
            int indexY = (int) transformed.getY();
            grid.get(indexY).get(indexX).add(i);
        }
        for (int i = 0; i < maxIdxY + 1; i++) {
            for (int ii = 0; ii < maxIdxX + 1; ii++) {
                List<Integer> potential_neighbors = new ArrayList<>(grid.get(i).get(ii));
                int ogSize = potential_neighbors.size();
                if (i != 0) {
                    potential_neighbors.addAll(grid.get(i - 1).get(ii));
                }
                if (ii != 0) {
                    potential_neighbors.addAll(grid.get(i).get(ii - 1));
                }
                if (ii != 0 && i != 0) {
                    potential_neighbors.addAll(grid.get(i - 1).get(ii - 1));
                }
                for (int p = 0; p < ogSize; p++) {
                    for (int pp = p + 1; pp < potential_neighbors.size(); pp++) {
                        Integer idxP = potential_neighbors.get(p);
                        Integer idxPP = potential_neighbors.get(pp);

                        AABB aabb1 = aabbs.get(idxP);
                        AABB aabb2 = aabbs.get(idxPP);
                        if (!aabb1.isOverlapping(aabb2, threshold)) {
                            continue;
                        }
                        Property p1 = properties.get(idxP);
                        Property p2 = properties.get(idxPP);
                        if (areAdjacentByDistance(p1, p2)) {
                            if (t % 100 == 0) {
                                System.out.println(t + " neighbours ");

                            }
                            t++;
                            g.addEdge(p1, p2);
                            addNeighbours(p1, p2);
                        }
                    }
                }
            }
        }
        System.out.println(t + " neighbours ");

        //searching for neighbours
        return g;
    }


    /**
     * Establishes a bidirectional neighbor relationship between two properties
     *
     * @param p1 p1 The first {@link Property} object.
     * @param p2 p2 The second {@link Property} object.
     */
    private void addNeighbours(Property p1, Property p2) {
        p1.addNeighbour(p2);
        p2.addNeighbour(p1);
    }

    /**
     * Determines if two properties are adjacent based on the distance between their corners.
     *
     * @param p1 p1 The first {@link Property} object to compare.
     * @param p2 p2 The second {@link Property} object to compare.
     * @return `true` if the properties are adjacent (i.e., at least one pair of corners has a distance
     * *         less than or equal to the threshold), otherwise `false`.
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
     * Calculates the Euclidean distance between two points represented by {@link Coordinates}.
     *
     * @param c1 c1 The first point as a `Coordinates` object.
     * @param c2 c2 The second point as a `Coordinates` object.
     * @return The Euclidean distance between `c1` and `c2`.
     */
    public static double calculateDistance(Coordinates c1, Coordinates c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Graph<Property, DefaultEdge> getG() {
        return this.graph;
    }

}





