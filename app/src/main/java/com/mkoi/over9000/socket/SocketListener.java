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
    public static final String EVENT = "EVENT";
    public static final String DATA = "DATA";

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
        bundle.putString(EVENT, event);
        bundle.putSerializable(DATA, obj.toString());
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
