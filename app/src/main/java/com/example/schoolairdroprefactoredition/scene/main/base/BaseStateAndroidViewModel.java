package com.example.schoolairdroprefactoredition.scene.main.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BaseStateAndroidViewModel extends AndroidViewModel {
    public BaseStateAndroidViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 通用请求监听，请求错误和正在加载
     */
    protected BaseStateViewModel.OnRequestListener mOnRequestListener;

    public interface OnRequestListener {
        void onError();
    }

    public void setOnRequestListener(BaseStateViewModel.OnRequestListener listener) {
        mOnRequestListener = listener;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mOnRequestListener != null)
            mOnRequestListener = null;
    }
}
