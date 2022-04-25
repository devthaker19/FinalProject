module com.example.messengerclientend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.messengerclientend to javafx.fxml;
    exports com.example.messengerclientend;
}