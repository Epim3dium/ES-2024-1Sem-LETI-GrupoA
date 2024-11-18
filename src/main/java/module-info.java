module org.openjfx.es20241semletigrupoa {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.jgrapht.core;

    opens org.openjfx.es20241semletigrupoa to javafx.fxml;
    exports org.openjfx.es20241semletigrupoa;
}