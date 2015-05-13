package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.mkoi.over9000.adapter.UserListAdapter;
import com.mkoi.over9000.handler.ConnectionHandler;
import com.mkoi.over9000.model.User;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.IOException;
import java.util.List;

@SuppressLint("Registered")
@EActivity(R.layout.activity_users)
public class UsersActivity extends Activity {
    public static final String LOG_TAG = "FriendsActivity";

    @ViewById
    ListView userList;

    @Bean
    UserListAdapter adapter;

    @Pref
    UserPreferences_ userPreferences;

    @Bean
    SocketConnection connection;

    @Bean
    ConnectionHandler connectionHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    @AfterViews
    public void bindAdapter() {
        userList.setAdapter(adapter);
    }

    @AfterInject
    public void setupConnection() {
        connection.init(userPreferences.token().get());
        connection.setupConnectionHandler(connectionHandler);
    }

    public void refreshList(String jsonUsersArray) {
        Log.d(LOG_TAG, "Refreshing user list");
        try {
            List<User> users = objectMapper.readValue(jsonUsersArray,
                    TypeFactory.defaultInstance().constructType(List.class, User.class));
            adapter.setUsers(users);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing users json", e);
        }
    }

    public void userConnected(String jsonUser) {
        Log.d(LOG_TAG, "Adding new user");
        try {
            User user = objectMapper.readValue(jsonUser, User.class);
            adapter.addUser(user);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        }
    }

    public void userDisconnected(String jsonUser) {
        Log.d(LOG_TAG, "Removing user from list");
        try {
            User user = objectMapper.readValue(jsonUser, User.class);
            adapter.deleteUser(user);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        }

    }
}
