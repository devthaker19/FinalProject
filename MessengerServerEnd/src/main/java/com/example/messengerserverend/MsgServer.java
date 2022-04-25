package com.example.messengerserverend;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MsgServer {

    private ServerSocket myServerSocket;
    private Socket mySocket;
    private BufferedReader myReader;
    private BufferedWriter myWriter;




    public MsgServer(ServerSocket myServerSocket) {
        try {
            this.myServerSocket = myServerSocket;
            this.mySocket = myServerSocket.accept();
            this.myReader = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            this.myWriter = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
        }catch (IOException exception){
            System.out.println("Server was not created :(");
            exception.printStackTrace();
        }

    }
    public void sendClientMessage(String messageToBeSent){
        try{
            myWriter.write(messageToBeSent);
            myWriter.newLine();
            myWriter.flush();
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Could not send the message :(");
            eventCloser(mySocket,myReader,myWriter);

        }

    }

    public void gotClientMessage(VBox myVbox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mySocket.isConnected()){
                    try{

                        String toClientMessage = myReader.readLine();
                        ServerController.printMessageOnGUI(toClientMessage, myVbox);
                    }catch(IOException exception){
                        System.out.println("Could not receive the message :(");
                        exception.printStackTrace();
                        eventCloser(mySocket, myReader, myWriter);
                        break;
                    }

                }
            }
        }).start();
    }

    public void eventCloser(Socket mySocket, BufferedReader myReader, BufferedWriter myWriter){
        try{
            if (mySocket != null){
                mySocket.close();
            }
            if (myReader != null){
                myReader.close();
            }
            if(myWriter != null){
                myWriter.close();
            }

        }catch(IOException exception){
            exception.printStackTrace();
        }

    }
}
