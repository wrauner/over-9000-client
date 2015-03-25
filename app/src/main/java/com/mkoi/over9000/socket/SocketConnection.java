package com.mkoi.over9000.socket;

import android.os.Handler;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mkoi.over9000.model.User;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Singleton dający połączenie z serwerem za pomocą Socket.IO
 *
 * @author Wojciech Rauner
 */
public class SocketConnection {

    public static final String LOG_TAG = "SocketConnection";
    public static final String REGISTER_USER = "registerUser";
    public static final String REGISTER_RESPONSE = "registerResponse";
    public static final String SERVER_ADDRESS = "http://192.168.0.4:3000";

    private Socket socket;
    private static SocketConnection instance;
    private Handler handler;
    private ObjectMapper objectMapper;

    public static SocketConnection get(Handler handler) {
        if (instance == null) {
            instance = getSynchronized(handler);
        }
        instance.handler = handler;
        return instance;
    }

    public static synchronized SocketConnection getSynchronized(Handler handler) {
        if (instance == null) {
            instance = new SocketConnection(handler);
        }
        return instance;
    }

    private SocketConnection(Handler handler) {
        this.handler = handler;
        this.socket = getServerSocket();
        this.objectMapper = new ObjectMapper();
    }

    private Socket getServerSocket() {
        try {
            this.socket = IO.socket(SERVER_ADDRESS);
            //Deklaracja eventów
            socket.on(REGISTER_RESPONSE, new SocketListener(REGISTER_RESPONSE, handler));
            socket.connect();
        } catch (URISyntaxException e) {
            Log.e(LOG_TAG, "Cannot setup connection to server", e);
        }
        return socket;
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
