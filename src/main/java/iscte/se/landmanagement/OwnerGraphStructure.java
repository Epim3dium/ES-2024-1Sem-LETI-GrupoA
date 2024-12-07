package iscte.se.landmanagement;

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

/**
 * Represents a graph structure to model relationships between property owners based on property adjacency.
 * Supports operations such as calculating exchanges, fitness of exchanges, and identifying islands of properties
 * owned by the same owner.
 */
public class OwnerGraphStructure {
    /**
     * Represents a pair of properties with associated owners.
     */
    public static class PropertyPair {
        public Property getSecond() {
            return second;
        }

        public Property getFirst() {
            return first;
        }

        Property first;
        Property second;

        /**
         * Swaps the two properties in the pair.
         */
        public void swap() {
            Property temp = first;
            first = second;
            second = temp;
        }

        /**
         * Constructor for creating a property pair. Ensures the properties are sorted by owner ID.
         *
         * @param first  the first property in the pair
         * @param second the second property in the pair
         */
        PropertyPair(Property first, Property second) {
            if (first.getOwnerID() > second.getOwnerID()) {
                this.first = first;
                this.second = second;
            } else {
                this.first = second;
                this.second = first;
            }
        }
    }

    /**
     * Represents an edge between owners, holding the pairs of properties involved in the relationship.
     */
    static class NeighbourEdge extends DefaultEdge {
        /**
         * Adds a new property pair to the edge.
         *
         * @param property1 the first property
         * @param property2 the second property
         */
        public void addPair(Property property1, Property property2) {
            incidents.add(new PropertyPair(property1, property2));
        }

        private final ArrayList<PropertyPair> incidents;

        /**
         * Constructor for creating a neighbour edge with an initial property pair.
         *
         * @param propery1 the first property
         * @param propery2 the second property
         */
        public NeighbourEdge(Property propery1, Property propery2) {
            incidents = new ArrayList<>();
            addPair(propery1, propery2);
        }
    }

    private final Graph<Integer, NeighbourEdge> graph;
    private final Graph<Property, DefaultEdge> neigbour_map;

    private final double fitness_increase_coef = 5;
    private final double fitness_difference_coef = 1;

    /**
     * Represents metadata for a property in an island.
     */
    private static class IslandMember {
        boolean isMiddle = false;
        int islandID = 0;
        boolean isLeaf = false;

        public IslandMember(IslandMember info) {
            isMiddle = info.isMiddle;
            isLeaf = info.isLeaf;
            islandID = info.islandID;
        }

        public IslandMember() {
        }
    }

    /**
     * Represents metadata for an owner's properties in an island.
     */
    private static class IslandInfo {
        int count = 0;
        double sum = 0;

        /**
         * Returns the average area of properties in the island.
         *
         * @return the average area
         */
        double getAverage() {
            return sum / count;
        }

        public IslandInfo(IslandInfo info) {
            count = info.count;
            sum = info.sum;
        }

        public IslandInfo() {
        }
    }

    private Map<Property, IslandMember> islands = new HashMap<>();
    private Map<Integer, IslandInfo> owner_islands = new HashMap<>();
    private Integer next_island = 0;


    /**
     * Constructs an OwnerGraphStructure from a graph of properties and their neighbors.
     *
     * @param property_neighbours the property graph
     */
    public OwnerGraphStructure(Graph<Property, DefaultEdge> property_neighbours) {
        this.neigbour_map = property_neighbours;
        this.graph = formGraph(property_neighbours);
        calcIslands(this.islands, this.owner_islands);
    }

    /**
     * Inserts a new owner ID into the graph if not already present.
     *
     * @param id the owner ID to insert
     * @param g  the graph to insert into
     */
    private static void insertNew(Integer id, Graph<Integer, NeighbourEdge> g) {
        if (!g.containsVertex(id)) {
            g.addVertex(id);
        }
    }

