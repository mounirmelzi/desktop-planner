module com.example.planner {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.planner to javafx.fxml;
    exports com.example.planner;
}