package com.mkoi.over9000.message.response;

/**
 * @Author Bartłomiej Borucki
 */
public class RegisterResponse extends ServerResponse {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{error=").append(getError());
        sb.append(" description=").append(getDescription());
        sb.append("}");

        return sb.toString();
    }
}
