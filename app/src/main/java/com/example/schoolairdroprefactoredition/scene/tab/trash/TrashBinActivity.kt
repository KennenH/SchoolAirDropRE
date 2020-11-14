package com.example.schoolairdroprefactoredition.scene.tab.trash

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.tab.TabBaseActivity

class TrashBinActivity : TabBaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TrashBinActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getPagerAdapter(): FragmentPagerAdapter {
        TODO("Not yet implemented")
    }

    override fun getPageNameRes(): Int {
        return R.string.transhBin
    }
}