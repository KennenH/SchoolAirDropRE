package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 网络数据接口类
 */
public interface Api {

    /**
     * 获取附近在售的商品
     *
     * @return
     */
    @GET("goods/relative")
    Call<TestGoodsItemBean> getGoodsInfo();

    /**
     * 获取首页最新消息
     *
     * @return
     */
    @GET
    Call<TestNewsItemBean> getNews();

    /**
     * 获取历史搜索
     *
     * @return
     */
    Call<DomainSearchItems> getSearchHistory();

    /**
     * 获取历史搜索的匹配和实时热词的建议
     *
     * @return
     */
    Call<DomainSearchItems> getSearchSuggestion();

    /**
     * 获取搜索商品结果
     *
     * @return
     */
    Call<TestGoodsItemBean> getSearchResult();
}
