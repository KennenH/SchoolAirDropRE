package com.example.schoolairdroprefactoredition.scene.main.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        /**
         * 主页面是否收到过定位信息
         * 与具体某个页面没有关系，只要任何一个子页面收到回调便认为已定位
         */
        private var isMainActivityLocated = false

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
     * 当前子页面是否已经获取来自[MainActivity]的定位信息
     * 与上面的[BaseChildFragment.isMainActivityLocated]不同的是这个变量记录每个子页面的已定位标志
     *
     * 1、先入为主的页面将有权力主动要求MainActivity获取定位信息
     * 2、后来者若发现页面中已经存在定位信息，则可以直接使用它获取数据
     * 3、后来者若发现页面中不存在定位信息，则将等待主页面返回定位信息
     * 4、在主页的定位信息返回之后，若发现该页面已经被通知过了，则忽略该页面的数据获取，否则获取页面数据
     *
     * fatal bug fix: 但是这里的 已定位 逻辑会有问题，若是因为网络等其他原因造成的页面无法加载，用户在
     * 点击重试之后会发现页面无限等待，因为这里的定位查重逻辑将用户手动重试的请求也一并过滤了
     */
    private var isThisPageLocationGot = false

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
     * 下拉刷新
     */
    abstract fun getRefreshData(refreshLayout: RefreshLayout, aMapLocation: AMapLocation?)

    /**
     * 上拉获取更多数据
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
     * 即若定位权限被拒绝也将保持沉默
     */
    private fun locateWithoutRequest() {
        if (activity is MainActivity) {
            (activity as MainActivity).checkPermissionWithoutRequest(PermissionBaseActivity.Automatically.LOCATION)
        }
    }

    /**
     * [MainActivity.OnLocationListener]监听回调
     */
    override fun onLocated(aMapLocation: AMapLocation?) {
        Companion.aMapLocation = aMapLocation
        isMainActivityLocated = true
//        if (!isThisPageLocationGot) {
//            isThisPageLocationGot = true
        getPageDataIfLocated()
//        }
    }

    override fun onLocationPermissionDenied() {
        showPlaceHolder(StatePlaceHolder.TYPE_DENIED)
    }

    override fun autoLoadMore(recycler: EndlessRecyclerView) {
        if (aMapLocation != null) {
            getAutoLoadMoreData(aMapLocation)
        } else {
            Toast.makeText(context, "something went wrong >_<", Toast.LENGTH_SHORT).show()
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