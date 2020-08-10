package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.model.databean.TestCreditBean;
import com.example.schoolairdroprefactoredition.model.databean.TestCreditHistory;

import java.util.List;

public interface ICreditCallback {
    void onCreditLoaded(TestCreditBean bean);

    void onCreditHistoryLoaded(List<TestCreditHistory> list);
}
