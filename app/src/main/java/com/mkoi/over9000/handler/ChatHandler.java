package com.mkoi.over9000.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mkoi.over9000.ChatActivity;
import com.mkoi.over9000.message.UserMessage;
import com.mkoi.over9000.socket.SocketConnection;
import com.mkoi.over9000.socket.SocketListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * @author Wojciech Rauner
 */
@EBean
public class ChatHandler extends Handler {
    public static final String LOG_TAG = "Over9000.ChatHandler";

    @RootContext
    ChatActivity chatActivity;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        if(bundle.getString(SocketListener.EVENT).equals(SocketConnection.RECEIVED_MESSAGE)) {
            try {
                UserMessage message = objectMapper.readValue(bundle.getString(SocketListener.DATA), UserMessage.class);
                chatActivity.receivedMessage(message);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while parsing json", e);
            }
        }
    }
}
