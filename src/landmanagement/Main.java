import iscte.se.landmanagement.PropertiesFileReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");

        PropertiesFileReader prop_reader = new PropertiesFileReader("src/main/resources/Madeira-Moodle.csv");
        prop_reader.readPropertiesFile();
    }
}