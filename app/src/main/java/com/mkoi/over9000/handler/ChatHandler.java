package com.mkoi.over9000.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mkoi.over9000.ChatActivity;
import com.mkoi.over9000.message.UserMessage;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import static com.mkoi.over9000.socket.SocketListener.DATA;
import static com.mkoi.over9000.socket.SocketListener.EVENT;

/**
 * Handler eventów związanych z chatem z użytkownikiem
 * @author Wojciech Rauner
 */
@EBean
public class ChatHandler extends Handler {
    public static final String LOG_TAG = "Over9000.ChatHandler";

    @RootContext
    ChatActivity chatActivity;

    /**
     * Mapper POJO/JSON
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obsługa odebrania wiadomości
     * @param msg wiadomość
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        if(bundle.getString(EVENT).equals(SocketConnection.RECEIVED_MESSAGE)) {
            try {
                UserMessage message = objectMapper.readValue(bundle.getString(DATA), UserMessage.class);
                chatActivity.receivedMessage(message);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while parsing json", e);
            }
        }
    }
}
