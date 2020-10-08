package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPrice;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

public class SSBAdapter extends BaseQuickAdapter<DomainGoodsInfo.DataBean, BaseViewHolder> {

    private OnSSBItemActionListener mOnSSBItemActionListener;

    public SSBAdapter() {
        super(R.layout.item_ssb);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.DataBean bean) {
        if (bean != null) {
            final boolean isQuotable = bean.getGoods_is_quotable() == 1;
            final boolean isSecondHand = bean.getGoods_is_brandNew() == 0;

            if (isQuotable && isSecondHand)
                holder.setText(R.id.ssb_item_title, getContext().getString(R.string.itemNSs, bean.getGoods_name()));
            else if (isQuotable)
                holder.setText(R.id.ssb_item_title, getContext().getString(R.string.itemNs, bean.getGoods_name()));
            else
                holder.setText(R.id.ssb_item_title, getContext().getString(R.string.itemSs, bean.getGoods_name()));

            ImageUtil.scaledImageLoad(holder.itemView.findViewById(R.id.ssb_item_img), ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getGoods_img_cover(), SizeUtils.dp2px(85));
            ((GoodsPrice) holder.itemView.findViewById(R.id.ssb_item_price)).setPrice(bean.getGoods_price());
        }

        if (getContext() instanceof AppCompatActivity)
            holder.itemView.setOnClickListener(v -> GoodsActivity.start(getContext(), ((AppCompatActivity) getContext()).getIntent().getExtras(), bean));

        holder.itemView.findViewById(R.id.ssb_item_more_action).setOnClickListener(v -> {
            // 弹出更多动作的弹窗
            if (mOnSSBItemActionListener != null)
                mOnSSBItemActionListener.onItemActionButtonClick(v, bean);
        });
    }

    public interface OnSSBItemActionListener {
        void onItemActionButtonClick(View view, DomainGoodsInfo.DataBean bean);
    }

    public void setOnSSBItemActionListener(OnSSBItemActionListener listener) {
        mOnSSBItemActionListener = listener;
    }
}
