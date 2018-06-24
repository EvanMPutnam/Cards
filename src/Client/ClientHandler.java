package Client;

import Server.User;
import UI.ChooseQuestion;
import UI.UserInterface;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClientHandler implements Runnable {


    private Client client;
    private BufferedReader in;
    private boolean running = true;

    private ArrayList<String> cardsToJudge;

    public ClientHandler(Client client, BufferedReader in){
        this.client = client;
        this.in = in;
        this.cardsToJudge = new ArrayList<>();
    }

    private synchronized void toggleRunning(){
        running = false;
    }

    private static synchronized String strHelper(String[] words){
        int count = 1;
        String s = "";
        for (int i = 3; i < words.length; i++){
            s += (String.valueOf(count)+": ("+words[i]+")\n");
            count += 1;
        }
        return s;
    }

    //Handles all incoming requests...
    public void run(){
        String input;
        try {
            while ((input = in.readLine()) != null && running) {
                System.out.println(input);
                String[] line = input.split(";");
                if (input.equals("quit")){
                    break;
                }else if(line[0].equals("stage") && line.length >= 4 && line[1].equals("results")){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Winning card");
                            alert.setHeaderText("Winner: "+line[2]);
                            alert.setContentText(strHelper(line));
                            alert.show();
                        }
                    });
                }else if (line[0].equals("stage") && line.length == 4 && line[2].equals(client.getUserName())){
                    //Handles if judging...
                    System.out.println("Here be a judge...");
                    cardsToJudge = new ArrayList<>();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            client.setJudging(line[3]);
                        }
                    });
                }else if(line[0].equals("stage") && line.length == 4 && line[1].equals("judge")){
                    //Handle regular judging...
                    System.out.println("Submission phase");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            client.setSubmitting(line[3]);
                        }
                    });
                }else if (line[0].equals("cardsToJudge")){
                    int numberOfSubmissionsToExpect = Integer.valueOf(line[2]);
                    this.cardsToJudge.add(line[1]);
                    if (this.cardsToJudge.size() == numberOfSubmissionsToExpect){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //Some function here to display judged cards to users!
                                ChooseQuestion chooseQuestion = new ChooseQuestion(cardsToJudge);
                                String s = chooseQuestion.queryQuestion();
                                //Do stuff here to send back to server and distribute results
                                System.out.println("Judged: "+s);
                                client.sendCommand("setWinningCard;"+s+'\n');
                            }
                        });

                    }
                }
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }

        System.out.println("Closing connection");
    }





}
