package com.example.schoolairdroprefactoredition.scene.main.base;

import androidx.lifecycle.ViewModel;

public class BaseChildFragmentViewModel extends ViewModel {
    /**
     * 通用请求监听，请求错误和正在加载
     */
    protected OnRequestListener mOnRequestListener;

    public interface OnRequestListener {
        void onError();

        void onLoading();
    }

    public void setOnRequestListener(OnRequestListener listener) {
        mOnRequestListener = listener;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mOnRequestListener != null)
            mOnRequestListener = null;
    }
}
