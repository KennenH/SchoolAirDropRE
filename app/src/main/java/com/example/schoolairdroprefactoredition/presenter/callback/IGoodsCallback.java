package com.example.schoolairdroprefactoredition.presenter.callback;

public interface IGoodsCallback extends IBaseCallback {
    void onQuoteSuccess();

    void onFavoriteSuccess();

    void onIsFavorGet(boolean isFavor);

    void onUnFavorSuccess();
}
