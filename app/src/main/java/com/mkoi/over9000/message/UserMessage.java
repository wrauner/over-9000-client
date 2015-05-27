package com.mkoi.over9000.message;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;

/**
 * Klasa reprezentująca wiadomość
 * @author Wojciech Rauner
 */
public class UserMessage {
    private String to;
    private String from;
    private long timestamp;
    private ArrayList<SecuredMessage> securedMessages;

    @JsonIgnore
    private String decodedMessage;

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

    @JsonIgnore
    public String getDecodedMessage() {
        return decodedMessage;
    }

    @JsonIgnore
    public void setDecodedMessage(String decodedMessage) {
        this.decodedMessage = decodedMessage;
    }
}
