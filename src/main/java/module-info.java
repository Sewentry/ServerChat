module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controllers;
    opens org.example.controllers to javafx.fxml;
    exports org.example.server;
    opens org.example.server to javafx.fxml;
    exports org.example.server.models;
    opens org.example.server.models to javafx.fxml;
}
