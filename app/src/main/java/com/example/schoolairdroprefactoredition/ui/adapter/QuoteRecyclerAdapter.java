package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;

public class QuoteRecyclerAdapter extends BaseQuickAdapter<DomainGoodsInfo.DataBean, BaseViewHolder> {
    private Bundle bundle;

    public QuoteRecyclerAdapter(Bundle bundle) {
        super(R.layout.item_quote);
        this.bundle = bundle;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.DataBean bean) {
        holder.itemView.findViewById(R.id.user_avatar).setOnClickListener(v -> {
            startUserActivity(bean);
        });
        holder.itemView.findViewById(R.id.user_name).setOnClickListener(v -> {
            startUserActivity(bean);
        });
        holder.itemView.findViewById(R.id.quote_accept).setOnClickListener(v -> {

        });
        holder.itemView.findViewById(R.id.quote_refuse).setOnClickListener(v -> {

        });
        holder.itemView.setOnClickListener(v -> {
//            QuoteDetailActivity.start(getContext());
        });
    }

    private void startUserActivity(DomainGoodsInfo.DataBean bean) {
        DomainUserInfo.DataBean userInfo = null;
        if (bundle != null)
            userInfo = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        if (userInfo != null && userInfo.getUid() == bean.getSeller_info().getUid()) {
            bundle.putSerializable(ConstantUtil.KEY_GOODS_INFO, bean);
            UserActivity.startForResult(getContext(), bundle,false);
        }
    }
}
