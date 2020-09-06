package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.scene.credit.CreditActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestCreditBean;
import com.example.schoolairdroprefactoredition.model.databean.TestCreditHistory;
import com.example.schoolairdroprefactoredition.presenter.ICreditPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ICreditCallback;
import com.example.schoolairdroprefactoredition.ui.components.CreditAssessment;

import java.util.Arrays;

public class CreditImpl implements ICreditPresenter {

    private ICreditCallback mCallback;

    @Override
    public void getCredit() {
        TestCreditBean bean = new TestCreditBean();
        bean.setCredits(CreditActivity.CREDIT4);
        mCallback.onCreditLoaded(bean);
    }

    @Override
    public void getCreditHistory() {
        TestCreditHistory[] data = new TestCreditHistory[5];
        for (int i = 0; i < 5; i++) data[i] = new TestCreditHistory();
        data[0].setCredits(CreditActivity.CREDIT2);
        data[0].setType(CreditAssessment.TYPE_MONTHLY);
        data[0].setUp(true);
        data[1].setCredits(CreditActivity.CREDIT3);
        data[1].setType(CreditAssessment.TYPE_MONTHLY);
        data[1].setUp(true);
        data[2].setCredits(CreditActivity.CREDIT4);
        data[2].setType(CreditAssessment.TYPE_MONTHLY);
        data[2].setUp(true);
        data[3].setCredits(CreditActivity.CREDIT3);
        data[3].setType(CreditAssessment.TYPE_REPORT);
        data[3].setUp(false);
        data[4].setCredits(CreditActivity.CREDIT4);
        data[4].setType(CreditAssessment.TYPE_MONTHLY);
        data[4].setUp(true);

        mCallback.onCreditHistoryLoaded(Arrays.asList(data));

    }

    @Override
    public void registerCallback(ICreditCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ICreditCallback callback) {
        mCallback = null;
    }
}
