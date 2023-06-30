module com.example.qizil {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.qizil.Model to javafx.base;
    opens com.example.qizil.Controllers to javafx.fxml;
    exports com.example.qizil;
    exports com.example.qizil.Controllers;
}