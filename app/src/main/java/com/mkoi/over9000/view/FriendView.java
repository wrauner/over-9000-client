package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkoi.over9000.R;
import com.mkoi.over9000.model.Friend;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.friend_view)
public class FriendView extends RelativeLayout{

    @ViewById
    TextView friendName;

    @ViewById
    TextView friendEmail;

    public FriendView(Context context) {
        super(context);
    }

    public void bind(Friend friend) {
        friendName.setText(friend.getName()+" "+friend.getLastname());
        friendEmail.setText(friend.getEmail());
    }
}
