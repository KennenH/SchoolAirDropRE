package com.example.schoolairdroprefactoredition.presenter.callback;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;

public interface ILikesCallback extends IBaseCallback {
    void onLikesLoaded(DomainGoodsInfo likes);
}
