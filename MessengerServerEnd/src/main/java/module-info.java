module com.example.messengerserverend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.messengerserverend to javafx.fxml;
    exports com.example.messengerserverend;
}