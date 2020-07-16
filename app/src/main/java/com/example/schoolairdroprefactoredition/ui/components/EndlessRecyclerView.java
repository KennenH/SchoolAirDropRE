package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

/**
 * 自动加载数据、调整了tap to top滑动速度
 * 的recycler view
 */
public class EndlessRecyclerView extends RecyclerView {

    int pastVisibleItem, visibleItemCount, totalItemCount;

    int[] pastVisibleItems = new int[100];

    private boolean loading = false;// 正在加载标志

    private LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;

    public EndlessRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public EndlessRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        void autoLoadMore(EndlessRecyclerView recycler);
    }

    /**
     * 设置自动加载接口
     *
     * @param onLoadMoreListener 自动加载接口
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
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

