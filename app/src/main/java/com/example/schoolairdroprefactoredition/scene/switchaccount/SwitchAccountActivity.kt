package com.example.schoolairdroprefactoredition.scene.switchaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.main.AccountViewModel
import com.example.schoolairdroprefactoredition.ui.adapter.SwitchAccountRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.AnimUtil
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import kotlinx.android.synthetic.main.activity_switch_account.*

class SwitchAccountActivity : ImmersionStatusBarActivity() {

    companion object {
        const val SWITCH_ACCOUNT = 9346

        fun start(context: Context, bundle: Bundle) {
            val intent = Intent(context, SwitchAccountActivity::class.java)
            intent.putExtras(bundle)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, SWITCH_ACCOUNT)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    private val accountViewModel by lazy {
        ViewModelProvider(this).get(AccountViewModel::class.java)
    }

    private val accountRecyclerAdapter by lazy {
        SwitchAccountRecyclerAdapter(intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)
        setSupportActionBar(toolbar)

        account_recycler.apply {
            adapter = accountRecyclerAdapter
            addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(5f), false))
            layoutManager = LinearLayoutManager(this@SwitchAccountActivity, RecyclerView.VERTICAL, false)
        }

        accountViewModel.allUserCacheOnThisDevice.observe(this, {
            accountRecyclerAdapter.setList(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            AnimUtil.activityExitAnimDown(this@SwitchAccountActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}