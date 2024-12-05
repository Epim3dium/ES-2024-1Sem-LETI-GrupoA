package iscte.se.landmanagement;

import javafx.util.Pair;
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


    private final Graph<Integer, NeighbourEdge> graph;
    private final Graph<Property, DefaultEdge> neigbour_map;
    private HashSet<Integer> owners;

    private final double fitness_increase_coef = 1;
    private final double fitness_difference_coef = 1;

    static class IslandMember {
        boolean isMiddle = false;
        int islandID = 0;
        boolean isLeaf = false;
        public IslandMember(IslandMember info) {
            isMiddle = info.isMiddle;
            isLeaf = info.isLeaf;
            islandID = info.islandID;
        }
        public IslandMember() {}
    }
    static class IslandInfo {
        int count = 0;
        double sum = 0;
        double getAverage() {
            return sum / count;
        }
        public IslandInfo(IslandInfo info) {
            count = info.count;
            sum = info.sum;
        }
        public IslandInfo() {}
    }
    private Map<Property, IslandMember> islands = new HashMap<>();
    private Map<Integer, IslandInfo> owner_islands = new HashMap<>();

    private Integer next_island = 0;

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

        List<PropertyPair> exchanges = og.generateAllExchanges();
        System.out.println("total possible exchanges: " + exchanges.size());
        //leave only tail of exchanges
        int head = exchanges.size() - 10;
        for(int i = 0; i < head; i++) {
            exchanges.removeFirst();
        }
        //display exchanges
        for (PropertyPair pair : exchanges) {
            System.out.println("[" + pair.first + "] -> [" + pair.second + "]");
            System.out.println("\towners: " + pair.first.getOwnerID() + " -> " + pair.second.getOwnerID());
            System.out.println("\tareas:  " + pair.first.getShapeArea() + " -> " + pair.second.getShapeArea());
            //only for debugging
            Pair<Double, Double> p = calcAvgAreaIncrease(og, pair);
            System.out.println("\tarea increase:" + p.getKey() + " & " + p.getValue());
        }
    }

    public void visualizeGraph() {

        //Visualizer.PositionCaller<Integer> posCaller = (p) -> { return Coordinates.avg(owners_positions.get(p)); };
        //Visualizer.OutlineCaller<Integer> outlineCaller = null;
        //Visualizer vis = new Visualizer(graph, posCaller, outlineCaller);
    }


    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.neigbour_map = property_neighbours;
        this.graph = formGraph(property_neighbours);
        calcIslands(this.islands, this.owner_islands);
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

    static Pair<Double, Double> calcAvgAreaIncrease(OwnerGraphStructure ogs, PropertyPair exchange) {
        Property first = exchange.first;
        Property second = exchange.second;
        IslandMember member_first = new IslandMember(ogs.islands.get(first));
        IslandMember member_second = new IslandMember(ogs.islands.get(second));

        IslandInfo island_first = new IslandInfo(ogs.owner_islands.get(first.getOwnerID()) );
        IslandInfo island_second = new IslandInfo(ogs.owner_islands.get(second.getOwnerID()) );

        assert(island_first != null);
        assert(island_second != null);
        assert(member_first != null);
        assert(member_second != null);

        double first_avg = island_first.getAverage();
        double second_avg = island_second.getAverage();

        if(member_first.isMiddle || member_first.isLeaf) {
            island_first.count--;
            assert(island_first.count != 0);
        }
        if(member_second.isMiddle || member_second.isLeaf) {
            island_second.count--;
            assert(island_second.count != 0);
        }
        island_first.sum += -first.getShapeArea() + second.getShapeArea();
        island_second.sum += -second.getShapeArea() + first.getShapeArea();

        double first_avg_aft = island_first.getAverage();
        double second_avg_aft = island_second.getAverage();
        double first_island_icrease = first_avg_aft - first_avg;
        double second_island_increase = second_avg_aft - second_avg;
        return new Pair<>(first_island_icrease, second_island_increase);
    }
    static double exchangeFitness(OwnerGraphStructure ogs, PropertyPair exchange) {
        Pair<Double, Double> increase = calcAvgAreaIncrease(ogs, exchange);
        if(increase.getValue() < 0) { return 0;}
        if(increase.getKey() < 0) { return 0;}
        double difference =
                Math.pow(exchange.first.getShapeArea() - exchange.second.getShapeArea(), 2.0)
                        * ogs.fitness_difference_coef;
        double avgIncrease = (increase.getKey() + increase.getValue()) / 2.0 * ogs.fitness_increase_coef;
        double increaseFairness = Math.abs(increase.getKey() - increase.getValue());
        return avgIncrease - increaseFairness - difference;
    }

    static class PairComparator implements Comparator<PropertyPair> {
        OwnerGraphStructure owner_graph_structure;
        public int compare(PropertyPair o1, PropertyPair o2) {
            double fit1 = exchangeFitness(owner_graph_structure, o1);
            double fit2 = exchangeFitness(owner_graph_structure, o2);
            return Double.compare(fit1, fit2);
        }
        public PairComparator(OwnerGraphStructure ogs) {
            owner_graph_structure = ogs;
        }
    }
    void sortExchangesByFitness(List<PropertyPair> exchanges) {
        Collections.sort(exchanges, new PairComparator(this));
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
        sortExchangesByFitness(result);
        return result;
    }
    boolean checkIsMiddle(Property prop) {
        int total_count = 0;
        for(DefaultEdge e : neigbour_map.edgesOf(prop)) {
            Property other = neigbour_map.getEdgeTarget(e);
            if(other.getOwnerID() == prop.getOwnerID()) {
                total_count++;
            }
        }
        return total_count > 1;
    }
    List<Property> searchOwnedNeighbours(Property prop, Set<Property> ban_list) {
        List<Property> result = new ArrayList<>();
        for(DefaultEdge e : neigbour_map.edgesOf(prop)) {
            Property other = neigbour_map.getEdgeTarget(e);
            if(other.getOwnerID() == prop.getOwnerID() && !ban_list.contains(other)) {
                result.add(other);
            }
        }
        return result;
    }
    void calcIslands(Map<Property, IslandMember> islands, Map<Integer, IslandInfo> island_owners) {
        Set<Property> computed = new HashSet<>();
        for(Property p : neigbour_map.vertexSet()) {
            if(computed.contains(p)){
                continue;
            }
            Queue<Property> to_compute = new LinkedList<>();
            to_compute.add(p);
            int cur_island = this.next_island++;
            IslandInfo info = new IslandInfo();
            while(!to_compute.isEmpty()) {

                Property current = to_compute.poll();
                if(computed.contains(current)) {
                    continue;
                }
                computed.add(current);

                IslandMember member = new IslandMember();
                member.islandID = cur_island;
                member.isMiddle = checkIsMiddle(p);

                info.count ++;
                info.sum += current.getShapeArea();
                List<Property> others = searchOwnedNeighbours(current, computed);
                to_compute.addAll(others);
                if(others.isEmpty() && p == current) {
                    member.isLeaf = true;
                }
                islands.put(current, member);
            }
            island_owners.putIfAbsent(p.getOwnerID(), new IslandInfo());
            island_owners.get(p.getOwnerID()).count += info.count;
            island_owners.get(p.getOwnerID()).sum += info.sum;
        }
    }
}