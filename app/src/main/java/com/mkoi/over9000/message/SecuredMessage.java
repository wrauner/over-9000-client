package com.mkoi.over9000.message;

/**
 * Wiadomość z chatu
 * @author Bartłomiej Borucki
 */
public class SecuredMessage {
    /**
     * Kolejność
     */
    private int id;
    /**
     * Wiadomość
     */
    private String message;
    /**
     * HMAC dla wiadomości
     */
    private String hmac;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

}
