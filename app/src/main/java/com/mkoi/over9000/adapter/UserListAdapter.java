package com.mkoi.over9000.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkoi.over9000.model.User;
import com.mkoi.over9000.view.UserView;
import com.mkoi.over9000.view.UserView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wojciech Rauner
 */
@EBean
public class UserListAdapter extends BaseAdapter {
    List<User> users = new ArrayList<>(); //TODO save this list

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UserView userView;
        if(view == null) {
            userView = UserView_.build(context);
        } else {
            userView = (UserView) view;
        }
        userView.bind(getItem(i));
        return userView;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void addUser(User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    public void deleteUser(User user) {
        int index = users.indexOf(user);
        users.remove(index);
        notifyDataSetChanged();
    }
}