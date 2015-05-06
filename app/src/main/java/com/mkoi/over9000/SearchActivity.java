package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mkoi.over9000.adapter.SearchListAdapter;
import com.mkoi.over9000.http.RestClient;
import com.mkoi.over9000.message.response.SearchResponse;
import com.mkoi.over9000.message.response.ServerResponse;
import com.mkoi.over9000.model.SearchResultUser;
import com.mkoi.over9000.preferences.UserPreferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

@SuppressLint("Registered")
@EActivity(R.layout.activity_search)
public class SearchActivity extends Activity {

    public static final String LOG_TAG = "Over9000.SearchActivity";

    @ViewById
    ListView searchList;

    @ViewById
    TextView searchResultText;

    @ViewById
    EditText emailSearchText;

    @RestService
    RestClient restClient;

    @Pref
    UserPreferences_ userPreferences;

    @Bean
    SearchListAdapter searchListAdapter;

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
        SearchResponse result = restClient.searchUsers(email);
        searchResult(result.getSearchResultUsers());
    }

    @UiThread
    public void searchResult(List<SearchResultUser> searchResultUsers) {
        Log.d(LOG_TAG, "Users found: " + searchResultUsers.size());
        searchResultText.setVisibility(View.VISIBLE);
        searchListAdapter.setSearchResult(searchResultUsers);
    }

    @AfterViews
    public void bindAdapter() {
        searchList.setAdapter(searchListAdapter);
    }

    @AfterViews
    public void searchAfterEnter() {
        emailSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    searchClick(view);
                    return true;
                }
                return false;
            }
        });
    }

    @ItemClick
    public void searchListItemClicked(SearchResultUser user) {
        Log.d(LOG_TAG, "Friend request send to "+user.getEmail());
        sendFriendRequest(user.getEmail());
    }

    @Background
    public void sendFriendRequest(String email) {
        String authHeader = "Bearer "+userPreferences.token().get();
        restClient.setHeader("Authorization", authHeader);
        ServerResponse response = restClient.friendRequest(email);
        friendRequestResult(response);
    }

    @UiThread
    public void friendRequestResult(ServerResponse result) {
        Log.d(LOG_TAG, "Friend request response "+result.getError()+" "+result.getDescription());
    }
}
