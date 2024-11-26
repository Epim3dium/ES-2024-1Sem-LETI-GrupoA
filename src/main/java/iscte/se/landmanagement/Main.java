package iscte.se.landmanagement;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("test.csv");
        if (url == null) {
            System.out.println("Arquivo CSV não encontrado!");
            return;
        }

        Path path = Paths.get(url.toURI());
        PropFileReader propFileReader = new PropFileReader(path);

        System.out.println("Hello and welcome!");


       propFileReader.readFile();
       propFileReader.convertToPropertiy();
        System.out.printf("PropertyID: " + Integer.toString(propFileReader.getProperties().get(1).getPropertyID()) + "\t");
        System.out.printf("OwnerID: "+  Integer.toString(propFileReader.getProperties().get(1).getOwnerID())+"\n");
        System.out.printf("Island: " + propFileReader.getProperties().get(1).getIsland() + "\t");
        System.out.print("Parish: " + propFileReader.getProperties().get(1).getParish());

        GraphStructure g = new GraphStructure(propFileReader.getProperties(), 1);
    }
}