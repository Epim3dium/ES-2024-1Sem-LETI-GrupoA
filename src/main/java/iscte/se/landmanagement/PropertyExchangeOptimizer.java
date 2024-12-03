package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PropertyExchangeOptimizer {

    private Graph<Property, DefaultEdge> graph;
    private int ownerID;  // Store the target owner ID

    // Constructor now takes ownerID as a parameter
    public PropertyExchangeOptimizer(Graph<Property, DefaultEdge> graph, int ownerID) {
        this.graph = graph;
        this.ownerID = ownerID;
    }

    /**
     * Generates a list of property exchange suggestions to maximize the average area per owner.
     *
     * @return List of suggestions, each as a string describing the proposed exchange.
     */
    public List<String> generateExchangeSuggestions() {
        List<String> suggestions = new ArrayList<>();
        Set<Property> allProperties = graph.vertexSet();
        int i = 0;

        // Filter properties by ownerID
        Set<Property> propertiesByOwner = new HashSet<>();
        for (Property property : allProperties) {
            if (property.getOwnerID() == ownerID) {
                propertiesByOwner.add(property);
            }
        }

        // Only consider properties owned by the specified ownerID
        for (Property p1 : propertiesByOwner) {
            for (Property p2 : allProperties) {
                if (p1.equals(p2) || p1.getOwnerID() == p2.getOwnerID()) continue;

                double currentAvgArea = calcAverageArea();
                performExchange(p1, p2);
                double newAvgArea = calcAverageArea();
                i++;
                if (newAvgArea > currentAvgArea && isExchangeViable(p1, p2)) {

                    System.out.print("*******FEito " + i + "**************");
                    suggestions.add(String.format(
                            "Suggest exchange: Property %d (Owner %d) ↔ Property %d (Owner %d) [Area Difference: %.2f]",
                            p1.getPropertyID(), p1.getOwnerID(),
                            p2.getPropertyID(), p2.getOwnerID(),
                            Math.abs(p1.getShapeArea() - p2.getShapeArea())
                    ));
                }
                // Undo the exchange for next evaluation
                undoExchange(p1, p2);
            }
        }
        return suggestions;
    }

    /**
     * Perform a hypothetical exchange of properties between their owners.
     */
    private void performExchange(Property p1, Property p2) {
        int tempOwner = p1.getOwnerID();
        p1.setOwnerID(p2.getOwnerID());
        p2.setOwnerID(tempOwner);
    }

    /**
     * Undo the hypothetical exchange of properties.
     */
    private void undoExchange(Property p1, Property p2) {
        performExchange(p1, p2); // Revert the owners back
    }

    /**
     * Calculate the average area per owner based on connected components with the same owner.
     */
    private double calcAverageArea() {
        Set<Property> visited = new HashSet<>();
        List<Double> aggregatedArea = new ArrayList<>();

        for (Property property : graph.vertexSet()) {
            if (!visited.contains(property)) {
                double totalA = exploreConnectedComp(graph, property, visited);
                aggregatedArea.add(totalA);
            }
        }
        return aggregatedArea.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Explore a connected component of properties with the same owner.
     */
    private double exploreConnectedComp(Graph<Property, DefaultEdge> graph, Property property, Set<Property> visited) {
        double totalArea = 0;
        Queue<Property> queue = new LinkedList<>();
        queue.add(property);
        visited.add(property);

        while (!queue.isEmpty()) {
            Property p = queue.poll();
            totalArea += p.getShapeArea();

            for (DefaultEdge edge : graph.edgesOf(p)) {
                Property neighbor = graph.getEdgeTarget(edge);
                if (!visited.contains(neighbor) && neighbor.getOwnerID() == p.getOwnerID()) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return totalArea;
    }

    /**
     * Determines if the exchange is viable based on the similarity of property areas.
     *
     * @param p1 First property
     * @param p2 Second property
     * @return True if the exchange is viable, false otherwise.
     */
    private boolean isExchangeViable(Property p1, Property p2) {
        double areaDiff = Math.abs(p1.getShapeArea() - p2.getShapeArea());
        return areaDiff / Math.min(p1.getShapeArea(), p2.getShapeArea()) <= 0.2; // Example: max 20% difference
    }

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
        System.out.println("Converti propriedades-------------------------------------------");
        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 4);
        System.out.println("Grafo criado-------------------------------------------");

        // Pass the target ownerID as a parameter (e.g., ownerID = 123)
        int targetOwnerID = 123; // Example, replace with the desired ownerID
        PropertyExchangeOptimizer optimizer = new PropertyExchangeOptimizer(g.getG(), targetOwnerID);

        System.out.println("sugestões criadas-------------------------------------------");
        List<String> suggestions = optimizer.generateExchangeSuggestions();
        System.out.println("sugestões finalizadas-------------------------------------------");

        System.out.println("Exchange Suggestions:");
        suggestions.forEach(System.out::println);
    }
}
