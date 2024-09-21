module com.trimula.dircomp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;
    requires jdk.compiler;


    opens com.trimula.dircomp to javafx.fxml;
    exports com.trimula.dircomp;
}