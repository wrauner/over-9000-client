package com.mkoi.over9000.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mkoi.over9000.RegisterActivity;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Wojciech Rauner
 */
@EBean
public class RegisterHandler extends Handler {
    public static final String LOG_TAG = "RegisterHandler";

    @RootContext
    RegisterActivity registerActivity;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle.getString("event").equals(SocketConnection.REGISTER_RESPONSE)) {
            Log.d(LOG_TAG, "Received message " + SocketConnection.REGISTER_RESPONSE);
            Log.d(LOG_TAG, "Message body: " + bundle.getString("data"));
//            try {
//                RegisterMessage registerMessage = objectMapper.readValue(bundle.getString("data"), RegisterMessage.class);
//                registerActivity.registerResponse(registerMessage);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error while converting JSON to object ", e);
//            }
        }
    }
}
