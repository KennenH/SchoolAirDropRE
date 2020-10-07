package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;

import java.util.List;

public interface IQuoteSectionsCallback extends IBaseCallback {
    void onQuoteReceivedLoaded(List<DomainGoodsInfo.DataBean> received);

    void onQuoteSentLoaded(List<DomainGoodsInfo.DataBean> sent);
}
