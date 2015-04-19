package com.mkoi.over9000.message.response;

/**
 * @Author Bartłomiej Borucki
 */
public class RegisterResponse extends ServerResponse {

    @Override
    public String toString() {

        return "{error=" + getError() + " description=" + getDescription() + "}";
    }
}
