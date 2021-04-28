package com.example.schoolairdroprefactoredition.scene.main.home

import android.widget.Toast
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.adapter.PurchasingRecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.PurchasingViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout

class PurchasingFragment : BaseChildFragment(), BaseFooterAdapter.OnNoMoreDataListener {

    companion object {
        private var INSTANCE: PurchasingFragment? = null
        fun getInstance(): PurchasingFragment {
            return INSTANCE ?: PurchasingFragment().also {
                INSTANCE = it
            }
        }
    }

    private val purchasingViewModel by lazy {
        PurchasingViewModel.PurchasingViewModelFactory((activity?.application as SAApplication).databaseRepository).create(PurchasingViewModel::class.java)
    }

    private var mEndlessRecyclerView: EndlessRecyclerView? = null

    private val purchasingRecyclerAdapter by lazy {
        PurchasingRecyclerAdapter()
    }

    private val mManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun initView(binding: FragmentHomeContentBinding?) {
        purchasingRecyclerAdapter.setOnNoMoreDataListener(this)
        mEndlessRecyclerView = binding?.homeRecycler
        mEndlessRecyclerView?.apply {
            layoutManager = mManager
            adapter = purchasingRecyclerAdapter
            setOnLoadMoreListener(this@PurchasingFragment)
        }

        binding?.homeRefresh?.setOnRefreshListener(this)
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     */
    fun scrollToTop() {
        if (mManager.findLastVisibleItemPosition() > 10) {
            mEndlessRecyclerView?.scrollToPosition(5)
        }
        mEndlessRecyclerView?.smoothScrollToPosition(0)
    }

    /**
     * 没有更多数据
     */
    override fun onNoMoreData() {
        mEndlessRecyclerView?.setIsNoMoreData(true)
    }

    /**
     * 上拉加载更多的时候获取到的数据不足默认数量，则表明已经没有更多数据
     */
    override fun resetNoMoreData() {
        mEndlessRecyclerView?.setIsNoMoreData(false)
    }

    /**
     * 从缓存中获取占位数据
     */
    override fun getLocalCache() {
        purchasingViewModel.getPurchasingCache().observeOnce(viewLifecycleOwner) { cache ->
            cache?.data?.size?.let { size ->
                if (size > 0) {
                    purchasingRecyclerAdapter.setList(cache.data)
                    showContentContainer()
                }
            }
        }
    }

    /**
     * 从服务器获取网络数据，仅在页面第一次被加载的时候才被调用
     */
    override fun getOnlineData(aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            if (purchasingRecyclerAdapter.data.isEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME)
            }
            return
        }

        // 获取物品网络数据
        purchasingViewModel.getGoodsInfo(aMapLocation.longitude, aMapLocation.latitude).observeOnce(viewLifecycleOwner) {
            if (it != null) {
                // 如果获取的物品数量为0且当前的adapter也尚没有数据则显示placeholder
                if (it.data.isEmpty() && purchasingRecyclerAdapter.data.isEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_GOODS)
                } else {
                    purchasingRecyclerAdapter.setList(it.data)
                    showContentContainer()
                }
            } else {
                // 如果当前adapter没有数据且加载失败了则显示placeholder
                if (purchasingRecyclerAdapter.data.isEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_ERROR)
                }
            }
        }
    }

    /**
     * 刷新时获取数据的逻辑
     */
    override fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
//            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.errorLocation)
            Toast.makeText(context, R.string.errorLocation, Toast.LENGTH_SHORT).show()
            return
        }
        purchasingViewModel.getGoodsInfo(aMapLocation.longitude, aMapLocation.latitude).observeOnce(viewLifecycleOwner) {
            refreshLayout.finishRefresh()
            showContentContainer()
            if (it != null) {
                purchasingRecyclerAdapter.setList(it.data)
            } else {
//                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
                Toast.makeText(context, R.string.systemBusy, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 上拉加载更多的数据获取逻辑
     */
    override fun getAutoLoadMoreData(recycler: EndlessRecyclerView, aMapLocation: AMapLocation?) {
        purchasingViewModel.getGoodsInfo().observeOnce(viewLifecycleOwner) {
            recycler.finishLoading()
            showContentContainer()
            if (it != null) {
                purchasingRecyclerAdapter.addData(it.data)
            } else {
//                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
                Toast.makeText(context, R.string.systemBusy, Toast.LENGTH_SHORT).show()
            }
        }
    }
}