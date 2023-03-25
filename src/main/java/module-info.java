module com.example.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetwork to javafx.fxml;
    exports com.example.socialnetwork;
    exports com.example.socialnetwork.Domain;
    exports com.example.socialnetwork.Domain.validators;
    exports com.example.socialnetwork.UserInterface;
    exports com.example.socialnetwork.Service;
    exports com.example.socialnetwork.Controller;
 //   opens com.example.socialnetwork.Controller to javafx.fxml;
}