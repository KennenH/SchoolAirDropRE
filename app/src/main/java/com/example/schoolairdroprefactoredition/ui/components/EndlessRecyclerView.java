package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

/**
 * 自动加载数据
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
}

