package com.example.schoolairdroprefactoredition.fragment.home;

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
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import org.jetbrains.annotations.NotNull;

/**
 * 自动加载数据、调整了tap to top滑动速度
 * 的recycler view
 */
public class HomeRecycler extends RecyclerView {

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
     * 当向下滑动时检测剩余已加载的item数量是否小于一个定值
     * 并加载新数据，以loading为加载标志，防止同时重复加载
     * 加载完毕后外部必须调用 {@link #finishLoading()}
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
                if (totalItemCount - pastVisibleItem < ConstantUtil.DATA_FETCH_DEFAULT_SIZE / 2) {
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
     * {@link #onScrolled}
     */
    public void finishLoading() {
        loading = false;
    }

    /**
     * 数据自动加载接口
     * 数据加载完毕务必调用 {@link #finishLoading()}
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
     * 附近在售列表的adapter
     */
    public static class HomeNearbyRecyclerAdapter extends BaseQuickAdapter<TestGoodsItemBean, BaseViewHolder> implements OnClickListener {
        HomeNearbyRecyclerAdapter() {
            super(R.layout.item_home_goods_info);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, TestGoodsItemBean item) {
            if (item != null) {
                Glide.with(getContext())
                        .load(item.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(getContext().getResources().getDrawable(R.drawable.logo_120x, getContext().getTheme()))
                        .dontTransform()
                        .into((ImageView) holder.itemView.findViewById(R.id.item_image));

                holder.itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), GoodsActivity.class);
            getContext().startActivity(intent);
        }
    }

    /**
     * 新闻列表的adapter
     * todo 将BaseQuickAdapter内的泛型改为新闻bean类
     */
    public static class HomeNewsRecyclerAdapter extends BaseQuickAdapter<TestNewsItemBean, BaseViewHolder> implements OnClickListener {
        HomeNewsRecyclerAdapter() {
            super(R.layout.item_home_news);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, TestNewsItemBean item) {
            if (item != null) {
                Glide.with(getContext())
                        .load(item.getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(getContext().getResources().getDrawable(R.drawable.logo_120x, getContext().getTheme()))
                        .dontTransform()
                        .into((ImageView) holder.itemView.findViewById(R.id.news_cover));

                holder.setText(R.id.news_title, item.getTitle());
                holder.setText(R.id.news_day, item.getDay());
                holder.setText(R.id.news_month, item.getMonth());
                holder.setText(R.id.news_sender, item.getSender());

                holder.itemView.setOnClickListener(this);
            }
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

