package com.example.schoolairdroprefactoredition.fragment.home;

import androidx.lifecycle.ViewModel;

public class BaseChildFragmentViewModel extends ViewModel {
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
