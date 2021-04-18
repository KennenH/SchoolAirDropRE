package com.example.schoolairdroprefactoredition.scene.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.scene.main.base.BaseParentFragment
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 首页中求购tab下的所有子页面的父类，当前仅有求购一个子页面
 * [com.example.schoolairdroprefactoredition.scene.main.home.IDesireFragment]
 */
class IDesireParentFragment : BaseParentFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): IDesireParentFragment {
            return IDesireParentFragment()
        }
    }

    private val homePagerAdapter by lazy {
        HomePagerAdapter(childFragmentManager, HomePagerAdapter.PAGE_TYPE_INQUIRY)
    }

    private lateinit var mViewPager: ViewPager

    private lateinit var mAppbarLayout: AppBarLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as? MainActivity)?.autoLogin()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        ////////////////////// 取消注释以显示功能未支持 ///////////////////////
//        val binding = FragmentHomeFeatureNotSupportBinding.inflate(inflater, container, false)
//        binding.featureNotSupportMask.setOnClickListener(this)
        ////////////////////// 取消注释以显示功能未支持 ///////////////////////

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        mViewPager = binding.homeViewpager
        mAppbarLayout = binding.toolbarWrapper
        setUpPlaceHolderHAndContainerView(binding.placeholder, binding.homeViewpager)
        val commonNavigator = CommonNavigator(context)
        val adapter = HomeNavigatorAdapter(context, binding.homeViewpager, HomeNavigatorAdapter.HOME)
        commonNavigator.adapter = adapter
        binding.apply {
            ViewPagerHelper.bind(
                    homeIndicator.also {
                        it.navigator = commonNavigator
                    },
                    homeViewpager.also {
                        it.adapter = homePagerAdapter
                    })
            homeSearchBar.setOnClickListener(this@IDesireParentFragment)
            homeTopAdd.setOnClickListener(this@IDesireParentFragment)
            homeTopAdd.text = getString(R.string.postIDesire)
            homeTopAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_ask_for_buy, 0, 0, 0)
        }
        binding.homeToolbar.setOnClickListener(this)
        return binding.root
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     *
     * 详见 [IDesireFragment.scrollToTop]
     */
    fun pageScrollToTop() {
        homePagerAdapter.scrollToTop(mViewPager.currentItem)
        mAppbarLayout.setExpanded(true, true)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.home_search_bar) {
            mOnSearchBarClickedListener?.onSearchBarClicked()
        } else if (id == R.id.home_top_add) {
            if (activity is MainActivity) {
                AddNewActivity.startAddNew(context, AddNewActivity.AddNewType.ADD_IDESIRE)
            }
        }
    }
}