package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Rozmowa z użytkownikiem
 */
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

    @Bean
    SecureBlock secureBlock;

    /**
     * Dane użytkownika z którym rozmawiamy
     */
    @Extra
    User connectedUser;

    /**
     * Wspólny sekret ustalony protokołem DH
     */
    @Extra
    byte[] secret;

    /**
     * Odebranie wiadomości i rozpoczęcie jej procesowania
     * @param message
     */
    public void receivedMessage(UserMessage message) {
        Log.d(LOG_TAG, "Received message");
        Log.d(LOG_TAG, "Received message, Secret trace:"+Base64.encodeToString(secret, Base64.DEFAULT));
        processMessage(message);
    }

    /**
     * Wysyła wiadomość podaną przez użytkownika
     */
    @Click(R.id.sendButton)
    public void sendMessage() {
        Log.d(LOG_TAG, "Send message, Secret trace:"+Base64.encodeToString(secret, Base64.DEFAULT));
        if (messageText.toString().trim().length() > 0) {
            UserMessage userMessage = new UserMessage();
            ArrayList<SecuredMessage> messages; //TODO do this @ background
            try {
                ArrayList<String> blocks = AllOrNothing.transformMessage(messageText.getText().toString().trim());
                messages = secureBlock.createBlocksToSend(blocks, secret);
                userMessage.setSecuredMessages(messages);
                userMessage.setDecodedMessage(messageText.getText().toString().trim());
                userMessage.setFrom(preferences.nick().get());
                userMessage.setTo(connectedUser.getId());
                userMessage.setTimestamp(getNowTime());
                userMessage.setMine(true);
                listAdapter.add(userMessage);
                messageText.setText("");
                Log.d(LOG_TAG, "Sending message:" + userMessage.toString());
                connection.sendMessage(userMessage);
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException e) {
                Log.e(LOG_TAG, "Error while creating message", e);
            }
        }
    }

    /**
     * @return obecny czas
     */
    private long getNowTime() {
        Date nowDate = new Date();
        return nowDate.getTime();
    }

    /**
     * Ustawienie handlera połączenia
     */
    @AfterInject
    public void setupConnection() {
        connection.setupChatHandler(chatHandler);
    }

    /**
     * Podpięcie adaptera listy
     */
    @AfterViews
    public void bindAdapter() {
        chatList.setAdapter(listAdapter);
    }

    /**
     * Odtworzenie wiadomości
     * @param message wiadomość
     */
    public void processMessage(UserMessage message) {
        Log.d(LOG_TAG, "Process message, Secret trace:"+Base64.encodeToString(secret, Base64.DEFAULT));
        ArrayList<SecuredMessage> inputBlocks;
        ArrayList<String> goodBlocks;

        inputBlocks = message.getSecuredMessages();
        try {
            goodBlocks = secureBlock.prepareReceivedBlocks(inputBlocks, secret);
            message.setDecodedMessage(AllOrNothing.revertTransformation(goodBlocks));
            listAdapter.add(message);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
            mediaPlayer.start();
        } catch (InvalidKeyException | NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException | NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Error while decoding message", e);
        }
    }

    /**
     * Użytkownik nas rozłączył
     */
    @UiThread
    public void clientQuitConversation() {
        Log.d(LOG_TAG, "Received quit conversation");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.disconnectedTitle)).setMessage(getString(R.string.disconnectedMessage));
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    /**
     * Rozłączenie po wyjściu z chatu
     */
    @Override
    public void onBackPressed() {
        connection.disconnectFromUser(connectedUser.getId());
        finish();
    }
}
