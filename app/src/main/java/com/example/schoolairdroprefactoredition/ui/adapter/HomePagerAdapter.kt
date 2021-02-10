package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.scene.main.home.HomePlaygroundFragment
import com.example.schoolairdroprefactoredition.scene.main.home.HomePurchasingFragment
import java.util.*

class HomePagerAdapter(fm: FragmentManager, private val pageType: Int) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val PAGE_TYPE_PLAYGROUND = 9837 // 页面类型码 广场
        const val PAGE_TYPE_PURCHASING = 3557 // 页面类型码 淘物
        const val PAGE_NUM_PLAYGROUND = 1 // 子页面数量 广场
        const val PAGE_NUM_PURCHASING = 1 // 子页面数量 淘物
    }

    /**
     * 广场 子页面数组
     */
    private val mPlaygroundFragments: MutableList<HomePlaygroundFragment> = ArrayList(PAGE_NUM_PLAYGROUND)

    /**
     * 淘物 子页面数组
     */
    private val mPurchasingFragments: MutableList<HomePurchasingFragment> = ArrayList(PAGE_NUM_PURCHASING)

    override fun getItem(position: Int): Fragment {
        return if (pageType == PAGE_TYPE_PLAYGROUND) {
            val fragment = HomePlaygroundFragment.newInstance()
            mPlaygroundFragments.add(fragment)
            fragment
        } else {
            val fragment = HomePurchasingFragment.newInstance()
            mPurchasingFragments.add(fragment)
            fragment
        }
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     *
     * @param subFragmentIndex 要滑动的子页面在 广场 或者 淘物 的位置
     */
    fun scrollToTop(subFragmentIndex: Int) {
        if (pageType == PAGE_TYPE_PLAYGROUND) {
            mPlaygroundFragments[subFragmentIndex].scrollToTop()
        } else {
            mPurchasingFragments[subFragmentIndex].scrollToTop()
        }
    }

    override fun getCount(): Int {
        return when (pageType) {
            PAGE_TYPE_PLAYGROUND -> {
                PAGE_NUM_PLAYGROUND
            }
            PAGE_TYPE_PURCHASING -> {
                PAGE_NUM_PURCHASING
            }
            else -> {
                1
            }
        }
    }
}