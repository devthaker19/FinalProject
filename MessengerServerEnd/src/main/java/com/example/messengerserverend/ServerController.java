package com.example.messengerserverend;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;


public class ServerController implements Initializable {
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

    private MsgServer myServer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            myServer = new MsgServer (new ServerSocket(2020));

        }catch (IOException exception){
            exception.printStackTrace();
            System.out.println("Server was not created :(");
        }

        Vbox_Message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollMessages.setVvalue((Double) t1);
            }

        });


        myServer.gotClientMessage(Vbox_Message);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToBeSent = sendMessage.getText();
                if (!messageToBeSent.isEmpty()){
                    HBox myHbox = new HBox();
                    myHbox.setAlignment(Pos.CENTER_RIGHT);
                    myHbox.setPadding(new Insets(7,7,7,14));

                    Text textMessage = new Text (messageToBeSent);
                    TextFlow textMessageFlow = new TextFlow(textMessage);

                    //if possible change the color codes
                    textMessageFlow.setStyle("-fx-background-color: #dddddd;");
                    textMessageFlow.setPadding(new Insets(7,10,7,10));
                    textMessage.setFill(Paint.valueOf("blue"));

                    myHbox.getChildren().add(textMessageFlow);
                    Vbox_Message.getChildren().add(myHbox);

                    myServer.sendClientMessage(messageToBeSent);
                    sendMessage.clear();
                }
            }
        });


    }

    public static void printMessageOnGUI(String clientMessage, VBox myVbox){
        HBox myHbox = new HBox();
        myHbox.setAlignment(Pos.CENTER_LEFT);
        myHbox.setPadding(new Insets(7,7,7,10));

        Text myText = new Text(clientMessage);
        TextFlow myTextFlow = new TextFlow(myText);
        myTextFlow.setStyle("-fx-color:#FF3547;");
        myTextFlow.setPadding(new Insets(7,10,7,10));
        myHbox.getChildren().add(myTextFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myVbox.getChildren().add(myHbox);
            }
        });


    }
}
