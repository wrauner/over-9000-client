package com.mkoi.over9000.message;

import java.util.ArrayList;

/**
 * Klasa reprezentująca wiadomość
 * @author Wojciech Rauner
 */
public class UserMessage {
    private String to;
    private String from;
    private long timestamp;
    //private String message;
    private ArrayList<SecuredMessage> securedMessages;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<SecuredMessage> getSecuredMessages() {
        return securedMessages;
    }

    public void setSecuredMessages(ArrayList<SecuredMessage> securedMessages) {
        this.securedMessages = securedMessages;
    }
}
