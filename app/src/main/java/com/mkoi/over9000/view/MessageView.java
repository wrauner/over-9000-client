package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    TextView nickText;

    @ViewById
    TextView messageText;

    public MessageView(Context context) {
        super(context);
    }

    public void bind(UserMessage message) {
        nickText.setText(message.getNick()+": ");
        messageText.setText(message.getText());
    }
}
