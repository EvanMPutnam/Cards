package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerClientHandler extends Thread {

    private final Socket clientSocket;
    private String roomPassword;
    private ClientPool clientPool;
    private GameManager manager;


    public ServerClientHandler(Socket socket, String roomPassword, ClientPool pool, GameManager manager) {
        this.clientSocket = socket;
        this.roomPassword = roomPassword;
        this.clientPool = pool;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            //Gets data from client
            InputStream is = clientSocket.getInputStream();

            //Can send data to the client
            OutputStream os = clientSocket.getOutputStream();


            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            String username = null;

            int commandCounter = 0;
            //While loop handler
            while ((line = reader.readLine()) != null) {
                String[] str = line.split(";");
                if(commandCounter == 0){
                    if (str[0].equals("login")) {
                        try {
                            System.out.println("Log in: " + str[1]);
                            if (str[2].equals(roomPassword)){
                                commandCounter += 1;
                                username = str[1];
                                if (clientPool.hasUser(username)){
                                    os.write("message;Username already taken\n".getBytes());
                                    break;
                                }
                                clientPool.addUser(username, new User(username, os));
                                os.write("message;login successful\n".getBytes());
                                if (clientPool.getCurrentUserCount() >= GameManager.MIN_PLAYERS ){
                                    manager.changeState();
                                }
                                continue;
                            }else{
                                os.write("message;Invalid room password\n".getBytes());
                                System.out.println("Invalid room password. Exiting.");
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }

                //All other commands go here!
                if (str.length != 0) {

                    if ("quit".equalsIgnoreCase(line)) {
                        System.out.println("quit command");
                        break;
                    }else if("playerCount".equalsIgnoreCase(line)){
                        ArrayList<String> users = clientPool.getUserNames();
                        String send = "message";
                        for (String s: users) {
                            send += s;
                            send += ";";
                        }
                        send += '\n';
                        os.write(send.getBytes());
                        //Given the precondition that the submission stage is in progress.
                    }else if (str[0].equalsIgnoreCase("submit")) {
                        System.out.println("Submission: "+str[1]);
                        manager.submitCard(str[1]);
                    }else if (str[0].equals("setWinningCard") && manager.getJudge().getUserName().equals(username)) {
                        manager.setWinningCard(str[1]);
                    }else{
                        if (!line.equals("") && !line.equals("\n")) {
                            System.out.println("Command not supported: "+line);
                        }
                    }
                }
            }


            if (username != null){
                System.out.println("Exit: "+username);
                clientPool.removeUser(username);
            }

            System.out.println("Closing connection");
            clientSocket.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }


}