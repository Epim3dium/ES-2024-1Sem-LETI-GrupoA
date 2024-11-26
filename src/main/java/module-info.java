module org.openjfx.es20241semletigrupoa {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    requires org.jgrapht.ext;
    requires org.locationtech.jts;
    requires com.opencsv;

    opens org.openjfx.es20241semletigrupoa to javafx.fxml;
    exports org.openjfx.es20241semletigrupoa;
}