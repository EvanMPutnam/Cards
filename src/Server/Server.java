package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//find . -name '*.java' -print0 | xargs -0 wc -l
//TODO Maybe make the buttons invisible if they can not send???

public class Server {

    public static void main(String[] args){

        ClientPool pool = new ClientPool();
        GameManager manager = new GameManager(pool);

        Thread thread = new Thread(manager);
        thread.start();

        int port = 8080;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            String password = "Evan";
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerClientHandler serverClientHandler = new ServerClientHandler(clientSocket, password, pool, manager);
                serverClientHandler.start();
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }

}
