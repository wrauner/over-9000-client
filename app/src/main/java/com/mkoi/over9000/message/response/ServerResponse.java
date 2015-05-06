package com.mkoi.over9000.message.response;

/**
 * @author Bartłomiej Borucki
 */
public class ServerResponse {

    private String error;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
