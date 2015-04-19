package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.mkoi.over9000.adapter.FriendListAdapter;
import com.mkoi.over9000.http.RestClient;
import com.mkoi.over9000.message.response.FriendResponse;
import com.mkoi.over9000.model.Friend;
import com.mkoi.over9000.preferences.UserPreferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

@SuppressLint("Registered")
@EActivity(R.layout.activity_friends)
@OptionsMenu(R.menu.menu_friends)
public class FriendsActivity extends Activity {
    public static final String LOG_TAG = "FriendsActivity";

    @ViewById
    ListView friendList;

    @ViewById
    EditText emailSearchText;

    @Bean
    FriendListAdapter adapter;

    @RestService
    RestClient restClient;

    @Pref
    UserPreferences_ userPreferences;

    @AfterViews
    public void bindAdapter() {
        friendList.setAdapter(adapter);
    }

    @Click(R.id.searchButton)
    public void searchClick(View view) {
        if(!emailSearchText.getText().toString().equals("")) {
            searchFriend(emailSearchText.getText().toString());
        } else {
            emailSearchText.setError("Podaj email");
        }
    }

    @Background
    public void searchFriend(String email) {
        FriendResponse result = restClient.searchFriends(email);
        searchResult(result.getFriends());
    }

    @UiThread
    public void searchResult(List<Friend> friends) {
        Log.d(LOG_TAG, "Users found: "+friends.size());
        adapter.setFriends(friends);
    }

    @AfterViews
    @Background
    public void refreshFriends() {
        Log.d(LOG_TAG, "Refreshing friends list");
        String authHeader = "Bearer "+userPreferences.token().get();
        Log.d(LOG_TAG, "Header: "+authHeader);
        restClient.setHeader("Authorization", authHeader);
        FriendResponse result = restClient.getFriends();
        refreshFriendsResult(result.getFriends());
    }

    @UiThread
    public void refreshFriendsResult(List<Friend> friends) {
        Log.d(LOG_TAG, "Friends downloaded: "+friends.size());
        adapter.setFriends(friends);
    }

    @OptionsItem(R.id.action_search)
    public void handleSearch() {
        Intent intent = new Intent(FriendsActivity.this, SearchActivity_.class);
        startActivity(intent);
    }
}
