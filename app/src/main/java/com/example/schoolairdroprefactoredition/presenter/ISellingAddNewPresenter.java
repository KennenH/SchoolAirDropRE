package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public interface ISellingAddNewPresenter extends IBasePresenter<ISellingAddNewCallback> {
    /**
     * 提交新的物品
     *
     * @param token       token
     * @param cover       封面
     * @param picSet      图片集
     * @param name        物品标题
     * @param description 物品描述
     * @param longitude   经度
     * @param latitude    纬度
     * @param isBrandNew  是否全新
     * @param isQuotable  是否可议价
     * @param price       价格
     */
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

    /**
     * 保存用户草稿
     *
     * @param cover        封面
     * @param picSet       图片集
     * @param title        标题
     * @param description  描述
     * @param price        价格
     * @param isQuotable   是否可议价
     * @param isSecondHand 是否二手
     */
    void save(String cover,
              List<LocalMedia> picSet,
              String title,
              String description,
              String price,
              boolean isQuotable,
              boolean isSecondHand);

    /**
     * 删除草稿
     */
    void deleteDraft();

    /**
     * 恢复草稿回调
     */
    void recoverDraft();
}
