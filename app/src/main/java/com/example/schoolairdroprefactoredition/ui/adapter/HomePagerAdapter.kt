package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.schoolairdroprefactoredition.scene.main.home.IWantFragment
import com.example.schoolairdroprefactoredition.scene.main.home.PurchasingFragment
import java.util.*

class HomePagerAdapter(fm: FragmentManager, private val pageType: Int) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val PAGE_TYPE_IWANT = 9837 // 页面类型码 求购
        const val PAGE_TYPE_PURCHASING = 3557 // 页面类型码 淘物
        const val PAGE_NUM_IWANT = 1 // 子页面数量 求购
        const val PAGE_NUM_PURCHASING = 1 // 子页面数量 淘物
    }

    /**
     * 求购 子页面数组
     */
    private val mIWantFragments: MutableList<IWantFragment> = ArrayList(PAGE_NUM_IWANT)

    /**
     * 淘物 子页面数组
     */
    private val mPurchasingFragments: MutableList<PurchasingFragment> = ArrayList(PAGE_NUM_PURCHASING)

    override fun getItem(position: Int): Fragment {
        return if (pageType == PAGE_TYPE_IWANT) {
            IWantFragment.getInstance().also {
                mIWantFragments.add(it)
            }
        } else {
            PurchasingFragment.getInstance().also {
                mPurchasingFragments.add(it)
            }
        }
    }

    /**
     * 使列表滑动至顶部
     * 当当前页面最后可见的item位置小于一定值时直接调用平滑滑动
     * 否则将先闪现至固定item位置处再平滑滚动
     *
     * @param subFragmentIndex 要滑动的子页面在 广场 或者 淘物 的位置
     */
    fun scrollToTop(subFragmentIndex: Int?) {
        if (subFragmentIndex == null) return

        if (pageType == PAGE_TYPE_IWANT) {
            mIWantFragments[subFragmentIndex].scrollToTop()
        } else {
            mPurchasingFragments[subFragmentIndex].scrollToTop()
        }
    }

    override fun getCount(): Int {
        return when (pageType) {
            PAGE_TYPE_IWANT -> {
                PAGE_NUM_IWANT
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