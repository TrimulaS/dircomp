module com.trimula.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.trimula.demo to javafx.fxml;
    exports com.trimula.demo;
}