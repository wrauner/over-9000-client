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
 * Widok użytkownika na liście
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.friend_view)
public class UserView extends RelativeLayout{

    @ViewById
    TextView userNick;

    @ViewById
    Identicon userAvatar;

    /**
     * Wymagany konstruktor
     * @param context
     */
    public UserView(Context context) {
        super(context);
    }

    /**
     * Podpięcie użytkownika pod widok
     * @param user użytkownik
     */
    public void bind(User user) {
        userNick.setText(user.getNick());
        userAvatar.show(user.getNick());
    }
}
