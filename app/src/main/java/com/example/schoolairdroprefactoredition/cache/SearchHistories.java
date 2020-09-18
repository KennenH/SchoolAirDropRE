package com.example.schoolairdroprefactoredition.cache;

import java.util.List;

public class SearchHistories {
    public static final String SEARCH_HISTORY = "search_history";

    private List<String> historyList;

    public List<String> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<String> historyList) {
        this.historyList = historyList;
    }
}
