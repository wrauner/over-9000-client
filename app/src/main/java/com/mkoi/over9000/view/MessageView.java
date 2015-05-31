package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkoi.android_identicons.SymmetricIdenticon;
import com.mkoi.over9000.R;
import com.mkoi.over9000.message.UserMessage;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
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

    public MessageView(Context context) {
        super(context);
    }

    public void bind(UserMessage message) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        long timestamp = message.getTimestamp();
        Date testPOP = new Date(timestamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(testPOP);
        userNickLabel.setText(message.getFrom());
        timestampLabel.setText(dateFormatted);
        textMessage.setText(message.getDecodedMessage());
        userAvatar.show(message.getFrom());
    }

}
