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

    public void visualizeGraph() {
        ;
    }


    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.graph = formGraph(property_neighbours);
    }

    private Graph<Integer, DefaultEdge> formGraph(Graph<Property, DefaultEdge> property_neighbours) {
        SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        for(Property p : property_neighbours.vertexSet()) {
            if(!g.containsVertex(p.getOwnerID())) {
                g.addVertex(p.getOwnerID());
            }
            for(DefaultEdge e : property_neighbours.edgesOf(p)) {
                Property neighbour = property_neighbours.getEdgeTarget(e);
                if(!g.containsVertex(neighbour.getOwnerID())) {
                    g.addVertex(neighbour.getOwnerID());
                }

                if(!g.containsEdge(p.getOwnerID(), neighbour.getOwnerID())
                        && !g.containsEdge(neighbour.getOwnerID(), p.getOwnerID())
                        && neighbour.getOwnerID() != p.getOwnerID())
                {
                    g.addEdge(p.getOwnerID(), neighbour.getOwnerID());
                }
            }
        }
        return g;
    }
}