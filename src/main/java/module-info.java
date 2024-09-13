module com.trimula.dircomp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.trimula.dircomp to javafx.fxml;
    exports com.trimula.dircomp;
}