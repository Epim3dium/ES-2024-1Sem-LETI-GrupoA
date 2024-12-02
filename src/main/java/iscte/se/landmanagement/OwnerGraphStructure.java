package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OwnerGraphStructure {
    private Graph<Integer, DefaultEdge> graph;
    private ArrayList<Integer> owners;
    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.graph = formGraph(property_neighbours);
    }

    private Graph<Integer, DefaultEdge> formGraph(Graph<Property, DefaultEdge> property_neighbours) {
        SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        return g;
    }
}