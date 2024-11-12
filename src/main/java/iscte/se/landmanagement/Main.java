package iscte.se.landmanagement;

import iscte.se.landmanagement.PropertiesFileReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!\n");

        PropertiesFileReader prop_reader = new PropertiesFileReader("src/main/resources/Madeira-Moodle.csv");
        prop_reader.readPropertiesFile();
        System.out.printf(Integer.toString(prop_reader.getProperties().get(0).getPropertyID()) + "\n");
        System.out.printf(Integer.toString(prop_reader.getProperties().get(0).getOwnerID()));
    }
}