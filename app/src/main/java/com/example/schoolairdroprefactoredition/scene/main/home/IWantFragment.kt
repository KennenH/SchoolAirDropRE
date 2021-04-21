package com.example.schoolairdroprefactoredition.scene.main.home

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.OnInquiryItemClickListener
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.viewmodel.InquiryViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.example.schoolairdroprefactoredition.scene.idesire.IWantActivity

class IWantFragment : BaseChildFragment(), IWantRecyclerAdapter.OnNoMoreDataListener, OnInquiryItemClickListener {

    companion object {
        fun newInstance(): IWantFragment {
            return IWantFragment()
        }
    }

    private val iWantViewModel by lazy {
        ViewModelProvider(this).get(InquiryViewModel::class.java)
    }

    private val iWantRecyclerAdapter by lazy {
        IWantRecyclerAdapter().also {
            it.setOnNoMoreDataListener(this)
            it.setOnInquiryItemClickListener(this)
        }
    }

    private val mManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    }

    private var binding: FragmentHomeContentBinding? = null

    override fun initView(binding: FragmentHomeContentBinding?) {
        this.binding = binding
        binding?.homeRecycler?.apply {
            setOnLoadMoreListener(this@IWantFragment)
            setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f))
            layoutManager = mManager
//            addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(8f), true))
            adapter = iWantRecyclerAdapter
        }
    }

    override fun getLocalCache() {
        // TODO: 2021/3/5 获取本地缓存
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     */
    fun scrollToTop() {
        val visible = IntArray(mManager.spanCount + 1)
        mManager.findLastVisibleItemPositions(visible)
        if (visible[0] > 10) {
            binding?.homeRecycler?.scrollToPosition(5)
        }
        binding?.homeRecycler?.smoothScrollToPosition(0)
    }

    override fun getOnlineData(aMapLocation: AMapLocation?) {
        iWantViewModel.getInquiry().observeOnce(viewLifecycleOwner, { data: List<BaseIWantEntity> ->
            if (data.isNullOrEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_INQUIRY)
            } else {
                iWantRecyclerAdapter.setList(data)
                showContentContainer()
            }
        })
    }

    override fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?) {
        iWantViewModel.getInquiry(aMapLocation?.longitude, aMapLocation?.latitude).observeOnce(viewLifecycleOwner, { data: List<BaseIWantEntity> ->
            refreshLayout.finishRefresh()
            if (data.isNullOrEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_INQUIRY)
            } else {
                iWantRecyclerAdapter.setList(data)
                showContentContainer()
            }
        })
    }

    /**
     * 当获取到的数据小于默认条数会添加尾巴以提示用户没有更多
     */
    override fun onNoMoreData() {
        binding?.homeRecycler?.setIsNoMoreData(true)
    }

    /**
     * 当页面被刷新时重置没有更多数据的标识符
     */
    override fun onNoMoreDataRefresh() {
        binding?.homeRecycler?.setIsNoMoreData(false)
    }

    /**
     * 以元素共享动画打开求购详情页面
     */
    override fun onHomePostItemClicked(card: View, item: BaseIWantEntity) {
        if (context != null) {
            IWantActivity.start(requireContext(), card,item )
        }
    }

    override fun getAutoLoadMoreData(recycler: EndlessRecyclerView, aMapLocation: AMapLocation?) {}
}