package com.mkoi.over9000.message;

/**
 * @Author Bartłomiej Borucki
 */
public class LoginMessage {
    private String email;
    private String password;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
