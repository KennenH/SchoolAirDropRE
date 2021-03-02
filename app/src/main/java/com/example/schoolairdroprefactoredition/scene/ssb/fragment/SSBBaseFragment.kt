package com.example.schoolairdroprefactoredition.scene.ssb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel.OnRequestListener
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter.OnSSBItemActionListener
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder.OnPlaceHolderRefreshListener
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.SellingViewModel

abstract class SSBBaseFragment :
        StatePlaceholderFragment(),
        OnRequestListener,
        OnPlaceHolderRefreshListener,
        OnSSBItemActionListener {

    companion object {
        /**
         * 在售子页面的位置
         */
        const val POS_SELLING = 0
    }

    protected val viewModel by lazy {
        ViewModelProvider(this).get(SellingViewModel::class.java)
    }

    protected val mAdapter by lazy {
        SSBAdapter(activity?.intent?.getBooleanExtra(ConstantUtil.KEY_IS_MINE, false))
    }

    protected var binding: FragmentSsbBinding? = null

    protected var mList: ArrayList<DomainPurchasing.DataBean> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSsbBinding.inflate(inflater, container, false)
        binding?.apply {
            ssbRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            placeHolder.addOnPlaceHolderActionListener(this@SSBBaseFragment)
            ssbRecycler.addItemDecoration(MarginItemDecoration())
            ssbRecycler.adapter = mAdapter
            ssbRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (activity is SSBActivity) {
                        (activity as? SSBActivity)?.hideSearchBar()
                    }
                }
            })
        }
        mAdapter.addOnSSBItemActionListener(this)

        init(binding)
        return binding?.getRoot()
    }

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return binding?.placeHolder
    }

    override fun getContentContainer(): View? {
        return binding?.ssbRecycler
    }

    /**
     * 子fragment在获取网络数据之后调用
     */
    protected fun loadData(data: DomainPurchasing) {
        mList.addAll(data.data)
        if (mList.size == 0) showPlaceholder(StatePlaceHolder.TYPE_EMPTY) else {
            mAdapter.setList(mList)
            showContentContainer()
        }
    }

    /**
     * 初始化页面
     */
    abstract fun init(binding: FragmentSsbBinding?)

    /**
     * 按下刷新重试网络数据获取
     */
    abstract fun retryGrabOnlineData()


    override fun onPlaceHolderRetry(view: View) {
        retryGrabOnlineData()
    }

    override fun onError() {
        dismissLoading { showPlaceholder(StatePlaceHolder.TYPE_ERROR) }
    }

    /**
     * item上右下角的三个点按钮的操作
     * 在售列表中为 {下架物品}
     */
    abstract fun onItemAction(view: View, bean: DomainPurchasing.DataBean?)

    override fun onItemActionButtonClick(view: View, bean: DomainPurchasing.DataBean?) {
        onItemAction(view, bean)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.addOnSSBItemActionListener(this)
    }
}