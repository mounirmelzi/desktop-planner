module com.example.planner {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;


    opens com.example.planner to javafx.fxml;
    exports com.example.planner;
    exports com.example.controllers;
    exports com.example.core;
    opens com.example.controllers to javafx.fxml;
    exports com.example.controllers.auth;
    opens com.example.controllers.auth to javafx.fxml;
    exports com.example.core.exceptions;
    exports com.example.core.utils;
    exports com.example.controllers.historique;
    opens com.example.controllers.historique to javafx.fxml;
    exports com.example.controllers.calendar;
    opens com.example.controllers.calendar to javafx.fxml;
    exports com.example.controllers.home;
    opens com.example.controllers.home to javafx.fxml;
    exports com.example.controllers.tache;
    opens com.example.controllers.tache to javafx.fxml;
    exports com.example.controllers.project;
    opens com.example.controllers.project to javafx.fxml;
    exports com.example.controllers.profile;
    opens com.example.controllers.profile to javafx.fxml;
}