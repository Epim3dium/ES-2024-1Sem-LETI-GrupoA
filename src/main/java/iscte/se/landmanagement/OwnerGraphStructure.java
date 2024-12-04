package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class OwnerGraphStructure {
    static class PropertyPair {
        Property first;
        Property second;
        PropertyPair(Property first, Property second) {
            if(first.getOwnerID() > second.getOwnerID()) {
                this.first = first;
                this.second = second;
            }else {
                this.first = second;
                this.second = first;
            }
        }
    }
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
        List<PropertyPair> exchanges = og.generateAllExchanges();
        System.out.println("total possible exchanges: " + exchanges.size());
        sortExchangesByFitness(exchanges);
        for (PropertyPair pair : exchanges) {
            System.out.println("[" + pair.first + "] -> [" + pair.second + "]");
            System.out.println("\towners: " + pair.first.getOwnerID() + " -> " + pair.second.getOwnerID());
            System.out.println("\tareas:  " + pair.first.getShapeArea() + " -> " + pair.second.getShapeArea());
        }
    }

    public void visualizeGraph() {

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
    static double exchangeFitness(PropertyPair exchange) {
        return exchangeFitness(exchange, 1.0, 1.0);
    }
    static double exchangeFitness(PropertyPair exchange, double sizeCoef, double diffCoef) {
        double avgSize = (exchange.first.getShapeArea() + exchange.second.getShapeArea()) / 2.0 * sizeCoef;
        double difference = Math.pow(exchange.first.getShapeArea() - exchange.second.getShapeArea(), 2.0) * diffCoef;
        return avgSize - difference;
    }
    static class PairComparator implements Comparator<PropertyPair> {
        public int compare(PropertyPair o1, PropertyPair o2) {
            double fit1 = exchangeFitness(o1), fit2 = exchangeFitness(o2);
            return Double.compare(fit1, fit2);
        }
    }
    static void sortExchangesByFitness(List<PropertyPair> exchanges) {
        Collections.sort(exchanges, new PairComparator());
    }
    List<PropertyPair> generateAllExchanges() {
        ArrayList<PropertyPair> result = new ArrayList<>();
        for(NeighbourEdge edge : graph.edgeSet()) {
            if(edge.incidents.size() <= 1) {
                continue;
            }
            List<PropertyPair> all_contacts = edge.incidents;
            for(int i = 0; i < all_contacts.size(); i++) {
                for(int ii = 0; ii < all_contacts.size(); ii++) {
                    if(i == ii) {
                        continue;
                    }
                    Property prop1 = all_contacts.get(i).first;
                    Property prop2 = all_contacts.get(ii).second;
                    result.add(new PropertyPair(prop1, prop2));
                    assert(prop1.getOwnerID() != prop2.getOwnerID());
                }
            }
        }
        return result;
    }
}