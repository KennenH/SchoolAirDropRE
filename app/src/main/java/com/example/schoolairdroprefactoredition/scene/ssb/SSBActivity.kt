package com.example.schoolairdroprefactoredition.scene.ssb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter.OnSSBDataLenChangedIntermediaryListener
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.android.synthetic.main.activity_ssb.*

class SSBActivity : ImmersionStatusBarActivity(), View.OnClickListener, OnSSBDataLenChangedIntermediaryListener {

    companion object {
        const val SELLING = R.string.selling
        const val POSTS = R.string.posts
        const val PAGE_INDEX = "SSBPage?Index"

        /**
         * 三种页面进入方式
         * 1、未登录 没有token
         * 2、登录查看自己的页面 有token
         * 3、登录查看他人的页面 有token
         */
        fun start(context: Context?, userID: Int?, page: Int, isMine: Boolean) {
            if (userID == null) return

            val intent = Intent(context, SSBActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_USER_ID, userID)
            intent.putExtra(ConstantUtil.KEY_IS_MINE, isMine)
            intent.putExtra(PAGE_INDEX, page)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
            } else {
                context?.startActivity(intent)
            }
        }
    }

    private var mOnLoginStateChangeListener: OnLoginStateChangeListener? = null

    private val mPagerAdapter by lazy {
        SSBPagerAdapter(this,
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                intent.getBooleanExtra(ConstantUtil.KEY_IS_MINE, false))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ssb)
        setSupportActionBar(findViewById(R.id.toolbar))

        ssb_search.setOnClickListener(this)
        ssb_title.setOnClickListener(this)

        ssb_search.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                KeyboardUtils.showSoftInput(this@SSBActivity)
            } else {
                KeyboardUtils.hideSoftInput(this@SSBActivity)
            }
        }
        mPagerAdapter.setOnSSBDataLenChangedIntermediaryListener(this)

        ssb_pager.offscreenPageLimit = 2
        ssb_pager.adapter = mPagerAdapter
        ssb_pager.currentItem = intent.getIntExtra(PAGE_INDEX, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                if (data != null) {
                    intent.putExtras(data)
                    mOnLoginStateChangeListener?.onLoginSSB()
                    setResult(RESULT_OK, data)
                }
            }
        }
    }

    /**
     * 获取当前页面index
     */
    fun getCurrentItem(): Int {
        return ssb_pager.currentItem
    }

    override fun onSSBDataLenChangedIntermediary(total: Int) {
        ssb_filter.setDataStatistics(total)
    }

    /**
     * 在[AddNewActivity]中登录后
     * 将登录结果回调至ssb的子fragment中
     */
    interface OnLoginStateChangeListener {
        fun onLoginSSB()
    }

    fun setOnLoginStateChangeListener(listener: OnLoginStateChangeListener?) {
        mOnLoginStateChangeListener = listener
    }

    fun hideSearchBar() {
        ssb_title.requestFocus()
        ssb_search.clearFocus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.ssb_search) {

        }
    }
}