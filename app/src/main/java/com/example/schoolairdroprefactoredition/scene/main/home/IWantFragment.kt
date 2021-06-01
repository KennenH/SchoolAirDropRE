package com.example.schoolairdroprefactoredition.scene.main.home

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.OnIWantItemClickListener
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.viewmodel.IWantViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.example.schoolairdroprefactoredition.scene.iwant.IWantActivity

/**
 * 首页的求购页面
 */
class IWantFragment : BaseChildFragment(), IWantRecyclerAdapter.OnNoMoreDataListener, OnIWantItemClickListener {

    companion object {
        private var INSTANCE: IWantFragment? = null
        fun getInstance(): IWantFragment {
            return INSTANCE ?: IWantFragment().also {
                INSTANCE = it
            }
        }
    }

    private val iWantViewModel by lazy {
        IWantViewModel.IWantViewModelFactory((activity?.application as SAApplication).databaseRepository).create(IWantViewModel::class.java)
    }

    private val iWantRecyclerAdapter by lazy {
        IWantRecyclerAdapter().also {
            it.addOnNoMoreDataListener(this)
            it.setOnIWantItemClickListener(this)
        }
    }

    private var binding: FragmentHomeContentBinding? = null

    override fun initView(binding: FragmentHomeContentBinding?) {
        this.binding = binding
        binding?.homeRecycler?.apply {
            setOnLoadMoreListener(this@IWantFragment)
            setPadding(SizeUtils.dp2px(3f), SizeUtils.dp2px(3f), SizeUtils.dp2px(3f), SizeUtils.dp2px(3f))
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
                it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
            adapter = iWantRecyclerAdapter
        }
    }

    /**
     * 从缓存中获取求购
     */
    override fun getLocalCache() {
        iWantViewModel.getIWantCache().observeOnce(viewLifecycleOwner) {
            it?.data?.size?.let { size ->
                if (size > 0) {
                    iWantRecyclerAdapter.setList(it.data)
                    showContentContainer()
                }
            }
        }
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动（模仿美团）
     */
    fun scrollToTop() {
        val manager = binding?.homeRecycler?.layoutManager as StaggeredGridLayoutManager
        val visible = IntArray(manager.spanCount + 1)
        manager.findLastVisibleItemPositions(visible)
        if (visible[0] > 10) {
            binding?.homeRecycler?.scrollToPosition(5)
        }
        binding?.homeRecycler?.smoothScrollToPosition(0)
    }

    /**
     * 进入app首次获取求购数据
     */
    override fun getOnlineData(aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            if (iWantRecyclerAdapter.data.isEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME)
            }
            return
        }

        iWantViewModel.getNearByIWant(aMapLocation.longitude, aMapLocation.latitude).observeOnce(viewLifecycleOwner, {
            if (it != null) {
                // 如果获取的物品数量为0且当前的adapter也尚没有数据则显示placeholder
                if (it.data.isEmpty() && iWantRecyclerAdapter.data.isEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_IWANT)
                } else {
                    iWantRecyclerAdapter.setList(it.data)
                    showContentContainer()
                }
            } else {
                // 如果当前adapter没有数据且加载失败了则显示placeholder
                if (iWantRecyclerAdapter.data.isEmpty()) {
                    showPlaceHolder(StatePlaceHolder.TYPE_ERROR)
                }
            }
        })
    }

    /**
     * 刷新时获取数据的逻辑
     */
    override fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            Toast.makeText(context, R.string.errorLocation, Toast.LENGTH_SHORT).show()
            return
        }
        iWantViewModel.getNearByIWant(aMapLocation.longitude, aMapLocation.latitude).observeOnce(viewLifecycleOwner, {
            refreshLayout.finishRefresh()
            showContentContainer()
            if (it != null) {
                iWantRecyclerAdapter.setList(it.data)
            } else {
                Toast.makeText(context, R.string.systemBusy, Toast.LENGTH_SHORT).show()
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
    override fun onHomePostItemClicked(card: View, item: DomainIWant.Data) {
        if (context != null) {
            IWantActivity.start(requireContext(), card, item)
        }
    }

    override fun getAutoLoadMoreData(recycler: EndlessRecyclerView, aMapLocation: AMapLocation?) {
        iWantViewModel.getNearByIWant().observeOnce(viewLifecycleOwner) {
            recycler.finishLoading()
            showContentContainer()
            if (it != null) {
                iWantRecyclerAdapter.addData(it.data)
            } else {
                Toast.makeText(context, R.string.systemBusy, Toast.LENGTH_SHORT).show()
            }
        }
    }
}