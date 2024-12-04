package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class OwnerGraphStructure {
    static class NeighbourEdge extends DefaultEdge {
        public void addPair(Property propery1, Property propery2) {
            incidents.add(new PropertyPair(propery1, propery2));
        }
        private final ArrayList<PropertyPair> incidents;
        public NeighbourEdge(Property propery1, Property propery2) {
            incidents = new ArrayList<>();
            addPair(propery1, propery2);        }
    }
    private Graph<Integer, NeighbourEdge> graph;
    private HashSet<Integer> owners;

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

        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 2);
        OwnerGraphStructure og = new OwnerGraphStructure(g.getG());
        //g.visualizeGraph();
        og.visualizeGraph();
    }

    public void visualizeGraph() {
        ;
        //Visualizer.PositionCaller<Integer> posCaller = (p) -> { return Coordinates.avg(owners_positions.get(p)); };
        //Visualizer.OutlineCaller<Integer> outlineCaller = null;
        //Visualizer vis = new Visualizer(graph, posCaller, outlineCaller);
    }


    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.graph = formGraph(property_neighbours);
    }

    private static void insertNew(Integer id, Graph<Integer, NeighbourEdge> g) {
        if(!g.containsVertex(id)) {
            g.addVertex(id);
        }
    }
    private Graph<Integer, NeighbourEdge> formGraph(Graph<Property, DefaultEdge> property_neighbours) {
        SimpleGraph<Integer, NeighbourEdge> g = new SimpleGraph<>(NeighbourEdge.class);
        owners = new HashSet<>();

        for(Property p : property_neighbours.vertexSet()) {
            owners.add(p.getOwnerID());

            Integer owner = p.getOwnerID();
            insertNew(owner, g);

            for(DefaultEdge e : property_neighbours.edgesOf(p)) {
                Property neighbour_prop =property_neighbours.getEdgeTarget(e);
                Integer neighbour = neighbour_prop.getOwnerID();
                if(neighbour.equals(owner)) {
                    continue;
                }
                insertNew(neighbour, g);

                boolean edgeExists = g.containsEdge(owner, neighbour) || g.containsEdge(neighbour, owner);
                if(!edgeExists) {
                    g.addEdge(owner, neighbour, new NeighbourEdge(neighbour_prop, p));
                }else {
                    NeighbourEdge edge = g.getEdge(owner, neighbour);
                    edge.addPair(neighbour_prop, p);
                }
            }
        }
        return g;
    }
}