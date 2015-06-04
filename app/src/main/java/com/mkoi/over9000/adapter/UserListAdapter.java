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
 * Adapter listy użytkowników dostępnych na serwerze
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

    /**
     * Dodaje użytkowników do listy
     * @param users użytkownicy dostępni na serwerze
     */
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    /**
     * Dodaje pojedynczego użytkownika do listy
     * @param user użytkownik
     */
    public void addUser(User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    /**
     * Przeszukuje listę i usuwa użytkownika, który się rozłączył
     * @param user użytkownik który się rozłączył
     */
    public void deleteUser(User user) {
        for(User search : users) {
            if(search.getId().equals(user.getId())) {
                users.remove(search);
                break;
            }
        }
    }
}
