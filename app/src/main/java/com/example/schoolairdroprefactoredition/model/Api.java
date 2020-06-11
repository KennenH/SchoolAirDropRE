package com.example.schoolairdroprefactoredition.model;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainNews;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 网络数据接口类
 */
public interface Api {

    /**
     * 获取附近在售的商品
     * @return
     */
    @GET("goods/relative")
    Call<DomainGoodsInfo> getGoodsInfo();

    /**
     * 获取首页最新消息
     * @return
     */
    @GET
    Call<DomainNews> getNews();

    /**
     * 获取历史搜索
     * @return
     */
    Call<DomainSearchItems> getSearchHistory();

    /**
     * 获取历史搜索的匹配和实时热词的建议
     * @return
     */
    Call<DomainSearchItems> getSearchSuggestion();
}
