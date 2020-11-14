package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainOnGoing;

public interface IOnGoingCallback extends IBaseCallback {
    void onEventMySentLoaded(DomainOnGoing data);

    void onEventMyReceivedLoaded(DomainOnGoing data);
}
