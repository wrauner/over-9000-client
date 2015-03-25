package com.mkoi.over9000.message;

/**
 * @author Wojciech Rauner
 */
public class RegisterMessage {
    private String error;
    private String description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
