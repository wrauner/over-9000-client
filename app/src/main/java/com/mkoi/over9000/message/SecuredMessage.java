package com.mkoi.over9000.message;

/**
 * @author Bartłomiej Borucki
 */
public class SecuredMessage {
    private int id;
    private String message;
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
