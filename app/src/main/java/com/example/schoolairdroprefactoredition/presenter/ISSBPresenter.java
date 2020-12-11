package com.example.schoolairdroprefactoredition.presenter;

import com.example.schoolairdroprefactoredition.presenter.callback.ISSBCallback;

public interface ISSBPresenter extends IBasePresenter<ISSBCallback> {

    /**
     * 获取自己的在售列表
     */
    void getSellingList(String token, int page);

    /**
     * 下架物品
     */
    void unListItem(String token, String goodsID);

    /**
     * 查看他人的在售物品页面
     */
    void getSellingByUID(int userID);

    /**
     * 获取帖子
     */
    void getPosts(String token, int page);


    /**
     * 删除帖子
     */
    void deletePost(String token, String postID);
}