    /**
     * Constructs the owner graph based on property adjacency relationships.
     *
     * @param property_neighbours the graph of properties and their neighbors
     * @return the owner graph
     */
    private Graph<Integer, NeighbourEdge> formGraph(Graph<Property, DefaultEdge> property_neighbours) {
        SimpleGraph<Integer, NeighbourEdge> g = new SimpleGraph<>(NeighbourEdge.class);


        for (Property p : property_neighbours.vertexSet()) {
            Integer owner = p.getOwnerID();
            insertNew(owner, g);

            for (DefaultEdge e : property_neighbours.edgesOf(p)) {
                Property neighbour_prop = property_neighbours.getEdgeTarget(e);
                Integer neighbour = neighbour_prop.getOwnerID();
                if (neighbour.equals(owner)) {
                    continue;
                }
                insertNew(neighbour, g);

                boolean edgeExists = g.containsEdge(owner, neighbour) || g.containsEdge(neighbour, owner);
                if (!edgeExists) {
                    g.addEdge(owner, neighbour, new NeighbourEdge(neighbour_prop, p));
                } else {
                    NeighbourEdge edge = g.getEdge(owner, neighbour);
                    edge.addPair(neighbour_prop, p);
                }
            }
        }
        return g;
    }

    /**
     * Calculates the average area increase for a potential property exchange.
     *
     * @param ogs      the owner graph structure
     * @param exchange the property pair to exchange
     * @return a pair containing the average area increases for both owners
     */
    public static Pair<Double, Double> calcAvgAreaIncrease(OwnerGraphStructure ogs, PropertyPair exchange) {
        Property first = exchange.first;
        Property second = exchange.second;
        IslandMember member_first = new IslandMember(ogs.islands.get(first));
        IslandMember member_second = new IslandMember(ogs.islands.get(second));

        IslandInfo island_first = new IslandInfo(ogs.owner_islands.get(first.getOwnerID()));
        IslandInfo island_second = new IslandInfo(ogs.owner_islands.get(second.getOwnerID()));

        double first_avg = island_first.getAverage();
        double second_avg = island_second.getAverage();

        if (member_first.isMiddle || member_first.isLeaf) {
            island_first.count--;
            assert (island_first.count != 0);
        }
        if (member_second.isMiddle || member_second.isLeaf) {
            island_second.count--;
            assert (island_second.count != 0);
        }
        island_first.sum += -first.getShapeArea() + second.getShapeArea();
        island_second.sum += -second.getShapeArea() + first.getShapeArea();

        double first_avg_aft = island_first.getAverage();
        double second_avg_aft = island_second.getAverage();
        double first_island_icrease = first_avg_aft - first_avg;
        double second_island_increase = second_avg_aft - second_avg;
        return new Pair<>(first_island_icrease, second_island_increase);
    }

    /**
     * Calculates the fitness of exchanging two properties between owners.
     *
     * @param ogs      the owner graph structure
     * @param exchange the pair of properties to exchange
     * @return the fitness value
     */
    public static double exchangeFitness(OwnerGraphStructure ogs, PropertyPair exchange) {
        Pair<Double, Double> increase = calcAvgAreaIncrease(ogs, exchange);
        if (increase.getValue() < 0) {
            return 0;
        }
        if (increase.getKey() < 0) {
            return 0;
        }
        double difference =
                Math.pow(exchange.first.getShapeArea() - exchange.second.getShapeArea(), 2)
                        * ogs.fitness_difference_coef;
        double avgIncrease = (increase.getKey() + increase.getValue()) * ogs.fitness_increase_coef;
        double increaseFairness = Math.abs(increase.getKey() - increase.getValue());
        return avgIncrease - increaseFairness - difference;
    }

    /**
     * Comparator class for comparing PropertyPair instances based on exchange fitness.
     */
    private static class PairComparator implements Comparator<PropertyPair> {
        OwnerGraphStructure owner_graph_structure;

        /**
         * Compares two PropertyPair objects based on their fitness values.
         *
         * @param o1 the first PropertyPair to compare
         * @param o2 the second PropertyPair to compare
         * @return a negative integer, zero, or a positive integer if the fitness of o1 is less than,
         * equal to, or greater than the fitness of o2
         */
        public int compare(PropertyPair o1, PropertyPair o2) {
            double fit1 = exchangeFitness(owner_graph_structure, o1);
            double fit2 = exchangeFitness(owner_graph_structure, o2);
            return Double.compare(fit1, fit2);
        }

