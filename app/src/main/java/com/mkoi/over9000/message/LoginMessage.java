package com.mkoi.over9000.message;

/**
 * @Author Bartłomiej Borucki
 */
public class LoginMessage {
    private String email;
    private String hash;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
