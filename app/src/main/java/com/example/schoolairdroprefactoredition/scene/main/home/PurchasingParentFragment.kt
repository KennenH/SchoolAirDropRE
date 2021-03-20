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
 * 首页中淘物tab下的所有子页面的父类，当前仅有淘物一个子页面
 * [com.example.schoolairdroprefactoredition.scene.main.home.PurchasingFragment]
 */
class PurchasingParentFragment : BaseParentFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): PurchasingParentFragment {
            return PurchasingParentFragment()
        }
    }

    private val purchasingPagerAdapter by lazy {
        HomePagerAdapter(childFragmentManager, HomePagerAdapter.PAGE_TYPE_PURCHASING)
    }

    private var mViewPager: ViewPager? = null

    private var mAppBarLayout: AppBarLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).autoLogin()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        mViewPager = binding.homeViewpager
        mAppBarLayout = binding.toolbarWrapper

        setUpPlaceHolderHAndContainerView(binding.placeholder, binding.homeViewpager)

        val commonNavigator = CommonNavigator(context)
        val adapter = HomeNavigatorAdapter(context, binding.homeViewpager, HomeNavigatorAdapter.PURCHASING)

        commonNavigator.adapter = adapter
        binding.homeViewpager.adapter = purchasingPagerAdapter
        binding.homeIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.homeIndicator, binding.homeViewpager)

        binding.homeSearchBar.setOnClickListener(this)
        binding.homeTopAdd.setOnClickListener(this)
        binding.homeTopAdd.setText(R.string.addNewSelling)
        binding.homeTopAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_logo_text_alpha_20, 0, 0, 0)
        return binding.root
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     *
     * 详见 [PurchasingFragment.scrollToTop]
     */
    fun pageScrollToTop() {
        if (mViewPager != null && mAppBarLayout != null) {
            purchasingPagerAdapter.scrollToTop(mViewPager?.currentItem ?: 0)
            mAppBarLayout?.setExpanded(true, true)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.home_search_bar) {
            mOnSearchBarClickedListener?.onSearchBarClicked()
        } else if (id == R.id.home_top_add) {
            // if 当前子页面为我的在售
            if (activity is MainActivity) {
                AddNewActivity.startAddNew(context, AddNewActivity.AddNewType.ADD_ITEM)
            }
            // else if 当前子页面为我的求购
//            if (activity is MainActivity) {
//                AddNewActivity.start(context, AddNewActivity.AddNewType.ADD_INQUIRY)
//            }
            // else
        }
    }

}