package Server;

import java.io.IOException;
import java.io.OutputStream;

public class User {

    private String userName;
    private OutputStream outputStream;

    public User(String userName, OutputStream stream){
        this.userName = userName;
        this.outputStream = stream;
    }

    public String getUserName() {
        return userName;
    }

    public void sendToUser(String s){
        try {
            if (s.contains("\n")){
                outputStream.write(s.getBytes());
            }else {
                outputStream.write((s+"\n").getBytes());
            }
        }catch (IOException e){

        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userName != null ? userName.equals(user.userName) : user.userName == null;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }
}
