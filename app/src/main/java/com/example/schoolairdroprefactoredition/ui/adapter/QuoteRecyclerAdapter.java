package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.databean.TestQuoteReceiveItemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuoteRecyclerAdapter extends BaseQuickAdapter<TestQuoteReceiveItemBean, BaseViewHolder> implements View.OnClickListener {

    public QuoteRecyclerAdapter(@Nullable List<TestQuoteReceiveItemBean> data) {
        super(R.layout.item_quote, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, TestQuoteReceiveItemBean bean) {
        TextView pending = holder.itemView.findViewById(R.id.handle_waiting);
        ImageView result = holder.itemView.findViewById(R.id.handle_result);

        if (!bean.isHandled()) {
            result.setVisibility(View.GONE);
            pending.setVisibility(View.VISIBLE);
        } else {
            int resultCode = bean.getResult();
            result.setScaleType(ImageView.ScaleType.CENTER);
            if (resultCode == TestQuoteReceiveItemBean.ACCEPT) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_accepted, getContext().getTheme()));
            } else if (resultCode == TestQuoteReceiveItemBean.REFUSE) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_refuse, getContext().getTheme()));
            } else if (resultCode == TestQuoteReceiveItemBean.OUT_OF_DATE) {
                result.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_quote_out_of_date, getContext().getTheme()));
            }
            pending.setVisibility(View.GONE);
            result.setVisibility(View.VISIBLE);
        }

        holder.itemView.findViewById(R.id.user_avatar).setOnClickListener(this);
        holder.itemView.findViewById(R.id.user_name).setOnClickListener(this);
    }

    @Override
    protected void setOnItemClick(@NotNull View v, int position) {
        // open quote detail page
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_name || id == R.id.user_avatar) {
            // open user detail page
        }
    }
}
