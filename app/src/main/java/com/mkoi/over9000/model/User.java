package com.mkoi.over9000.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca użytkownika
 *
 * @author Wojciech Rauner
 */
public class User implements Serializable{
    private String nick;
    private String id;

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
}
