package iscte.se.landmanagement;



import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import java.util.ArrayList;
import java.util.List;


public class GraphStructure {

    private Graph<Property, DefaultEdge> graph;
    private ArrayList<Property> properties;
    private int threshold;


    public GraphStructure(ArrayList<Property> properties, int threshold) {
        this.properties = properties;
        this.threshold = threshold ;
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
        int t = 0;
        for (Property property : properties) {

            g.addVertex(property);
        }
        for (int i = 0; i < properties.size(); i++) {
            for (int j = i + 1; j < properties.size(); j++) {
                Property p1 = properties.get(i);
                Property p2 = properties.get(j);


                if (areAdjacentByDistance(p1, p2)) {
                    System.out.println(t + " ");
                    t++;
                    g.addEdge(p1, p2);
                    addNeighbours(p1, p2);
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
     * Establishes a bidirectional neighbor relationship between two properties
     *
     * @param p1 p1 The first `Property` object.
     * @param p2 p2 The second `Property` object.
     */
    private void addNeighbours(Property p1, Property p2) {
        p1.addNeighbour(p2);
        p2.addNeighbour(p1);
    }

    /**
     * Determines if two properties are adjacent based on the distance between their corners.
     *
     * @param p1 p1 The first `Property` object to compare.
     * @param p2 p2 The second `Property` object to compare.
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
     * Calculates the Euclidean distance between two points represented by `Coordinates`.
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