package com.mkoi.over9000.message;

/**
 * @author Wojciech Rauner
 */
public class ConnectToUser {
    private String socketId;
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
