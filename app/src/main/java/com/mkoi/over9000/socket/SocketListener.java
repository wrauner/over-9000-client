package com.mkoi.over9000.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Uniwersalny listener dla obiektów JSON
 * @author Wojciech Rauner
 */
public class SocketListener implements Emitter.Listener {
    public static final String EVENT = "EVENT";
    public static final String DATA = "DATA";

    /**
     * Nazwa eventu socket.io (patrz SocketConnection)
     */
    private String event;
    /**
     * Handler obsługujący dany typ eventu
     */
    private Handler handler;

    /**
     * Konstruktor ustawiający nazwe eventu i handler
     * @param event nazwa eventu
     * @param handler handler do obsługi
     */
    public SocketListener(String event, Handler handler) {
        this.event = event;
        this.handler = handler;
    }

    /**
     * Uruchamiana razem z eventem
     * @param args
     */
    @Override
    public void call(Object... args) {
        try {
            JSONObject obj = (JSONObject) args[0];
            processObject(obj);
        } catch (ClassCastException ex) {
            JSONArray array = (JSONArray) args[0];
            processArray(array);
        }
    }

    /**
     * Jeżeli przyszedł pojedynczy obiekt JSON
     * @param obj dane z eventu
     */
    private void processObject(JSONObject obj) {
        Bundle bundle = new Bundle();
        bundle.putString(EVENT, event);
        bundle.putSerializable(DATA, obj.toString());
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * Jeżeli przyszła tablica obiektów
     * @param array tablica danych z eventu
     */
    private void processArray(JSONArray array) {
        Bundle bundle = new Bundle();
        bundle.putString(EVENT, event);
        bundle.putSerializable(DATA, array.toString());
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
