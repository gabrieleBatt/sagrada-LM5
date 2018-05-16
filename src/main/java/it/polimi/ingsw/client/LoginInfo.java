package it.polimi.ingsw.client;

public class LoginInfo{

    public String connectionType;
    public String nickname;
    public int portNumber;
    public String IP;
    public String password;

    @Override
    public String toString() {
        return connectionType+nickname+portNumber+IP;
    }
}
