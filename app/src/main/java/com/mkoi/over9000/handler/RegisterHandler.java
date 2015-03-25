package com.mkoi.over9000.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mkoi.over9000.RegisterActivity;
import com.mkoi.over9000.message.RegisterMessage;
import com.mkoi.over9000.socket.SocketConnection;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author Wojciech Rauner
 */
public class RegisterHandler extends Handler {
    public static final String LOG_TAG = "RegisterHandler";
    private final WeakReference<RegisterActivity> registerActivity;
    private ObjectMapper objectMapper;

    public RegisterHandler(RegisterActivity registerActivity) {
        this.registerActivity = new WeakReference<>(registerActivity);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handleMessage(Message msg) {
        RegisterActivity activity = registerActivity.get();
        Bundle bundle = msg.getData();
        if (bundle.getString("event").equals(SocketConnection.REGISTER_RESPONSE)) {
            Log.d(LOG_TAG, "Received message " + SocketConnection.REGISTER_RESPONSE);
            Log.d(LOG_TAG, "Message body: " + bundle.getString("data"));
            try {
                RegisterMessage registerMessage = objectMapper.readValue(bundle.getString("data"), RegisterMessage.class);
                activity.registerResponse(registerMessage);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while converting JSON to object ", e);
            }
        }
    }
}
