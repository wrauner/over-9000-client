package com.mkoi.over9000.message;

/**
 * Wiadomość żądania podłączenia do użytkownika
 * @author Wojciech Rauner
 */
public class ConnectToUser {
    /**
     * Socket.id uzytkownika do którego się podłączamy
     */
    private String socketId;
    /**
     * Wspólny sekret (lub część)
     */
    private byte[] key;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}
