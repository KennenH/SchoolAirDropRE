package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemQuoteBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainQuote;
import com.example.schoolairdroprefactoredition.scene.quote.fragment.QuoteFragment;
import com.example.schoolairdroprefactoredition.scene.user.UserActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

public class QuoteRecyclerAdapter extends BaseQuickAdapter<DomainQuote.DataBean, BaseViewHolder> {
    private Bundle bundle;

    private OnQuoteActionListener mOnQuoteActionListener;

    public QuoteRecyclerAdapter(Bundle bundle) {
        super(R.layout.item_quote);
        this.bundle = bundle;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainQuote.DataBean bean) {
        final int pageNum = bundle.getInt(ConstantUtil.FRAGMENT_NUM);

        try {
            ItemQuoteBinding binding = ItemQuoteBinding.bind(holder.itemView);

            if (bean.getSender_info() != null) {
                ImageUtil.loadRoundedImage(binding.userAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getSender_info().getUser_img_path());
                binding.userName.setText(bean.getSender_info().getUname());
            } else if (bean.getReceiver_info() != null) {
                ImageUtil.loadRoundedImage(binding.userAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getReceiver_info().getUser_img_path());
                binding.userName.setText(bean.getReceiver_info().getUname());
            }

            ImageUtil.loadRoundedImage(binding.goodsAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getGoods().getGoods_img_cover());
            binding.goodsOriginPrice.setPrice(bean.getGoods().getGoods_price());
            binding.quotePrice.setPrice(bean.getQuote_price());

            if (pageNum == QuoteFragment.PAGE_SENT) {
                binding.quoteRefuse.setVisibility(View.GONE);
                binding.quoteAccept.setVisibility(View.GONE);
            }

            switch (bean.getState()) {
                case 0:
                    binding.status.setText(getContext().getString(R.string.pending));
                    break;
                case 1:
                    binding.status.setText(getContext().getString(R.string.accepted));
                    binding.quoteAccept.setVisibility(View.GONE);
                    binding.quoteRefuse.setVisibility(View.GONE);
                    break;
                case 2:
                    binding.status.setText(getContext().getString(R.string.rejected));
                    binding.quoteAccept.setVisibility(View.GONE);
                    binding.quoteRefuse.setVisibility(View.GONE);
                    break;
            }

            boolean negotiable = bean.getGoods().getGoods_is_quotable() == 1;
            boolean secondHand = bean.getGoods().getGoods_is_brandNew() == 0;
            if (negotiable && secondHand)
                binding.goodsTitle.setText(getContext().getResources().getString(R.string.itemNSs, bean.getGoods().getGoods_name()));
            else if (negotiable)
                binding.goodsTitle.setText(getContext().getResources().getString(R.string.itemNs, bean.getGoods().getGoods_name()));
            else if (secondHand)
                binding.goodsTitle.setText(getContext().getResources().getString(R.string.itemSs, bean.getGoods().getGoods_name()));
            else
                binding.goodsTitle.setText(bean.getGoods().getGoods_name());

            binding.userAvatar.setOnClickListener(v -> {
                UserActivity.start(getContext(), false,
                        (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE),
                        bean.getReceiver_info() == null ? bean.getSender_info() : bean.getReceiver_info());
            });
            binding.userName.setOnClickListener(v -> {
                UserActivity.start(getContext(), false,
                        (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE),
                        bean.getReceiver_info() == null ? bean.getSender_info() : bean.getReceiver_info());
            });
            binding.quoteAccept.setOnClickListener(v -> {
                if (pageNum == QuoteFragment.PAGE_RECEIVED && mOnQuoteActionListener != null)
                    mOnQuoteActionListener.onQuoteAccept(bean.getQuote_id());
            });
            binding.quoteRefuse.setOnClickListener(v -> {
                if (pageNum == QuoteFragment.PAGE_RECEIVED && mOnQuoteActionListener != null)
                    mOnQuoteActionListener.onQuoteRefuse(bean.getQuote_id());
            });

            binding.getRoot().setOnClickListener(v -> {
//            QuoteDetailActivity.start(getContext());
            });
        } catch (NullPointerException e) {
            LogUtils.d("null");
        }
    }

    public interface OnQuoteActionListener {
        void onQuoteAccept(String quoteID);

        void onQuoteRefuse(String quoteID);
    }

    public void setOnQuoteActionListener(OnQuoteActionListener listener) {
        mOnQuoteActionListener = listener;
    }

}
