package com.example.schoolairdroprefactoredition.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchSuggestionRecyclerAdapter extends BaseQuickAdapter<SearchSuggestionBean, BaseViewHolder> {
    public SearchSuggestionRecyclerAdapter() {
        super(R.layout.item_search_suggestion);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SearchSuggestionBean item) {
        if (item != null) {
            holder.setText(R.id.search_suggestion_title, item.getTitle());
            holder.setImageDrawable(R.id.search_suggestion_icon, getContext().getDrawable(R.drawable.round_search_24));
        }
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
