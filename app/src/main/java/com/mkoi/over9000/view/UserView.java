package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkoi.android_identicons.Identicon;
import com.mkoi.over9000.R;
import com.mkoi.over9000.model.User;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.friend_view)
public class UserView extends RelativeLayout{

    @ViewById
    TextView userNick;

    @ViewById
    Identicon userAvatar;

    public UserView(Context context) {
        super(context);
    }

    public void bind(User user) {
        userNick.setText(user.getNick());
        userAvatar.show(user.getNick());
    }
}
