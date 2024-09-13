module com.trimula.dircomp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.trimula.dircomp to javafx.fxml;
    exports com.trimula.dircomp;
}