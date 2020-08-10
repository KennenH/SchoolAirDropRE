package com.example.schoolairdroprefactoredition.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.SheetActionItem;
import com.example.schoolairdroprefactoredition.utils.MyUtil;

public class SheetActionPagerAdapter extends PagerAdapter {
    private final int MARGIN = MyUtil.averageItemMargin(4, SizeUtils.dp2px(56));

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        GridLayout page = new GridLayout(container.getContext());
        page.setPadding(MARGIN, 0, MARGIN, 0);
        page.setRowCount(1);
        page.setColumnCount(4);

        for (int i = 0; i < 4; i++) {
            SheetActionItem item = new SheetActionItem(container.getContext());
            item.setImageDrawable(container.getContext().getResources().getDrawable(R.drawable.email));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(i, 1));
            params.setMargins(MARGIN, 0, MARGIN, 0);
            item.setLayoutParams(params);
            page.addView(item);
        }

        if (page.getParent() instanceof ViewGroup)
            ((ViewGroup) page.getParent()).removeView(page);

        container.addView(page);

        return page;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
