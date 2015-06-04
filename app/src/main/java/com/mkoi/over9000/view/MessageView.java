package com.mkoi.over9000.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkoi.android_identicons.SymmetricIdenticon;
import com.mkoi.over9000.R;
import com.mkoi.over9000.message.UserMessage;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Widok pojedynczej wiadomości
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.message_view_new)
public class MessageView extends LinearLayout {

    @ViewById
    TextView textMessage;

    @ViewById
    SymmetricIdenticon userAvatar;

    @ViewById
    TextView userNickLabel;

    @ViewById
    TextView timestampLabel;

    @ViewById
    LinearLayout messageBackground;

    /**
     * Wymagany konstruktor
     * @param context referencja do activity
     */
    public MessageView(Context context) {
        super(context);
    }

    /**
     * Podpięcie POJO do widoku
     * @param message wiadomość
     */
    @SuppressLint("SimpleDateFormat")
    public void bind(UserMessage message) {
        long timestamp = message.getTimestamp();
        Date testPOP = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(testPOP);
        userNickLabel.setText(message.getFrom());
        timestampLabel.setText(dateFormatted);
        textMessage.setText(message.getDecodedMessage());
        userAvatar.show(message.getFrom());
        if(message.isMine()) {
            changeLayoutForMine();
        } else {
            changeLayoutForOther();
        }
    }

    /**
     * Zmienia tło wiadomości
     */
    private void changeLayoutForMine() {
        messageBackground.setBackground(getResources().getDrawable(R.drawable.layout_background_rounded_green));
    }

    /**
     * Zmienia tło wiadmoości
     */
    private void changeLayoutForOther() {
        messageBackground.setBackground(getResources().getDrawable(R.drawable.layout_background_rounded));
    }

}
