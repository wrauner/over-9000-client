package com.mkoi.over9000.message.response;

/**
 * @Author Bartłomiej Borucki
 */
public class LoginResponse extends ServerResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {

        return "{error=" + getError() + " description=" + getDescription() + " token=" + getToken() + "}";
    }
}
