package iscte.se.landmanagement;

import iscte.se.landmanagement.PropertiesFileReader;

import java.io.InputStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("test.csv");
        if (is == null) {
            System.out.println("Erro: O ficheiro Madeira-Moodle.csv não foi encontrado no classpath.");
            return;
        }
        System.out.println("Hello and welcome!");

        PropertiesFileReader prop_reader = new PropertiesFileReader(is);
        prop_reader.readPropertiesFile();
        System.out.printf(Integer.toString(prop_reader.getProperties().get(0).getPropertyID()) + "\n");
        System.out.printf(Integer.toString(prop_reader.getProperties().get(0).getOwnerID()));
    }
}