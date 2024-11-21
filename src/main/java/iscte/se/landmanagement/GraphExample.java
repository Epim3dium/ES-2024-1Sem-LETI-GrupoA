package iscte.se.landmanagement;


import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GraphExample {

    private Graph<Property, DefaultEdge> graph;
    private ArrayList<Property> properties;
    private int threshold;

    public static void main(String[] args) throws IOException {
        PropertiesFileReader p=new PropertiesFileReader("src/main/resources/Madeira-Moodle.csv");
//        System.out.println(p.getProperties().get(0).getCorners().size());
//        for(Coordinates c : p.getProperties().get(0).getCorners()){
//
//            System.out.println(c);
//        }


        GraphExample g= new GraphExample(p.getProperties());
        //givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(g.graph);
//        saveGraphAsImage(g.getG(),"src/main/resources");
//
//
//        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>((g.getG()));
//
//        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new mxGraphComponent(graphAdapter));
//        frame.setSize(400, 400);
//        frame.setVisible(true);





    }


    public GraphExample(ArrayList<Property> properties) {
        this.properties = properties;
        this.threshold=defineThreshold();
        this.graph = formGraph();

    }

    public Graph<Property,DefaultEdge> getG(){
        return this.graph;
    }


    private Graph<Property, DefaultEdge> formGraph() {
        Graph<Property, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        for (Property property : properties) {
            g.addVertex(property);
        }
        for (int i = 0; i < properties.size(); i++) {
            for (int j = i + 1; j < properties.size(); j++) {
                Property p1 = properties.get(i);
                Property p2 = properties.get(j);

                // Verificar se são adjacentes
                if (areAdjacentByDistance(p1, p2, threshold)) {
                    g.addEdge(p1, p2);
                }
            }
        }
        System.out.println(g.vertexSet());
        System.out.println(g.edgeSet());
        return g;
    }

    public static boolean areAdjacentByDistance(Property p1, Property p2, double threshold) {
        List<Coordinates> corners1 = p1.getCorners();
        List<Coordinates> corners2 = p2.getCorners();

        // Iterar sobre todos os pares de vértices
        for (Coordinates c1 : corners1) {
            for (Coordinates c2 : corners2) {
                double distance = calculateDistance(c1, c2);
                if (distance <= threshold) {
                    return true; // Terrenos são adjacentes
                }
            }
        }
        return false; // Não são adjacentes
    }

    private static double calculateDistance(Coordinates c1, Coordinates c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        return Math.sqrt(dx * dx + dy * dy); // Distância Euclidiana
    }

    private int defineThreshold() {
        return 2;
    }

    public void givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist(Graph<Property, DefaultEdge> g) throws IOException {

        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<Property, DefaultEdge>(g);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }

    // Save the graph as an image
    private static void saveGraphAsImage(Graph<Property, DefaultEdge> graph, String outputPath) throws IOException {
        JGraphXAdapter<Property, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // Render the graph to an image
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(outputPath);

        // Write the image to the file
        ImageIO.write(image, "PNG", imgFile);

        // Confirm the file creation
        if (imgFile.exists()) {
            System.out.println("Graph image saved at: " + imgFile.getAbsolutePath());
        } else {
            System.err.println("Failed to save graph image.");
        }


    }






}
