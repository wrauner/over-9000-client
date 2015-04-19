package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.mkoi.over9000.adapter.MessageListAdapter;
import com.mkoi.over9000.handler.ChatHandler;
import com.mkoi.over9000.message.UserMessage;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@SuppressLint("Registered")
@EActivity(R.layout.activity_chat)
public class ChatActivity extends Activity {
    public static final String LOG_TAG = "Over9000.ChatActivity";

    @ViewById
    EditText messageText;

    @ViewById(R.id.chatList)
    ListView chatList;

    @Bean
    SocketConnection connection;

    @Bean
    ChatHandler chatHandler;

    @Pref
    UserPreferences_ preferences;

    @Bean
    MessageListAdapter listAdapter;

    public void receivedMessage(UserMessage message) {
        Log.d(LOG_TAG, "Received message: "+message.toString());
        listAdapter.add(message);
    }

    @Click(R.id.sendButton)
    public void sendMessage(View view) {
        UserMessage userMessage = new UserMessage();
        userMessage.setText(messageText.getText().toString().trim());
        userMessage.setNick(preferences.nick().get());
        listAdapter.add(userMessage);
        messageText.setText("");
        Log.d(LOG_TAG,"Sending message:"+userMessage.toString());
        connection.sendMessage(userMessage);
    }

    @AfterInject
    public void setupConnection() {
        connection.init(preferences.token().get());
        connection.setupChatHandler(chatHandler);
    }

    @AfterViews
    public void bindAdapter() {
        chatList.setAdapter(listAdapter);
    }
}
