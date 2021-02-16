package com.example.schoolairdroprefactoredition.scene.main.home

import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.ui.adapter.BaseFooterAdapter
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.adapter.HomeGoodsRecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.PurchasingViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout

class HomePurchasingFragment : BaseChildFragment(), BaseFooterAdapter.OnNoMoreDataListener {

    companion object {
        fun newInstance(): HomePurchasingFragment {
            return HomePurchasingFragment()
        }
    }

    private val purchasingViewModel by lazy {
        ViewModelProvider(this).get(PurchasingViewModel::class.java)
    }

    private var mEndlessRecyclerView: EndlessRecyclerView? = null

    private val mHomeGoodsRecyclerAdapter by lazy {
        HomeGoodsRecyclerAdapter()
    }

    private val mManager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun initView(binding: FragmentHomeContentBinding?) {
        mHomeGoodsRecyclerAdapter.setOnNoMoreDataListener(this)
        mEndlessRecyclerView = binding?.homeRecycler
        mEndlessRecyclerView?.apply {
            layoutManager = mManager
            adapter = mHomeGoodsRecyclerAdapter
            setOnLoadMoreListener(this@HomePurchasingFragment)
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
     * 刷新列表后
     */
    override fun onNoMoreDataRefresh() {
        mEndlessRecyclerView?.setIsNoMoreData(false)
    }

    override fun getOnlineData(aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME)
            return
        }
        purchasingViewModel.getGoodsInfo(aMapLocation.longitude, aMapLocation.latitude).observe(viewLifecycleOwner, {
            if (it != null) {
                // 回调成功
                if (it.data.isEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_HOME_GOODS)
                } else {
                    mHomeGoodsRecyclerAdapter.setList(it.data)
                    showContentContainer()
                }
            } else {
                // 回调失败，获取到null
                showPlaceHolder(StatePlaceHolder.TYPE_ERROR)
            }
        })
    }

    override fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
            return
        }
        purchasingViewModel.getGoodsInfo(aMapLocation.longitude, aMapLocation.latitude).observe(viewLifecycleOwner, {
            refreshLayout.finishRefresh()
            showContentContainer()
            if (it != null) {
                mHomeGoodsRecyclerAdapter.setList(it.data)
            } else {
                // 回调失败，获取到null
                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
            }
        })
    }

    override fun getAutoLoadMoreData(recycler: EndlessRecyclerView, aMapLocation: AMapLocation?) {
        purchasingViewModel.getGoodsInfo().observe(viewLifecycleOwner, {
            recycler.finishLoading()
            showContentContainer()
            if (it != null) {
                mHomeGoodsRecyclerAdapter.addData(it.data)
            } else {
                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
            }
        })
    }
}