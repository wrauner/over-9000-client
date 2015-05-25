package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.mkoi.over9000.adapter.MessageListAdapter;
import com.mkoi.over9000.handler.ChatHandler;
import com.mkoi.over9000.message.SecuredMessage;
import com.mkoi.over9000.message.UserMessage;
import com.mkoi.over9000.model.User;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.secure.AllOrNothing;
import com.mkoi.over9000.secure.SecureBlock;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

    User connectedUser;

    byte[] secret;

    MediaPlayer mediaPlayer;

    public void receivedMessage(UserMessage message) {
        //Log.d(LOG_TAG, "Received message: "+message.toString());
        Log.d(LOG_TAG, "Received message");
        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.start();
        listAdapter.add(message);
    }

    @Click(R.id.sendButton)
    public void sendMessage(View view) {
        if(messageText.toString().trim().length() > 0){

        UserMessage userMessage = new UserMessage();
        SecureRandom random = new SecureRandom();
        ArrayList<SecuredMessage> messages = new ArrayList<>();
        try {
            ArrayList<String> blocks = AllOrNothing.transformMessage(messageText.getText().toString().trim());
            messages = SecureBlock.createBlocksToSend(blocks);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        userMessage.setSecuredMessages(messages);
        //userMessage.setMessage(messageText.getText().toString().trim()+new String(secret));
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();
        userMessage.setFrom(preferences.nick().get());
        userMessage.setTo(connectedUser.getId());
        userMessage.setTimestamp(nowTime);
        listAdapter.add(userMessage);
        messageText.setText("");
        Log.d(LOG_TAG,"Sending message:"+userMessage.toString());
        connection.sendMessage(userMessage);
        }

    }

    @AfterInject
    public void setupConnection() {
        connection.setupChatHandler(chatHandler);
    }

    @AfterViews
    public void bindAdapter() {
        chatList.setAdapter(listAdapter);
    }

    @AfterInject
    public void fillUser() {
        connectedUser = (User) getIntent().getExtras().getSerializable("user");
        secret = getIntent().getExtras().getByteArray("secret");
    }
}
