package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkoi.android_identicons.SymmetricIdenticon;
import com.mkoi.over9000.R;
import com.mkoi.over9000.message.UserMessage;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.message_view)
public class MessageView extends LinearLayout {

    @ViewById
    TextView textMessage;

    @ViewById
    SymmetricIdenticon userAvatar;

    public MessageView(Context context) {
        super(context);
    }

    public void bind(UserMessage message) {
        textMessage.setText(message.getMessage());
        userAvatar.show(message.getFrom());
    }
}
