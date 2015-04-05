package com.mkoi.over9000.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkoi.over9000.message.UserMessage;
import com.mkoi.over9000.view.MessageView;
import com.mkoi.over9000.view.MessageView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wojciech Rauner
 */

@EBean
public class MessageListAdapter extends BaseAdapter{
    List<UserMessage> messages = new ArrayList<>();

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public UserMessage getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageView messageView;
        if(view == null) {
            messageView = MessageView_.build(context);
        } else {
            messageView = (MessageView) view;
        }

        messageView.bind(getItem(i));
        return messageView;
    }

    public void add(UserMessage message) {
        messages.add(message);
        notifyDataSetChanged();
    }
}
