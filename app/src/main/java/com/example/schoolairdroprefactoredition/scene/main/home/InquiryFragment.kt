package com.example.schoolairdroprefactoredition.scene.main.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragment
import com.example.schoolairdroprefactoredition.scene.post.PostActivity
import com.example.schoolairdroprefactoredition.ui.adapter.InquiryRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.InquiryRecyclerAdapter.OnInquiryItemClickListener
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.InquiryViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout

class InquiryFragment : BaseChildFragment(), InquiryRecyclerAdapter.OnNoMoreDataListener, OnInquiryItemClickListener {

    companion object {
        fun newInstance(): InquiryFragment {
            return InquiryFragment()
        }
    }

    private val inquiryViewModel by lazy {
        ViewModelProvider(this).get(InquiryViewModel::class.java)
    }

    private val inquiryRecyclerAdapter by lazy {
        InquiryRecyclerAdapter().also {
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
            setOnLoadMoreListener(this@InquiryFragment)
            setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f))
            layoutManager = mManager
            addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(1f), true))
            adapter = inquiryRecyclerAdapter
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
        inquiryViewModel.getInquiry().observeOnce(viewLifecycleOwner, { data: List<BaseHomeNewsEntity> ->
            if (data.isNullOrEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_INQUIRY)
            } else {
                inquiryRecyclerAdapter.setList(data)
                showContentContainer()
            }
        })
    }

    override fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?) {
        inquiryViewModel.getInquiry(aMapLocation?.longitude, aMapLocation?.latitude).observeOnce(viewLifecycleOwner, { data: List<BaseHomeNewsEntity> ->
            refreshLayout.finishRefresh()
            if (data.isNullOrEmpty()) {
                showPlaceHolder(StatePlaceHolder.TYPE_EMPTY_INQUIRY)
            } else {
                inquiryRecyclerAdapter.setList(data)
                showContentContainer()
            }
        })
    }

    override fun onNoMoreData() {
        binding?.homeRecycler?.setIsNoMoreData(true)
    }

    override fun onNoMoreDataRefresh() {
        binding?.homeRecycler?.setIsNoMoreData(false)
    }

    override fun onHomePostItemClicked(pager: CardView, title: TextView) {
        val intent = Intent(context, PostActivity::class.java)
        val pair1 = Pair.create<View, String>(pager, getString(R.string.sharedElementPostActivityWrapper))
        val pair2 = Pair.create<View, String>(title, getString(R.string.sharedElementPostActivityTitle))
        val options = activity?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(it, pair1, pair2)
        }
        startActivity(intent, options?.toBundle())
    }

    override fun getAutoLoadMoreData(recycler: EndlessRecyclerView, aMapLocation: AMapLocation?) {}
}