package com.example.schoolairdroprefactoredition.scene.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amap.api.location.AMapLocation
import com.blankj.utilcode.constant.PermissionConstants
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeContentBinding
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity.OnLocationListener
import com.example.schoolairdroprefactoredition.ui.components.EndlessRecyclerView
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

abstract class BaseChildFragment : Fragment(), OnLocationListener, EndlessRecyclerView.OnLoadMoreListener, OnRefreshListener {

    companion object {
        /**
         * 只在第一个子页面加载时才有主动请求定位的权限
         * 但是这里会有一个问题，切换app主题时由于requested已置为true
         * 因此页面被recreated的时候就不会再次自动获取数据
         * 解决：需要在[MainActivity]的OnDestroy中将requested重置
         */
        private var requested = false

        private var isMainActivityLocated = false // 其他页面是否受到过定位信息

        private var aMapLocation: AMapLocation? = null

        /**
         * 通知主题改变
         * 重置所有静态变量
         */
        fun notifyThemeChanged() {
            requested = false
            isMainActivityLocated = false
            aMapLocation = null
        }
    }

    /**
     * 是否已经获取来自[MainActivity]的定位信息
     *
     *
     * 1、先入为主的页面将有权力主动要求MainActivity获取定位信息
     * 2、后来者若发现页面中已经存在定位信息，则可以直接使用它获取数据
     * 3、后来者若发现页面中不存在定位信息，则将等待主页面返回定位信息
     * 4、在主页的定位信息返回之后，若发现该页面已经被通知过了，则忽略
     * 该页面的数据获取，否则获取页面数据
     */
    private var isThisPageLocationGot = false // 本页面是否收到过定位信息

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is MainActivity) {
            (activity as MainActivity).addOnLocationListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeContentBinding.inflate(inflater, container, false)

        binding.homeRecycler.setOnLoadMoreListener(this)
        binding.homeRefresh.setOnRefreshListener(this)

        initView(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPlaceHolder(StatePlaceHolder.TYPE_LOADING)

        if (!requested) {
            requested = true
            locate(PermissionBaseActivity.RequestType.AUTO) // 自动请求MainActivity的定位
        } else if (isMainActivityLocated) {
            getPageDataIfLocated()
        }
    }

    abstract fun initView(binding: FragmentHomeContentBinding?)

    /**
     * 定位相关的页面数据获取
     */
    private fun getPageDataIfLocated() {
        if (aMapLocation != null) {
            if (aMapLocation?.errorCode == 0) {
                getOnlineData(aMapLocation)
            } else {
                showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME)
            }
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME)
        }
    }

    /**
     * 获取页面数据
     */
    abstract fun getOnlineData(aMapLocation: AMapLocation?)

    /**
     * 下拉刷新获取数据
     */
    abstract fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?)

    /**
     * 下拉自动获取更多数据
     */
    abstract fun getAutoLoadMoreData(aMapLocation: AMapLocation?)

    /**
     * 显示PlaceHolder
     *
     * @param type [com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder.TYPE_NETWORK_OR_LOCATION_ERROR_HOME]
     * [com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder.TYPE_EMPTY_HOME_GOODS]
     */
    protected fun showPlaceHolder(type: Int) {
        if (parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).showPlaceholder(type)
        }
    }

    /**
     * 显示物品信息
     */
    protected fun showContentContainer() {
        if (parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).showContentContainer()
        }
    }

    /**
     * 请求[MainActivity.requestPermission] (int)} ()}的定位
     */
    private fun locate(@PermissionBaseActivity.RequestType type: Int) {
        if (activity is MainActivity) {
            (activity as MainActivity).requestPermission(PermissionConstants.LOCATION, type)
        }
    }

    /**
     * 请求定位但是不请求权限
     */
    private fun locateWithoutRequest() {
        if (activity is MainActivity) {
            (activity as MainActivity).checkPermissionWithoutRequest(PermissionBaseActivity.Automatically.LOCATION)
        }
    }

    override fun onLocated(aMapLocation: AMapLocation?) {
        Companion.aMapLocation = aMapLocation
        isMainActivityLocated = true
        if (!isThisPageLocationGot) {
            isThisPageLocationGot = true
            getPageDataIfLocated()
        }
    }

    override fun onLocationPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED)
    }

    override fun autoLoadMore(recycler: EndlessRecyclerView) {
        if (aMapLocation != null) {
            getAutoLoadMoreData(aMapLocation)
        } else {
            showPlaceHolder(StatePlaceHolder.TYPE_ERROR)
            recycler.finishLoading()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (aMapLocation != null) {
            getRefreshData(refreshLayout, aMapLocation)
        } else { // 定位失败时通知父Fragment显示PlaceHolder
            refreshLayout.finishRefresh()
            showPlaceHolder(StatePlaceHolder.TYPE_LOADING)
            locateWithoutRequest() // 自动请求MainActivity的定位
        }
    }

}