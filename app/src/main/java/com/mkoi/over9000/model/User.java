package com.mkoi.over9000.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca użytkownika
 *
 * @author Wojciech Rauner
 */
public class User implements Serializable {
    /**
     * Nick użytkownika
     */
    private String nick;
    /**
     * Socket.id użytkownika
     */
    private String id;
    /**
     * Wspólny sekret
     */
    private byte[] key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}
