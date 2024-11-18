package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;


public class GraphExample {

    private ArrayList<Property> properties;

    public GraphExample(ArrayList<Property> properties) {
        this.properties = properties;
        Graph<String, DefaultEdge> g = formGraph();
    }

    private Graph<String, DefaultEdge> formGraph() {
        Graph<Property, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        for (Property property : properties) {
            g.addVertex(property);
        }
    }

    public static void main(String[] args) {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        g.addVertex("v1");
        g.addVertex("v2");
        g.addEdge("v1","v2");

    }




}
