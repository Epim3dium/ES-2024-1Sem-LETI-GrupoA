package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.net.PortUnreachableException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OwnerGraphStructure {


    private Graph<Integer, DefaultEdge> graph;
    private HashSet<Integer> owners;
    private HashMap<Integer, List<Coordinates>> owners_positions;


    private Graph<Property, DefaultEdge> properties;

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
        OwnerGraphStructure og = new OwnerGraphStructure(g.getG());
        //g.visualizeGraph();
        og.visualizeGraph();
    }

    public void visualizeGraph() {
        ;
        Visualizer.PositionCaller<Integer> posCaller = (p) -> { return Coordinates.avg(owners_positions.get(p)); };
        Visualizer.OutlineCaller<Integer> outlineCaller = null;
        Visualizer vis = new Visualizer(graph, posCaller, outlineCaller);
    }


    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.properties = property_neighbours;
        this.graph = formGraph(property_neighbours);
    }

    private Graph<Integer, DefaultEdge> formGraph(Graph<Property, DefaultEdge> property_neighbours) {
        SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        owners_positions = new HashMap<>();
        owners = new HashSet<>();

        for(Property p : property_neighbours.vertexSet()) {
            owners.add(p.getOwnerID());

            Integer owner = p.getOwnerID();
            if(!g.containsVertex(p.getOwnerID())) {
                g.addVertex(owner);
                owners_positions.put(owner, new ArrayList<>());
            }
            owners_positions.get(owner).add(Coordinates.avg(p.getCorners()));

            for(DefaultEdge e : property_neighbours.edgesOf(p)) {
                Property neighbour_prop =property_neighbours.getEdgeTarget(e);
                Integer neighbour = neighbour_prop.getOwnerID();
                if(!g.containsVertex(neighbour)) {
                    g.addVertex(neighbour);
                    owners_positions.put(neighbour, new ArrayList<>());
                }
                owners_positions.get(neighbour).add(Coordinates.avg(neighbour_prop.getCorners()));

                if(!g.containsEdge(owner, neighbour)
                        && !g.containsEdge(neighbour, owner)
                        && !neighbour.equals(owner))
                {
                    g.addEdge(p.getOwnerID(), neighbour);
                }
            }
        }
        return g;
    }

    public Graph<Integer, DefaultEdge> getGraph() {
        return graph;
    }

    public Graph<Property, DefaultEdge> getProperties() {
        return properties;
    }
}