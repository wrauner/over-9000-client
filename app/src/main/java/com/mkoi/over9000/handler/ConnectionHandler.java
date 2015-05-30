package com.mkoi.over9000.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mkoi.over9000.UsersActivity;
import com.mkoi.over9000.socket.SocketConnection;
import com.mkoi.over9000.socket.SocketListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * @author Wojciech Rauner
 */
@EBean
public class ConnectionHandler extends Handler {

    @RootContext
    UsersActivity usersActivity;

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        String event = bundle.getString(SocketListener.EVENT);
        if(event.equals(SocketConnection.CLIENT_LIST)) {
            usersActivity.refreshList(bundle.getString(SocketListener.DATA));
        }
        if(event.equals(SocketConnection.NEW_CLIENT)) {
            usersActivity.userConnected(bundle.getString(SocketListener.DATA));
        }
        if(event.equals(SocketConnection.CLIENT_DISCONNECTED)) {
            usersActivity.userDisconnected(bundle.getString(SocketListener.DATA));
        }
        if(event.equals(SocketConnection.CONNECTION_ACCEPTED)) {
            usersActivity.connectionAccepted(bundle.getString(SocketListener.DATA));
        }
        if(event.equals(SocketConnection.CONNECTION_REJECTED)){
            usersActivity.connectionRejected(bundle.getString(SocketListener.DATA));
        }
        if(event.equals(SocketConnection.CONNECTION_REQUEST)) {
            usersActivity.connectionRequest(bundle.getString(SocketListener.DATA));
        }
    }
}