        /**
         * Constructs a PairComparator with the specified OwnerGraphStructure.
         *
         * @param ogs the owner graph structure used for fitness calculations
         */
        public PairComparator(OwnerGraphStructure ogs) {
            owner_graph_structure = ogs;
        }
    }

    /**
     * Sorts a list of PropertyPair instances by fitness in descending order.
     *
     * @param exchanges the list of PropertyPair objects to be sorted
     */
    public void sortExchangesByFitness(List<PropertyPair> exchanges) {
        Collections.sort(exchanges, new PairComparator(this));
        Collections.reverse(exchanges);
    }

    /**
     * Generates all possible property exchanges based on the graph structure.
     * The exchanges are sorted by fitness in descending order.
     *
     * @return a list of all possible PropertyPair exchanges
     */
    public List<PropertyPair> generateAllExchanges() {
        ArrayList<PropertyPair> result = new ArrayList<>();
        for (NeighbourEdge edge : graph.edgeSet()) {
            if (edge.incidents.size() <= 1) {
                continue;
            }
            List<PropertyPair> all_contacts = edge.incidents;
            for (int i = 0; i < all_contacts.size(); i++) {
                for (int ii = 0; ii < all_contacts.size(); ii++) {
                    if (i == ii) {
                        continue;
                    }
                    Property prop1 = all_contacts.get(i).first;
                    Property prop2 = all_contacts.get(ii).second;
                    result.add(new PropertyPair(prop1, prop2));
                    assert (prop1.getOwnerID() != prop2.getOwnerID());
                }
            }
        }
        sortExchangesByFitness(result);
        return result;
    }

    /**
     * Checks if a property is considered a "middle" property,
     * meaning it has more than one owned neighbor.
     *
     * @param prop the property to check
     * @return true if the property has more than one owned neighbor, false otherwise
     */
    private boolean checkIsMiddle(Property prop) {
        int total_count = 0;
        for (DefaultEdge e : neigbour_map.edgesOf(prop)) {
            Property other = neigbour_map.getEdgeTarget(e);
            if (other.getOwnerID() == prop.getOwnerID()) {
                total_count++;
            }
        }
        return total_count > 1;
    }

    /**
     * Searches for all neighbors of a property that are owned by the same owner,
     * excluding those in the ban list.
     *
     * @param prop     the property whose neighbors are being searched
     * @param ban_list the set of properties to exclude from the results
     * @return a list of neighboring properties owned by the same owner
     */
    private List<Property> searchOwnedNeighbours(Property prop, Set<Property> ban_list) {
        List<Property> result = new ArrayList<>();
        for (DefaultEdge e : neigbour_map.edgesOf(prop)) {
            Property other = neigbour_map.getEdgeTarget(e);
            if (other.getOwnerID() == prop.getOwnerID() && !ban_list.contains(other)) {
                result.add(other);
            }
        }
        return result;
    }

    /**
     * Calculates islands of connected properties owned by the same owner.
     * Updates the given maps with island information for each property and owner.
     *
     * @param islands       a map to store island metadata for each property
     * @param island_owners a map to store aggregated island information for each owner
     */
    private void calcIslands(Map<Property, IslandMember> islands, Map<Integer, IslandInfo> island_owners) {
        Set<Property> computed = new HashSet<>();
        for (Property p : neigbour_map.vertexSet()) {
            if (computed.contains(p)) {
                continue;
            }
            Queue<Property> to_compute = new LinkedList<>();
            to_compute.add(p);
            int cur_island = this.next_island++;
            IslandInfo info = new IslandInfo();
            while (!to_compute.isEmpty()) {

                Property current = to_compute.poll();
                if (computed.contains(current)) {
                    continue;
                }
                computed.add(current);

                IslandMember member = new IslandMember();
                member.islandID = cur_island;
                member.isMiddle = checkIsMiddle(p);

                info.count++;
                info.sum += current.getShapeArea();
                List<Property> others = searchOwnedNeighbours(current, computed);
                to_compute.addAll(others);
                if (others.isEmpty() && p == current) {
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