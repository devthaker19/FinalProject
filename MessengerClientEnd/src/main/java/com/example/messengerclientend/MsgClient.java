package com.example.messengerclientend;

import javafx.scene.layout.VBox;

import java.io.*;

import java.net.Socket;

public class MsgClient {

    private Socket myClientSocket;
    private BufferedReader myReader;
    private BufferedWriter myWriter;
    public MsgClient(Socket myClientSocket) {
        try {

            this.myClientSocket = myClientSocket;
            this.myReader = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
            this.myWriter = new BufferedWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));
        }catch (IOException exception){
            System.out.println("Could not create Client :(");
            exception.printStackTrace();
            eventCloser(myClientSocket, myReader, myWriter);
        }

    }

    public void sendServerMessage(String messageToBeSent){

        try{
            myWriter.write(messageToBeSent);
            myWriter.newLine();
            myWriter.flush();
        }catch(IOException exception){
            exception.printStackTrace();
            System.out.println("Could not send the message :(");
            eventCloser(myClientSocket,myReader,myWriter);

        }

    }
    public void gotServerMessage(VBox myVbox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (myClientSocket.isConnected()){
                    try{
                        String toServerMessage = myReader.readLine();
                        ClientController.printMessageOnGUI(toServerMessage, myVbox);
                    }catch(IOException exception){
                        System.out.println("Could not receive the message :(");
                        exception.printStackTrace();
                        eventCloser(myClientSocket, myReader, myWriter);
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
