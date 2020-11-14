package com.example.schoolairdroprefactoredition.scene.tab

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.viewpager.widget.PagerAdapter
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import kotlinx.android.synthetic.main.activity_tab.*

abstract class TabBaseActivity : ImmersionStatusBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)
        setSupportActionBar(toolbar)
        name.text = getString(getPageNameRes())
        tab.isTabIndicatorFullWidth = false
        pager.adapter = getPagerAdapter()
        tab.setTabTextColors(resources.getColor(R.color.black, theme), resources.getColor(R.color.black, theme))
        tab.setupWithViewPager(pager)
    }

    abstract fun getPagerAdapter(): PagerAdapter

    @StringRes
    abstract fun getPageNameRes(): Int

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}