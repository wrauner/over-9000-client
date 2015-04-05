package com.mkoi.over9000.socket;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mkoi.over9000.handler.ChatHandler;
import com.mkoi.over9000.message.UserMessage;

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

    public static final String LOG_TAG = "Over9000.SocketConnection";
    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String RECEIVED_MESSAGE = "RECEIVED_MESSAGE";
    public static final String SERVER_ADDRESS = "http://over9000-cryptosync.rhcloud.com";
    //public static final String SERVER_ADDRESS = "http://10.1.8.63:3000";

    private Socket socket;
    private ObjectMapper objectMapper;

    public SocketConnection() {
    }

    public void init(String token) {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token="+token;
            this.socket = IO.socket(SERVER_ADDRESS, opts);
            socket.connect();
            this.objectMapper = new ObjectMapper();
        } catch (URISyntaxException e) {
            Log.e(LOG_TAG, "Failed to initialize ", e);
        }
    }

    public void setupChatHandler(ChatHandler handler) {
        socket.on(RECEIVED_MESSAGE, new SocketListener(RECEIVED_MESSAGE, handler));
    }

    public void sendMessage(UserMessage message) {
        Log.d(LOG_TAG, "Sending message:" + message.getText());
        try {
            String data = objectMapper.writeValueAsString(message);
            socket.emit(SEND_MESSAGE, data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

}
