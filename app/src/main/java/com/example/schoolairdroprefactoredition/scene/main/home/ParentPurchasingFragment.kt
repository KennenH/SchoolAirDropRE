package com.example.schoolairdroprefactoredition.scene.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.scene.main.base.BaseParentFragment
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class ParentPurchasingFragment : BaseParentFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): ParentPurchasingFragment {
            return ParentPurchasingFragment()
        }
    }

    private var mViewPager: ViewPager? = null

    private var mHomePagerAdapter: HomePagerAdapter? = null

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

        mHomePagerAdapter = HomePagerAdapter(childFragmentManager, HomePagerAdapter.PAGE_TYPE_PURCHASING)
        setUpPlaceHolderHAndContainerView(binding.placeholder, binding.homeViewpager)

        val commonNavigator = CommonNavigator(context)
        val adapter = HomeNavigatorAdapter(context, binding.homeViewpager, HomeNavigatorAdapter.PURCHASING)

        commonNavigator.adapter = adapter
        binding.homeViewpager.adapter = mHomePagerAdapter
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
     * 详见 [HomePurchasingFragment.scrollToTop]
     */
    fun pageScrollToTop() {
        if (mViewPager != null && mAppBarLayout != null) {
            mHomePagerAdapter?.scrollToTop(mViewPager!!.currentItem)
            mAppBarLayout!!.setExpanded(true, true)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.home_search_bar) {
            mOnSearchBarClickedListener?.onSearchBarClicked()
        } else if (id == R.id.home_top_add) {
            onHomePostMyItem(v)
        }
    }

}