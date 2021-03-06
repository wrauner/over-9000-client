package com.mkoi.over9000.socket;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mkoi.over9000.handler.ChatHandler;
import com.mkoi.over9000.handler.ConnectionHandler;
import com.mkoi.over9000.message.ConnectToUser;
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

    public static final String LOG_TAG = "Over9000.SocketConn";
    public static final String SEND_MESSAGE = "send_message";
    public static final String RECEIVED_MESSAGE = "received_message";
    public static final String CLIENT_LIST = "client_list";
    public static final String CONNECT_TO_USER = "connect_to_user";
    public static final String CONNECTION_REQUEST = "connection_request";
    public static final String ACCEPT_CONNECTION = "accept_connection";
    public static final String REJECT_CONNECTION = "reject_connection";
    public static final String CONNECTION_ACCEPTED = "connection_accepted";
    public static final String CONNECTION_REJECTED = "connection_rejected";
    public static final String GET_USERS = "get_users";
    public static final String NEW_CLIENT = "new_client";
    public static final String CLIENT_DISCONNECTED = "client_disconnected";
    public static final String SERVER_ADDRESS = "http://over9000-cryptosync.rhcloud.com";
    public static final String DISCONNECT_FROM_USER = "disconnect_from_user";
    public static final String CLIENT_QUIT_CONVERSATION = "client_quit_conversation";
    //public static final String SERVER_ADDRESS = "http://192.168.0.4:3000";

    /**
     * Obiekt dostępu do socket.io
     */
    private Socket socket;
    /**
     * Mapper pojo/json
     */
    private ObjectMapper objectMapper;

    /**
     * Wymagany konstruktor bezparametrowy
     */
    public SocketConnection() {
    }

    /**
     * Inicjalizacja połączenia
     * @param token token uwierzytelniający JWT
     */
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

    /**
     * Ustawia handler dla wiadomości wysyłanych pomiędzy użytkownikami
     * @param handler
     */
    public void setupChatHandler(ChatHandler handler) {
        socket.on(RECEIVED_MESSAGE, new SocketListener(RECEIVED_MESSAGE, handler));
        socket.on(CLIENT_QUIT_CONVERSATION, new SocketListener(CLIENT_QUIT_CONVERSATION, handler));
    }

    /**
     * Ustawia handler dla pozostałych wiadomości
     * @param handler
     */
    public void setupConnectionHandler(ConnectionHandler handler) {
        socket.on(CLIENT_LIST, new SocketListener(CLIENT_LIST, handler));
        socket.on(CONNECTION_REQUEST, new SocketListener(CONNECTION_REQUEST, handler));
        socket.on(CONNECTION_ACCEPTED, new SocketListener(CONNECTION_ACCEPTED, handler));
        socket.on(CONNECTION_REJECTED, new SocketListener(CONNECTION_REJECTED, handler));
        socket.on(NEW_CLIENT, new SocketListener(NEW_CLIENT, handler));
        socket.on(CLIENT_DISCONNECTED, new SocketListener(CLIENT_DISCONNECTED, handler));
    }

    /**
     * Wysyła wiadomość
     * @param message wiadomość
     */
    public void sendMessage(UserMessage message) {
        Log.d(LOG_TAG, "Sending message");
        try {
            String data = objectMapper.writeValueAsString(message);
            socket.emit(SEND_MESSAGE, data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

    /**
     * Rozłącza się
     */
    public void disconnect() {
        Log.d(LOG_TAG, "Disconnecting");
        socket.disconnect();
    }

    /**
     * Podłącz do użytkownika
     * @param request żądanie podłączenia do użytkownika
     */
    public void connectToUser(ConnectToUser request) {
        Log.d(LOG_TAG, "Sending connection request");
        try {
            socket.emit(CONNECT_TO_USER, objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

    /**
     * Wysłanie odpowiedzi z akceptacją podłączenia
     * @param request żądanie podłączenia
     */
    public void acceptConnection(ConnectToUser request) {
        Log.d(LOG_TAG, "Sending connection accepted");
        try {
            socket.emit(ACCEPT_CONNECTION, objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

    /**
     * Odrzucenie połączenia
     * @param request odrzucenie połąćzenia
     */
    public void rejectConnection(ConnectToUser request) {
        Log.d(LOG_TAG, "Sending connection rejected");
        try {
            socket.emit(REJECT_CONNECTION, objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing object to json", e);
        }
    }

    /**
     * Zakończenie połączenia z użytkownikiem
     * @param socketId id podłączonego usera
     */
    public void disconnectFromUser(String socketId) {
        Log.d(LOG_TAG, "Sending disconnect request");
        socket.emit(DISCONNECT_FROM_USER, socketId);
    }
}
