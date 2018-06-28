package it.polimi.ingsw.client.view;

public final class LoginInfo{

    public final String connectionType;
    public final String nickname;
    public final int portNumber;
    public final String ip;
    public final String password;

    public LoginInfo(String connectionType, String nickname, int portNumber, String ip, String password){
        this.connectionType = connectionType;
        this.nickname = nickname;
        this.portNumber = portNumber;
        this.ip = ip;
        this.password = password;
    }

    @Override
    public String toString() {
        return connectionType+" "+nickname+" "+portNumber+" "+ip;
    }
}
