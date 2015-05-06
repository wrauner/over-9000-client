package com.mkoi.over9000.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkoi.android_identicons.Identicon;
import com.mkoi.over9000.R;
import com.mkoi.over9000.model.SearchResultUser;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * @author Wojciech Rauner
 */
@EViewGroup(R.layout.search_result_view)
public class SearchResultView extends RelativeLayout {

    @ViewById
    TextView searchResultName;

    @ViewById
    TextView searchResultEmail;

    @ViewById
    Identicon searchResultAvatar;

    public SearchResultView(Context context) {
        super(context);
    }

    public void bind(SearchResultUser user) {
        searchResultName.setText(user.getName()+" "+user.getLastname());
        searchResultEmail.setText(user.getEmail());
        searchResultAvatar.show(user.getEmail());
    }
}