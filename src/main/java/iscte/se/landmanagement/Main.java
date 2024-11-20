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

        System.out.printf(Integer.toString(propFileReader.getProperties().get(0).getPropertyID()) + "\n");
        System.out.printf(Integer.toString(propFileReader.getProperties().get(0).getOwnerID()));
    }
}