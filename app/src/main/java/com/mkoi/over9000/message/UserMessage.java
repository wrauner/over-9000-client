package com.mkoi.over9000.message;

/**
 * Klasa reprezentująca wiadomość
 * @author Wojciech Rauner
 */
public class UserMessage {
    private String text;
    private String nick;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "text='" + text + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
