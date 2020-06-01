package com.example.schoolairdroprefactoredition.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.GoodsActivity;
import com.example.schoolairdroprefactoredition.ui.components.GoodsPopularity;
import com.example.schoolairdroprefactoredition.ui.components.RecyclerItemData;
import com.example.schoolairdroprefactoredition.ui.components.Tags;
import com.google.android.flexbox.FlexboxLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * 自动加载数据、调整了tap to top滑动速度
 * 的recycler view
 */
public class HomeRecycler extends RecyclerView {

    private float loadFactor = 0.75f;// 加载因子，可见item位置 > 总数与因子乘积的位置 将加载新数据
    int pastVisibleItem, visibleItemCount, totalItemCount;

    int[] pastVisibleItems = new int[100];

    private boolean loading = false;// 正在加载标志

    private LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;

    public HomeRecycler(@NonNull Context context) {
        this(context, null);
    }

    public HomeRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);
        mLayoutManager = layout;
    }

    /**
     * 当向下滑动时检测当前可见item的位置是否大于等于加载因子与总item数之积
     * 并加载新数据，以loading为加载标志，防止重复加载
     * 加载完毕后外部必须调用 {@link #loadingFinished()}
     *
     * @param dy 垂直方向的滑动
     */
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (dy > 0) //check for scroll down
        {
            visibleItemCount = mLayoutManager.getChildCount();// 当前可见的item数量
            totalItemCount = mLayoutManager.getItemCount();// adapter中目前装填的item总数

            // pastVisibleItem 为当前第一个可见item的位置
            // 此处pastVisibleItems为临时数组，仅为获取其第一个item位置
            if (mLayoutManager instanceof LinearLayoutManager)
                pastVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(pastVisibleItems);
                pastVisibleItem = pastVisibleItems[0];
            }

            // 是否正在加载
            if (!loading) {
                // 是否需要加载新数据
                if ((visibleItemCount + pastVisibleItem) >= totalItemCount * loadFactor) {
                    // 正在加载标志，防止重复加载
                    loading = true;
                    // 加载新数据
                    if (mOnLoadMoreListener != null)
                        mOnLoadMoreListener.autoLoadMore(this);
                }
            }
        }
    }

    /**
     * 平滑滚动
     *
     * @param position 为0时即滑动至最顶部
     */
    public void smoothScrollToPosition(int position) {
        if (mLayoutManager != null) {
            FasterSmoothScroller mSmoothScroller = new FasterSmoothScroller(getContext());

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] temp = new int[100];
                ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(temp);
                if (temp[0] == 0)
                    return;
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                if (((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition() == 0)
                    return;
            }

            mSmoothScroller.setTargetPosition(position);
            mLayoutManager.startSmoothScroll(mSmoothScroller);
        }
    }

    /**
     * 数据加载完毕时必须调用此方法,否则之后不会自动加载数据
     * 自动加载参看{@link #onScrolled}
     */
    public void loadingFinished() {
        loading = false;
    }

    /**
     * 设置加载因子
     * 可见item位置 > 总数与因子乘积的位置 将加载新数据
     *
     * @param factor 因子
     * @throws Exception factor为介于 0 到 1 的float
     */
    public void setLoadFactor(float factor) throws Exception {
        if (factor > 0f && factor < 1f)
            loadFactor = factor;
        else throw new Exception("factor could not be larger than 1 or less than 0.");
    }

    /**
     * 数据自动加载接口
     * 数据加载完毕务必调用 {@link #loadingFinished()}
     */
    public interface OnLoadMoreListener {
        void autoLoadMore(HomeRecycler recycler);
    }

    /**
     * 设置自动加载接口
     *
     * @param onLoadMoreListener 自动加载接口
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }


    /**
     * 与HomeRecycler配对的Adapter
     */
    public static class HomeRecyclerAdapter extends BaseQuickAdapter<RecyclerItemData, BaseViewHolder> implements OnClickListener {
        HomeRecyclerAdapter() {
            super(R.layout.item_home_light);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, RecyclerItemData item) {
            Glide.with(getContext())
                    .load(item.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
                    .into((ImageView) holder.itemView.findViewById(R.id.item_image));

            holder.setText(R.id.item_title, item.getTitle());
            GoodsPopularity popularity = holder.itemView.findViewById(R.id.item_popularity);
            FlexboxLayout tagsWrapper = holder.itemView.findViewById(R.id.item_tags);
            tagsWrapper.removeAllViews();
            for (Integer tag : item.getTags()) {
                tagsWrapper.addView(new Tags(getContext(), tag));
            }
            popularity.setComments(item.getComments());
            popularity.setLikes(item.getLikes());
            popularity.setWatches(item.getWatches());

            holder.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), GoodsActivity.class);
            getContext().startActivity(intent);
        }
    }

    static class FasterSmoothScroller extends LinearSmoothScroller {

        FasterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            // 此函数计算滚动dx的距离需要多久，当要滚动的距离很大时，比如说52000，
            // 经测试，系统会多次调用此函数，每10000距离调一次，所以总的滚动时间
            // 是多次调用此函数返回的时间的和，所以修改每次调用该函数时返回的时间的
            // 大小就可以影响滚动需要的总时间，可以直接修改些函数的返回值，也可以修改
            // dx的值
            if (dx == 10000) {
                return 200;
            } else if (dx > 5000)
                dx = 5000;
            return super.calculateTimeForScrolling(dx);
        }
    }
}

