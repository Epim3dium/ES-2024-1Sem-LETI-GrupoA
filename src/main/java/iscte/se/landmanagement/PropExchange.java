package iscte.se.landmanagement;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropExchange {

    OwnerGraphStructure ownerG;
    CalcAreas calcArea;


    public static void main(String[] args) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("Madeira-Moodle-1.1.csv");
        if (url == null) {
            System.out.println("Arquivo CSV não encontrado!");
            return;
        }
        Path path = Paths.get(url.toURI());
        PropFileReader reader = new PropFileReader(path);
        reader.readFile();
        reader.convertToPropertiy();

        PropExchange p=new PropExchange(new OwnerGraphStructure(new GraphStructure(reader.getProperties(),20).getG()));


        System.out.println(p.ManageUser(4));

    }

    public PropExchange(OwnerGraphStructure ownerG) {
        this.calcArea=new CalcAreas(ownerG.getProperties());
        this.ownerG = ownerG;

    }

    public HashMap<DefaultEdge, HashMap<Property, Property>> ManageUser(int ownerID) throws Exception {
        List<DefaultEdge> e = ownerG.getGraph().edgesOf(ownerID).stream().toList();
        HashMap<Integer, List<Property>> propertiesS = locateN(ownerID, ownerG.getProperties());
        HashMap<Integer, List<Property>> propertiesT;
        HashMap<DefaultEdge, HashMap<Property, Property>> fina = new HashMap<>();
        for (DefaultEdge e1 : e) {
            System.out.println(e1);
            int s = ownerG.getGraph().getEdgeSource(e1);
            int t = ownerG.getGraph().getEdgeTarget(e1);
            if (s == ownerID) {
                propertiesT = locateN(t, ownerG.getProperties());
//                for(Integer i: propertiesT.keySet()){
//                    System.out.println(i.toString() + "+" + propertiesT.get(i).toString());
//                }

                HashMap<Property, Property> d = assertExchange(propertiesS, propertiesT);
                //System.out.println(d);
                if (d != null) {
                    fina.putIfAbsent(e1, d);
                    //System.out.println("fina "+fina);


                }

            }

        }
        //System.out.println("fina "+fina);
        verifyF(fina);
        return fina;
    }

    private void verifyF(HashMap<DefaultEdge, HashMap<Property, Property>> fina) {
        for(DefaultEdge e: fina.keySet()) {
            HashMap<Property, Property> d = fina.get(e);
            for(Property p:d.keySet()) {
                System.out.println(p.getOwnerID() + "+" + d.get(p).getOwnerID());
            }
        }
    }

    private HashMap<Property,Property> assertExchange(HashMap<Integer, List<Property>> propertiesS, HashMap<Integer, List<Property>> propertiesT) throws Exception {
        HashMap<Property,Property> exchange = new HashMap<>();
        for(Integer i: propertiesS.keySet()){
            for(Property p: propertiesS.get(i)){
                if(propertiesT.containsKey(p.getPropertyID())){
                    System.out.println(String.valueOf(i) + "+" + p + "+" + propertiesT.get(p.getPropertyID()));
                    if(assertAreas(p,getP(propertiesT.get(p.getPropertyID()),i))){


                        exchange.putIfAbsent(p,getP(propertiesT.get(p.getPropertyID()),i));

                    }
                }
            }
        }

        if(exchange.isEmpty()){
            return null;
        }

        return exchange;
    }

    private Property getP(List<Property> properties, Integer i) {
        for(Property p: properties){
            if(p.getPropertyID()==i){
                return p;
            }
        }
        return null;
    }

    private boolean assertAreas(Property t, Property s) throws Exception {
        int ns=s.getOwnerID();
        int mt=t.getOwnerID();
       // HashMap<Property,Property> H=new HashMap<>();


        if(t.getMunicipality().equals(s.getMunicipality())){
            if(t.getParish().equals(s.getParish())){
                //System.out.println("Municipality: "+t.getMunicipality() + "  Parish: " + t.getParish());
                double areaB = calcArea.calcArea4(t.getParish(),"Freguesia");
                //double areaA = calcArea.calcArea4(t.getParish(),"Freguesia");
                double areaA = performNewStruct(t,s,ns,mt,t.getParish(),"Freguesia");
                //System.out.println("areaA: "+areaA);
                //System.out.println("areaB: "+areaB);


                if(areaA>areaB){

                    if(reasonable(t.getShapeArea(),s.getShapeArea())){
                        System.out.println("ggg");
                        return true;
                        //H.put(s,t);
                    }
                }
            }
        }

        return false;


    }

    private double performNewStruct(Property t, Property s, int ns, int mt, String in, String type) throws Exception {
        Property tt=new Property(t.getPropertyID(),t.getParcelID(),t.getParcelNum(),t.getShapeLength(),t.getShapeArea(),t.getCorners(),s.getOwnerID(),t.getParish(),t.getMunicipality(),t.getIsland());
        Property ss=new Property(s.getPropertyID(),s.getParcelID(),s.getParcelNum(),s.getShapeLength(),s.getShapeArea(),s.getCorners(),t.getOwnerID(),s.getParish(),s.getMunicipality(),s.getIsland());
        Graph<Property,DefaultEdge> ne= ownerG.getProperties();

        ne.removeVertex(t);
        ne.addVertex(tt);

        ne.removeVertex(s);
        ne.addVertex(ss);

        CalcAreas a=new CalcAreas(ne);
        return a.calcArea4(in,type);
    }

    private boolean reasonable(double shapeArea, double shapeArea1) {
        System.out.println(shapeArea + "+" + shapeArea1);
        if(Math.abs(shapeArea-shapeArea1)>2000){
            return false;
        }
        return true;
    }

    private HashMap<Integer,List<Property>> locateN(int ownerID, Graph<Property, DefaultEdge> properties) {
        HashMap<Integer,List<Property>> p=new HashMap<>();
        for(DefaultEdge e: properties.edgeSet()){
            if(properties.getEdgeSource(e).getOwnerID()==ownerID) {
                p.putIfAbsent(properties.getEdgeSource(e).getPropertyID(),properties.getEdgeSource(e).getNeighbours());
            } else if (properties.getEdgeTarget(e).getOwnerID()==ownerID) {
                p.putIfAbsent(properties.getEdgeTarget(e).getPropertyID(),properties.getEdgeTarget(e).getNeighbours());
            }
        }
        return p;
    }

}
