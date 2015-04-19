package com.mkoi.over9000.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkoi.over9000.model.SearchResultUser;
import com.mkoi.over9000.view.SearchResultView;
import com.mkoi.over9000.view.SearchResultView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * @author Wojciech Rauner
 */
@EBean
public class SearchListAdapter extends BaseAdapter {
    ArrayList<SearchResultUser> searchResult = new ArrayList<>();

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return searchResult.size();
    }

    @Override
    public SearchResultUser getItem(int i) {
        return searchResult.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchResultView searchResultView;
        if(view == null) {
            searchResultView = SearchResultView_.build(context);
        } else {
            searchResultView = (SearchResultView) view;
        }
        searchResultView.bind(getItem(i));
        return searchResultView;
    }
}
