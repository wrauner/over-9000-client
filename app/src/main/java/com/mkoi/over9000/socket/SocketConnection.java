package com.mkoi.over9000.socket;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mkoi.over9000.handler.RegisterHandler;
import com.mkoi.over9000.model.User;

import org.androidannotations.annotations.EBean;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Singleton dający połączenie z serwerem za pomocą Socket.IO
 *
 * @author Wojciech Rauner
 */
@EBean(scope = EBean.Scope.Singleton)
public class SocketConnection {

    public static final String LOG_TAG = "SocketConnection";
    public static final String REGISTER_USER = "registerUser";
    public static final String REGISTER_RESPONSE = "registerResponse";
    public static final String SERVER_ADDRESS = "http://192.168.0.4:3000";
    //public static final String SERVER_ADDRESS = "http://10.1.8.63:3000";

    private Socket socket;
    private ObjectMapper objectMapper;

    public SocketConnection() {
        try {
            this.socket = IO.socket(SERVER_ADDRESS);
            socket.connect();
            this.objectMapper = new ObjectMapper();
        } catch (URISyntaxException e) {
            Log.e(LOG_TAG, "Failed to initialize ", e);
        }
    }

    public void setupRegisterHandler(RegisterHandler handler) {
        socket.on(REGISTER_RESPONSE, new SocketListener(REGISTER_RESPONSE, handler));
    }

    public void registerUser(User user) {
        Log.d(LOG_TAG, "Registering user:" + user.getEmail());
        try {
            String data = objectMapper.writeValueAsString(user);
            socket.emit(REGISTER_USER, data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

}