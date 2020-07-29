package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.quote.QuoteDetailActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteSectionItemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class QuoteRecyclerAdapter extends BaseQuickAdapter<TestQuoteSectionItemBean, BaseViewHolder> {

    public QuoteRecyclerAdapter() {
        super(R.layout.item_quote);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestQuoteSectionItemBean bean) {
        TextView pending = holder.itemView.findViewById(R.id.handle_waiting);
        ImageView result = holder.itemView.findViewById(R.id.handle_result);

        if (!bean.isHandled()) {
            result.setVisibility(View.GONE);
            pending.setVisibility(View.VISIBLE);
        } else {
            int resultCode = bean.getResult();
            result.setScaleType(ImageView.ScaleType.CENTER);
            if (resultCode == TestQuoteSectionItemBean.ACCEPT) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_accepted, getContext().getTheme()));
            } else if (resultCode == TestQuoteSectionItemBean.REFUSE) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_rejected, getContext().getTheme()));
            } else if (resultCode == TestQuoteSectionItemBean.OUT_OF_DATE) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_overdue, getContext().getTheme()));
            }
            pending.setVisibility(View.GONE);
            result.setVisibility(View.VISIBLE);
        }

        holder.itemView.findViewById(R.id.user_avatar).setOnClickListener(v -> {
            // todo 以用户唯一标识号为参数开启用户个人信息页面
        });
        holder.itemView.findViewById(R.id.user_name).setOnClickListener(v -> {
            // todo 以用户唯一标识号为参数开启用户个人信息页面
        });
        holder.itemView.findViewById(R.id.detail).setOnClickListener(v -> {
            // todo 以报价单唯一标识号为参数开启报价详情页面
            QuoteDetailActivity.start(getContext());
        });
    }
}
