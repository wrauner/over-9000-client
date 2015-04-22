package com.mkoi.over9000.message.response;

import com.mkoi.over9000.model.SearchResultUser;

import java.util.List;

/**
 * @author Wojciech Rauner
 */
public class SearchResponse {
    private List<SearchResultUser> searchResultUsers;

    public List<SearchResultUser> getSearchResultUsers() {
        return searchResultUsers;
    }

    public void setSearchResultUsers(List<SearchResultUser> searchResultUsers) {
        this.searchResultUsers = searchResultUsers;
    }
}
