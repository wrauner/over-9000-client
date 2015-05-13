package com.mkoi.over9000.model;

/**
 * Klasa reprezentująca użytkownika
 *
 * @author Wojciech Rauner
 */
public class User {
    private String nick;
    private String socketId;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
