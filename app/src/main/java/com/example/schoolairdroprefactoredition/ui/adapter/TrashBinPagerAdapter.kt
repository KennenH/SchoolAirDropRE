package com.example.schoolairdroprefactoredition.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.tab.trash.TrashBinFragment

class TrashBinPagerAdapter(fm: FragmentManager,  private val context: Context) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        val TABS = intArrayOf(R.string.corrupted, R.string.complished)
    }

    override fun getCount(): Int {
        return TABS.size
    }

    override fun getItem(position: Int): Fragment {
        return TrashBinFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(TABS[position])
    }
}