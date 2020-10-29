package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;

import java.util.List;

public interface IOnGoingCallback extends IBaseCallback {
    void onEventMySentLoaded(List<DomainOnGoing.Data> data);

    void onEventMyReceivedLoaded(List<DomainOnGoing.Data> data);
}
