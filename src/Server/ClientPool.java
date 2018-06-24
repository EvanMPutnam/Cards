package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ClientPool {


    ConcurrentHashMap<String, User> users;

    public ClientPool(){
        this.users = new ConcurrentHashMap<>();
    }

    public synchronized void addUser(String s, User u){
        users.put(s, u);
    }

    public synchronized User getUser(String s){
        if (users.containsKey(s)){
            return users.get(s);
        }
        return null;
    }

    public synchronized int getCurrentUserCount(){
        return users.size();
    }

    public synchronized boolean hasUser(String s){
        return users.containsKey(s);
    }

    public synchronized void removeUser(String s){
        if (users.containsKey(s)){
            users.remove(s);
        }
    }

    public synchronized ArrayList<String> getUserNames(){
        return new ArrayList<>(users.keySet());
    }

}
