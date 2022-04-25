package com.example.messengerclientend;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class ClientController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;




    @FXML
    private AnchorPane UI_Anchor;

    @FXML
    private VBox Vbox_Message;

    @FXML
    private ScrollPane scrollMessages;

    @FXML
    private Button sendButton;

    @FXML
    private TextField sendMessage;

    @FXML
    private Button tictactoe;


    private MsgClient myClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            myClient =  new MsgClient(new Socket("localhost",2020 ));
            System.out.println("Successful connection with the server!");
        } catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Could not connect with the server");
        }

        Vbox_Message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollMessages.setVvalue((Double) t1);

            }
        });

        myClient.gotServerMessage(Vbox_Message);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToBeSent = sendMessage.getText();
                if (!messageToBeSent.isEmpty()){
                    HBox myHBox = new HBox();
                    myHBox.setAlignment(Pos.CENTER_RIGHT);
                    myHBox.setPadding(new Insets(7,7,7,10));
                    Text myText = new Text(messageToBeSent);
                    TextFlow myTextFlow = new TextFlow(myText);

                    myTextFlow.setStyle("-fx-background-color: #dddddd;");
                    myTextFlow.setPadding(new Insets(7,10,7,10));
                    myText.setFill(Paint.valueOf("red"));

                    myHBox.getChildren().add(myTextFlow);
                    Vbox_Message.getChildren().add(myHBox);

                    myClient.sendServerMessage(messageToBeSent);
                    sendMessage.clear();
                }
            }
        });

    }

    public static void printMessageOnGUI(String serverMessage, VBox myVbox){
        HBox myHbox = new HBox();
        myHbox.setAlignment(Pos.CENTER_LEFT);
        myHbox.setPadding(new Insets(7,7,7,10));

        Text myText = new Text(serverMessage);
        TextFlow myTextFlow = new TextFlow(myText);
        myTextFlow.setStyle("-fx-color:#000000;");
        myHbox.getChildren().add(myTextFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myVbox.getChildren().add(myHbox);
            }
        });

    }


    @FXML
    public void changetoAbout(ActionEvent e) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("About.fxml"));
        Stage aboutStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene aboutScene = new Scene(root);
        aboutStage.setScene(aboutScene);
        aboutStage.show();
    }
    @FXML
    public void changetoLight(ActionEvent e) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("UI.fxml"));
        Stage aboutStage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene aboutScene = new Scene(root);
        aboutStage.setScene(aboutScene);
        aboutStage.show();
    }
}
