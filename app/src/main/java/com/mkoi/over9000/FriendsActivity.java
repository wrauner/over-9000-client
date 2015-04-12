package com.mkoi.over9000;

import android.app.Activity;
import android.widget.ListView;

import com.mkoi.over9000.adapter.FriendListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_friends)
public class FriendsActivity extends Activity {

    @ViewById
    ListView friendList;

    @Bean
    FriendListAdapter adapter;

    @AfterViews
    public void bindAdapter() {
        friendList.setAdapter(adapter);
    }
}
