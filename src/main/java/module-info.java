module com.tiketin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    
    opens com.tiketin to javafx.fxml;
   // opens com.tiketin.controller to javafx.fxml;
    opens com.tiketin.model to javafx.base;
    exports com.tiketin;
    exports com.tiketin.component;
}
