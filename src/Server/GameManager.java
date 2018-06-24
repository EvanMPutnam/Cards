package Server;


//TODO score for the cards.

import Utils.WhiteCardGenerator;

import java.util.ArrayList;
import java.util.Random;

public class GameManager implements Runnable{


    public static final int MIN_PLAYERS = 3;

    public enum GameState{
        Judgeing, Waiting;
    }

    //Used for the judging
    private Random random;
    private User judge = null;
    private String winningCard = null;
    private WhiteCardGenerator generator;


    private ClientPool clientPool;

    private GameState gameState;

    private boolean runnning = false;

    private ArrayList<String> cards = new ArrayList<>();




    public GameManager(ClientPool clientPool){
        this.clientPool = clientPool;
        this.gameState = GameState.Waiting;
        this.random = new Random();
        this.generator = new WhiteCardGenerator();
    }


    public synchronized void setRunnning(boolean runnning){
        this.runnning = runnning;
    }


    public synchronized void submitCard(String card){
        if (this.gameState.equals(GameState.Judgeing)) {
            this.cards.add(card);
            if (cards.size() == MIN_PLAYERS - 1) {
                sendCardsToJudge();
            }
        }
    }

    /**
     * Function that judge calls to send the winning card back to other players...
     * @param winningCard
     */
    public synchronized void setWinningCard(String winningCard){
        if (this.gameState.equals(GameState.Judgeing)) {
            this.winningCard = winningCard;
            notify();
        }
    }

    /**
     * Private helper function that sends the cards to the current judge.
     */
    private synchronized void sendCardsToJudge(){
        if (gameState.equals(GameState.Judgeing)) {
            for (String card : cards) {
                this.judge.sendToUser("cardsToJudge;" + card+";"+
                        String.valueOf(this.clientPool.getCurrentUserCount()-1));
            }
            System.out.println("Waiting for judge");
        }
    }

    public synchronized boolean getRunning(){
        return runnning;
    }

    public synchronized boolean isJudge(String user){
        if (judge != null){
            return user.equals(judge.getUserName());
        }
        return false;
    }

    public synchronized void changeState(){
        notify();
    }

    public synchronized void sendToAllUsers(String message){
        for (User u : clientPool.users.values()) {
            u.sendToUser(message);
        }
    }



    public synchronized User getJudge(){
        return this.judge;
    }

    public synchronized User getNewJudge(){
        ArrayList<User> users = new ArrayList<>(clientPool.users.values());
        return users.get(random.nextInt(users.size()));
    }

    private String getCards(){
        if (cards.size() != 0){
            String str = "";
            for (String s: cards){
                str += (";"+s);
            }
            return str;
        }
        return "";
    }

    /**
     * Client order of operations
     * login;<user>;<pass>
     *     Once three log in then good to go...
     *
     *
     */
    public void run(){
        while(clientPool.getCurrentUserCount() < MIN_PLAYERS){
            try {
                synchronized (this) {
                    this.wait();
                }
            }catch (InterruptedException io){
                io.printStackTrace();
            }
            sendToAllUsers("startingGame;10000");
            try {
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            this.gameState = GameState.Judgeing;
        }

        setRunnning(true);
        this.judge = getNewJudge();
        sendToAllUsers("stage;judge;"+judge.getUserName()+";"+generator.getRandomCard());


        //Wait until cards are sent in.  Handled with submit card
        //Submit card sends cards to judge and resets arraylist of cards.
        //Wait for winning card submission.  Once it notifies it then sends to all users the results.
        //Sleep x ammount of seconds and then restart.



        while (getRunning()){
            try {
                synchronized (this) {
                    System.out.println("Waiting for submissions");
                    this.wait();
                }
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }

            //Once winning card is selected notify will have been called.
            String s = "stage;results;"+this.winningCard+getCards();
            sendToAllUsers(s);
            cards = new ArrayList<>();
            try {
                //Sleep for a couple seconds before moving onto next stage.
                System.out.println("Waiting before sending more.");
                Thread.sleep(3000);
            }catch (InterruptedException ie2){
                ie2.printStackTrace();
            }
            //Get next judge and start the whole thing over again.
            System.out.println("Getting new judge");
            this.judge = getNewJudge();
            this.sendToAllUsers("stage;judge;"+this.judge.getUserName()+";"+generator.getRandomCard());


        }

    }
}
