package Client;

import UI.UserInterface;
import Utils.BlackCardGenerator;
import javafx.scene.control.Label;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{

    private String user;
    private String roomPass;
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private UserInterface userInterface;

    private boolean connected = false;
    private boolean isJudging = false;

    private BlackCardGenerator generator;


    public Client(String username, String roomPass, String ip, UserInterface userInterface, BlackCardGenerator generator){
        this.roomPass = roomPass;
        this.user = username;
        this.userInterface = userInterface;
        this.generator = generator;
        try{
            this.socket = new Socket(ip, 8080);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendCommand("login;"+user+";"+roomPass);

            connected = true;

            //Create a client handler to handle all incoming commands.
            ClientHandler clientHandler = new ClientHandler(this, in);
            Thread thread = new Thread(clientHandler);
            thread.start();
            

        }catch (IOException i){
            connected = false;
        }
    }

    public synchronized String getUserName(){
        return this.user;
    }

    public synchronized boolean getConnected(){
        return connected;
    }

    public synchronized void sendCommand(String command){
        out.println(command);
    }

    public synchronized void setJudging(String judging){
        this.userInterface.judging(judging);
    }

    public synchronized void setSubmitting(String question){
        userInterface.submitStage(question);
    }

    public synchronized void submitCard(String card, Label label){
        if (userInterface.getCanSubmit()) {
            this.sendCommand("submit;" + card);
            label.setText(generator.getRandomCard());
            this.userInterface.turnOffSubmit();
        }
    }


}