package com.example.schoolairdroprefactoredition.scene.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import kotlinx.android.synthetic.main.activity_selling_add_set.*

class UserUpdateNameActivity : ImmersionStatusBarActivity() {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, UserUpdateNameActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, UserActivity.REQUEST_UPDATE)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(ResultViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selling_add_set)
        setSupportActionBar(findViewById(R.id.toolbar))
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_done, menu)
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        } else if (id == R.id.done) {
            rename()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        val myInfo = (application as Application).getCachedMyInfo()
        set_title.setText(R.string.setName)
        input.inputType = EditorInfo.TYPE_NULL
        input.setLines(1)
        input.setText(myInfo?.userName)
        input.setSelection(input.text.length)

        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                KeyboardUtils.showSoftInput()
            }
        }

        input.setOnClickListener {
            if (!input.hasFocus()) {
                input.requestFocus()
            }
        }

        input.post {
            input.requestFocus()
        }
    }

    /**
     * 将数据回到上一个Activity
     */
    private fun sendData(myInfo: DomainUserInfo.DataBean?) {
        myInfo?.userName = input.text.toString().trim()
        intent.putExtra(ConstantUtil.KEY_USER_INFO, myInfo)
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * 网络请求修改名字
     */
    private fun rename() {
        val myInfo = (application as Application).getCachedMyInfo()
        KeyboardUtils.hideSoftInput(this)
        if (input.text.toString().trim() == myInfo?.userName) {
            finish()
        } else {
            val token = (application as Application).getCachedToken()
            if (token != null && token.access_token != null) {
                viewModel.rename(token.access_token, input.text.toString().trim())
                        .observe(this, {
                            if (it) {
                                sendData(myInfo)
                            } else {
                                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
                            }
                        })
            }
        }
    }
}