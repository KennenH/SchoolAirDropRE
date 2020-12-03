package com.example.schoolairdroprefactoredition.scene.tab.trash

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.scene.tab.TabBaseActivity
import com.example.schoolairdroprefactoredition.ui.adapter.TrashBinPagerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class TrashBinActivity : TabBaseActivity() {

    companion object {
        fun start(context: Context, token: DomainToken) {
            val intent = Intent(context, TrashBinActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_TOKEN, token)
            context.startActivity(intent)
        }
    }

    override fun getPagerAdapter(): FragmentPagerAdapter {
        return TrashBinPagerAdapter(supportFragmentManager, this@TrashBinActivity)
    }

    override fun getPageNameRes(): Int {
        return R.string.transhBin
    }
}