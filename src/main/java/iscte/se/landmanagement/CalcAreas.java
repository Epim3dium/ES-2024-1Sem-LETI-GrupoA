package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

        GraphStructure g= new GraphStructure(propFileReader.getProperties(),1);

        CalcAreas c=new CalcAreas(g.getG());
        System.out.println(c.getAvgArea3("Calheta"));

    }





    private Graph<Property, DefaultEdge> graph;


    public CalcAreas(Graph<Property, DefaultEdge> graph) {
        this.graph = graph;

    }


    public double getAvgArea3(String areaT) {
        double sum = 0;
        double count = 0;
        for (Property p : graph.vertexSet()) {
            if (p.getParish().equals(areaT) || p.getMunicipality().equals(areaT) || p.getIsland().equals(areaT)) {
                sum += p.getShapeArea();
                count++;
            }
        }
        System.out.println(sum);
        System.out.println(count);
        return sum / count;
    }



    public double getArea4(String areaT) {
        List<DefaultEdge> edges= getNeighbours(areaT);
        List<Property> sameIdVertices = mergeEdges(edges);
        double sum=0;
        double count=0;
        for (Property p : graph.vertexSet()) {
            if(!edges.contains(p)) {
                sum += p.getShapeArea();
                count++;
            }
        }
        double avgArea1=sum/count;
        double sum2=0;
        double count2=0;
        for (Property p : sameIdVertices) {
            sum2+=p.getShapeArea();
            count2++;
        }
        double avgArea2=sum2/count2;

        return avgArea1+avgArea2;
    }

    public List<DefaultEdge> getNeighbours(String areaT) {
        Map<Integer, List<Property>> groupedVertices = graph.vertexSet().stream().collect(Collectors.groupingBy(Property::getOwnerID));

        List<DefaultEdge> edgesWithSameIdVertices = new ArrayList<>();
        for (Map.Entry<Integer, List<Property>> entry : groupedVertices.entrySet()) {
            List<Property> sameIdVertices = entry.getValue();
            for (Property v : sameIdVertices) {
                for (Property u : sameIdVertices) {
                    if (graph.containsEdge(v, u)) {
                        if(v.getIsland().equals(u.getIsland()) && v.getIsland().equals(areaT)) {
                            edgesWithSameIdVertices.add(graph.getEdge(v, u));
                        }

                    }
                }
            }
        }

        return edgesWithSameIdVertices;
    }

    private List<Property> mergeEdges(List<DefaultEdge> edges) {
        List<Property> sameIdVertices = new ArrayList<>();
        for (DefaultEdge edge : edges) {
            double AreaSum=graph.getEdgeSource(edge).getShapeArea()+graph.getEdgeTarget(edge).getShapeArea();
            Property p = new Property(graph.getEdgeSource(edge).getOwnerID(),AreaSum);
            sameIdVertices.add(p);
        }
        return sameIdVertices;
    }
}
