package com.mkoi.over9000.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONObject;

/**
 * @author Wojciech Rauner
 */
public class SocketListener implements Emitter.Listener {
    public static final String LOG_TAG = "SocketListener";

    private String event;
    private Handler handler;

    public SocketListener(String event, Handler handler) {
        this.event = event;
        this.handler = handler;
    }

    @Override
    public void call(Object... args) {
        JSONObject obj = (JSONObject) args[0];
        Bundle bundle = new Bundle();
        bundle.putString("event", event);
        bundle.putSerializable("data", obj.toString());
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
