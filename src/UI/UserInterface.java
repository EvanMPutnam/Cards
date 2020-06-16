package UI;

import Client.Client;
import Utils.BlackCardGenerator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;


public class UserInterface extends Application{

    private ArrayList<Rect> rects;
    private Client client;
    private BlackCardGenerator generator;
    private Stage stage;
    private Scene startScene;
    private Label question;

    private boolean judging = false;
    private boolean canSubmit = false;


    private boolean handleLogin(String username, String roomPass, String ip){
        Client cl = new Client(username, roomPass, ip, this, this.generator);
        if (cl.getConnected()){
            client = cl;
            return true;
        }

        return false;
    }


    /**
     * Login page!
     * @param stage set the stage lololololol
     */
    public void loginPage(Stage stage){

        VBox vb = new VBox();

        Label l1 = new Label();
        l1.setText("Login here");
        l1.setAlignment(Pos.CENTER);
        l1.setFont(Font.font("Verdana", FontWeight.BOLD, 60));

        HBox userName = new HBox();
        Label lUserName = new Label();
        lUserName.setText("Username: ");
        TextField tfUserName = new TextField();
        userName.getChildren().addAll(lUserName, tfUserName);
        userName.setAlignment(Pos.CENTER);


        HBox roomPass = new HBox();
        Label lRoomPass = new Label();
        lRoomPass.setText("Room Password: ");
        TextField tfRoomPass = new TextField();
        roomPass.getChildren().addAll(lRoomPass, tfRoomPass);
        roomPass.setAlignment(Pos.CENTER);


        HBox ipAddress = new HBox();
        Label lIpAddress = new Label();
        lIpAddress.setText("IP Address: ");
        TextField tfIpAddress = new TextField();
        ipAddress.getChildren().addAll(lIpAddress, tfIpAddress);
        ipAddress.setAlignment(Pos.CENTER);


        Button b1 = new Button();
        b1.setText("Log in");
        b1.setAlignment(Pos.CENTER);
        b1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String uName = tfUserName.getText();
                String rPass = tfRoomPass.getText();
                String ipAddr = tfIpAddress.getText();


                //Handle errors
                if (uName.length() == 0|| rPass.length() == 0 || ipAddr.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Error in login.");
                    alert.setContentText("Please enter all fields and try again.");
                    alert.showAndWait();
                }else {
                    boolean x = handleLogin(uName, rPass, ipAddr);
                    if (!x){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login Error");
                        alert.setHeaderText("Error in login.");
                        alert.setContentText("Failed to connect to host.");
                        alert.showAndWait();
                    }else{
                        startPage(stage);
                    }
                }


            }
        });


        vb.getChildren().addAll(l1 ,userName, roomPass, ipAddress, b1);
        vb.setAlignment(Pos.CENTER);



        Scene scene = new Scene(vb, 800, 600);

        stage.setScene(scene);
        stage.setTitle("Space Against Time");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();


    }


    public void startPage(Stage stage){

        this.rects = new ArrayList<>();

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 7; i++){

            Button b1 = new Button();
            b1.setText("Submit");
            b1.setAlignment(Pos.CENTER);

            StackPane stackPane = new StackPane();
            stackPane.setMaxSize(100, 200);
            Label boxText = new Label();
            boxText.setMaxSize(90, 200);
            boxText.setAlignment(Pos.CENTER);
            boxText.setWrapText(true);
            boxText.setText(generator.getRandomCard());
            boxText.setTextFill(Color.WHITE);
            VBox vBox = new VBox(boxText);
            vBox.setAlignment(Pos.CENTER);
            Rect rect = new Rect(100, 200, boxText, b1, client);
            stackPane.setPadding(new Insets(5,5,5,5));
            stackPane.getChildren().addAll(rect, vBox);


            VBox vBox1 = new VBox();
            vBox1.getChildren().addAll(stackPane, b1);
            vBox1.setAlignment(Pos.CENTER);
            gridPane.add(vBox1, i, 0);

            rects.add(rect);
        }
        borderPane.setCenter(gridPane);

        //Question
        Label question2 = new Label();
        this.question = question2;
        question2.setText("Questions be here!  Please wait...");
        question2.setFont(new Font(30));
        question2.setWrapText(true);
        question2.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(question2);
        vbox.setAlignment(Pos.CENTER);
        borderPane.setTop(vbox);



        Scene scene = new Scene(borderPane,800, 600);
        this.startScene = scene;
        stage.setTitle("Space Against Time");
        stage.setWidth(800);
        stage.setHeight(600);

        stage.setScene(scene);
        stage.show();
    }

    //Might need a setter.
    public synchronized void submitStage(String card){
        judging = false;
        canSubmit = true;
        this.question.setText("Question: "+card);
    }


    public synchronized void turnOffSubmit(){
        this.canSubmit = false;
    }



    public synchronized boolean getCanSubmit(){
        return canSubmit;
    }


    public synchronized boolean getCanJudge(){
        return judging;
    }


    /**
     * Judging stage
     */
    public void judging(String card){
        this.question.setText("Judging: "+card);
        canSubmit = false;
        judging = true;
    }


    public void start(Stage stage){
        this.stage = stage;
        generator = new BlackCardGenerator();
        loginPage(stage);
    }

    public static void main(String[] args){
        Application.launch(args);
    }

}
