package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CalcAreas {

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

        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 1);

        CalcAreas c = new CalcAreas(g.getG());
        System.out.println(c.calcArea3("Ponta do Sol","Freguesia"));
        System.out.println(c.calcArea4("Ponta do Sol","Freguesia"));

    }


    private Graph<Property, DefaultEdge> graph;


    public CalcAreas(Graph<Property, DefaultEdge> graph) {
        this.graph = graph;

    }


    public double calcArea3(String areaT, String areaType) {
        List<Property> filteredProperties=graph.vertexSet().stream().filter(property -> matchesLocal(property,areaT, areaType)).toList();
        double sum = 0;

        for (Property p : filteredProperties) {
            sum += p.getShapeArea();

        }
        return sum / filteredProperties.size();
    }

   public double calcArea4(String input, String areaType) {
        List<Property> filteredProperties=graph.vertexSet().stream().filter(property -> matchesLocal(property,input, areaType)).toList();
        Set<Property> visited=new HashSet<>();
        List<Double> aggregatedArea=new ArrayList<>();

        for(Property property:filteredProperties){
            if(!visited.contains(property)){
                double totalA=exploreConnectedComp(graph,property,visited);
                aggregatedArea.add(totalA);

            }
        }

        double avg=aggregatedArea.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return avg;
    }

    private boolean matchesLocal(Property property, String areaT, String type) {
         switch(type){
             case "Freguesia":
                 return property.getParish().equals(areaT);
             case "Municipio":
                 return property.getMunicipality().equals(areaT);
             case "Ilha":
                 return property.getIsland().equals(areaT);
             default:
                 return false;
         }

    }

    private double exploreConnectedComp(Graph<Property, DefaultEdge> graph, Property property, Set<Property> visited) {
        double totalArea=0;
        Queue<Property> queue=new LinkedList<>();
        queue.add(property);
        visited.add(property);

        while (!queue.isEmpty()) {
            Property p = queue.poll();
            totalArea+=p.getShapeArea();

            for(DefaultEdge edge: graph.edgesOf(p)){
                Property neighbor=graph.getEdgeTarget(edge);
//                if(neighbor.equals(p)){
//                    neighbor=graph.getEdgeSource(edge);
//                }
                if(!visited.contains(neighbor) && neighbor.getOwnerID() == p.getOwnerID()){
                    visited.add(neighbor);
                    queue.add(neighbor);

                }
            }
        }

        return totalArea;
    }


}
