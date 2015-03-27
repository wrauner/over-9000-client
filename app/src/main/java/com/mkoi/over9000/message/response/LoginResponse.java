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
        StringBuilder sb = new StringBuilder();
        sb.append("{error=").append(getError());
        sb.append(" description=").append(getDescription());
        sb.append(" token=").append(getToken());
        sb.append("}");

        return sb.toString();
    }
}
