package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SellingFragment.Companion.newInstance

class SSBPagerAdapter(
        fm: FragmentManager, behavior: Int,
        private val isMine: Boolean
) : FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return newInstance(isMine)
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}