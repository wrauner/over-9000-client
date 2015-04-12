package com.mkoi.over9000.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkoi.over9000.model.Friend;
import com.mkoi.over9000.view.FriendView;
import com.mkoi.over9000.view.FriendView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wojciech Rauner
 */
@EBean
public class FriendListAdapter extends BaseAdapter {
    List<Friend> friends = new ArrayList<>(); //TODO save this list

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Friend getItem(int i) {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FriendView friendView;
        if(view == null) {
            friendView = FriendView_.build(context);
        } else {
            friendView = (FriendView) view;
        }
        friendView.bind(getItem(i));
        return friendView;
    }

    public void add(Friend friend) {
        friends.add(friend);
        notifyDataSetChanged();
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }
}
