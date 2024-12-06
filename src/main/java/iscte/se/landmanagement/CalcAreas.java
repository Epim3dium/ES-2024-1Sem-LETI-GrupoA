package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CalcAreas {

//    public static void main(String[] args) throws Exception {
//
//        URL url = Thread.currentThread().getContextClassLoader().getResource("Madeira-Moodle-1.1.csv");
//        if (url == null) {
//            System.out.println("Arquivo CSV não encontrado!");
//            return;
//        }
//
//        Path path = Paths.get(url.toURI());
//        PropFileReader propFileReader = new PropFileReader(path);
//
//        propFileReader.readFile();
//        propFileReader.convertToPropertiy();
//
//        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 4);
//
//        CalcAreas c = new CalcAreas(g.getG());
//        System.out.println(c.calcArea3("Jardim do Mar", "Freguesia"));
//        System.out.println(c.calcArea4("Jardim do Mar", "Freguesia"));
//
//    }


    private GraphStructure g;
    private Graph<Property, DefaultEdge> graph;


    public CalcAreas(Graph<Property, DefaultEdge> graph) {
        this.graph = graph;

    }

    public CalcAreas(GraphStructure g) {
        this.g = g;
        this.graph=g.getG();
    }

   public ArrayList<Property> toList(Graph<Property, DefaultEdge> graph) {
        ArrayList<Property> result = new ArrayList<>(graph.vertexSet());
       return result;
   }

    /**
     * Calculates the Average Area of {@link Property} of a geographic area indicated by the user
     *
     * @param areaT    Name of the area that will be calculated
     * @param areaType Type of the area that will be calculated (Parish/Municipality/Island)
     * @return The Average Area of the {@link Property} considered in the case
     */
    public double calcArea3(String areaT, String areaType) {

        List<Property> filteredProperties = graph.vertexSet().stream().filter(property -> matchesLocal(property, areaT, areaType)).toList();


        if(filteredProperties.isEmpty()) {
            return 0;
        }
        double sum = 0;



        for (Property p : filteredProperties) {
            System.out.println(p.getShapeArea());
            sum += p.getShapeArea();

        }


        return sum / filteredProperties.size();
    }

    /**
     * Calculates the Average Area of {@link Property} of  a geographic area indicated by user
     * considering that if adjacent properties have the same owner, they are just one
     *
     * @param input    Name of the area that will be calculated
     * @param areaType Type of the area that will be calculated (Parish/Municipality/Island)
     * @return The Average Area of the {@link Property} considered in the case
     */

    public double calcArea4(String input, String areaType) {
        List<Property> filteredProperties = graph.vertexSet().stream().filter(property -> matchesLocal(property, input, areaType)).toList();
        Set<Property> visited = new HashSet<>();
        List<Double> aggregatedArea = new ArrayList<>();

        for (Property property : filteredProperties) {
            if (!visited.contains(property)) {
                double totalA = exploreConnectedComp(graph, property, visited);
                aggregatedArea.add(totalA);

            }
        }

        double avg = aggregatedArea.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return avg;
    }

    private boolean matchesLocal(Property property, String areaT, String type) {
        return switch (type) {
            case "Freguesia" -> property.getParish().equals(areaT);
            case "Municipio" -> property.getMunicipality().equals(areaT);
            case "Ilha" -> property.getIsland().equals(areaT);
            default -> false;
        };

    }

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

    public GraphStructure changeProperty(Property t,Property s, int ns, int mt) {
        ArrayList<Property> n=toList(graph);
        for(Property i:n){
            if(i.equals(t)){
                i.setOwnerID(ns);
            } else if (i.equals(s)) {
                i.setOwnerID(mt);
            }

        }
        return new GraphStructure(n,4);

    }

}
