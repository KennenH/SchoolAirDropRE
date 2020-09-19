package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;

import java.util.List;

public interface ISellingAddNewPresenter extends IBasePresenter<ISellingAddNewCallback> {
    void submit(String token,
                String cover,
                List<String> picSet,
                String name,
                String description,
                double longitude,
                double latitude,
                boolean isBrandNew,
                boolean isQuotable,
                float price);

}
