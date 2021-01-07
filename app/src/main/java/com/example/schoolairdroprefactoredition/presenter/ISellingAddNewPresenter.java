package com.example.schoolairdroprefactoredition.presenter;

import android.content.Context;

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
     * @param title       物品标题
     * @param description 物品描述
     * @param longitude   经度
     * @param latitude    纬度
     * @param isBrandNew  是否全新
     * @param isQuotable  是否可议价
     * @param price       价格
     */
    void submitNewItem(String token,
                       String cover,
                       List<String> picSet,
                       String title,
                       String description,
                       double longitude,
                       double latitude,
                       boolean isBrandNew,
                       boolean isQuotable,
                       float price,
                       Context context);

    /**
     * 修改物品信息
     *
     * @param token       token
     * @param cover       封面
     * @param picSet      图片集
     * @param title       物品标题
     * @param description 物品描述
     * @param longitude   经度
     * @param latitude    纬度
     * @param isBrandNew  是否全新
     * @param isQuotable  是否可议价
     * @param price       价格
     */
    void updateItemInfo(String token,
                        String cover,
                        List<String> picSet,
                        String title,
                        String description,
                        double longitude,
                        double latitude,
                        boolean isBrandNew,
                        boolean isQuotable,
                        float price,
                        Context context);

    /**
     * 提交新的帖子
     *
     * @param token   token
     * @param cover   封面
     * @param hwRatio 封面的高宽比
     * @param picSet  图片集
     * @param title   标题
     * @param content 正文内容
     */
    void submitNewPost(String token,
                       String cover,
                       float hwRatio,
                       List<String> picSet,
                       String title,
                       String content,
                       double longitude, double latitude,
                       Context context);

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
    void saveItemDraft(String cover,
                       List<LocalMedia> picSet,
                       String title,
                       String description,
                       String price,
                       boolean isQuotable,
                       boolean isSecondHand);

    /**
     * 保存帖子草稿
     *
     * @param cover     封面
     * @param hwRatio   高与宽之比
     * @param picSet    图片集
     * @param tag       话题标签
     * @param title     标题
     * @param anonymous 是否匿名
     * @param content   正文内容
     */
    void savePostDraft(String cover,
                       float hwRatio,
                       List<LocalMedia> picSet,
                       String tag,
                       boolean anonymous,
                       String title,
                       String content);

    /**
     * 删除物品草稿
     */
    void deleteItemDraft();

    /**
     * 删除帖子草稿
     */
    void deletePostDraft();

    /**
     * 恢复物品草稿
     */
    void restoreItemDraft();

    /**
     * 恢复帖子草稿
     */
    void restorePostDraft();
}
