package com.mkoi.over9000.message.response;

/**
 * Ogólna odpowiedź serwera
 * @author Bartłomiej Borucki
 */
public class ServerResponse {

    /**
     * Numer błędu
     */
    private String error;
    /**
     * Opis błędu
     */
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
