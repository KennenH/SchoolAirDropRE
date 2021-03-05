package com.example.schoolairdroprefactoredition.scene.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.example.schoolairdroprefactoredition.utils.RegExrUtil
import com.example.schoolairdroprefactoredition.viewmodel.UserRenameViewModel
import kotlinx.android.synthetic.main.activity_selling_add_set.*
import java.util.regex.Pattern

class UserUpdateNameActivity : ImmersionStatusBarActivity() {

    companion object {

        /**
         * 名字最大长度
         */
        const val MAX_NAME_LENGTH = 8


        fun start(context: Context?) {
            val intent = Intent(context, UserUpdateNameActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, UserActivity.REQUEST_UPDATE_MY)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(UserRenameViewModel::class.java)
    }

    /**
     * 完成按钮
     *
     * 当[input_warning]可见时按钮将不可用
     */
    private var doneMenu: MenuItem? = null

    private val loading by lazy {
        MyUtil.loading(this)
    }

    /**
     * 仅匹配中文、字母、数字和下划线
     */
    private val noSpecialsPattern by lazy {
        Pattern.compile(RegExrUtil.NO_SPECIALS)
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
        doneMenu = item
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        } else if (id == R.id.done) {
            if (input_warning.visibility != View.VISIBLE) {
                rename()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        input_attention.visibility = View.GONE
        val myInfo = (application as SAApplication).getCachedMyInfo()
        set_title.setText(R.string.setName)

        input.apply {
            filters = arrayOf(InputFilter.LengthFilter(8))
            input_warning.setText(R.string.illegalName)
            setLines(1)
            addTextChangedListener({ _, _, _, _ -> }, { _, _, _, _ -> }, {
                LogUtils.d("after text changed")
                input_tip.text = getString(R.string.textRemainCount, MAX_NAME_LENGTH - input.text.length)
                if (!noSpecialsPattern.matcher(input.text).matches()) {
                    LogUtils.d("字符输入非法")
                    // 含有特殊字符，禁用提交按钮
                    doneMenu?.isEnabled = false
                    input_warning.visibility = View.VISIBLE
                } else {
                    LogUtils.d("字符输入合法")
                    doneMenu?.isEnabled = true
                    input_warning.visibility = View.INVISIBLE
                }
            })
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    KeyboardUtils.showSoftInput(v)
                } else {
                    KeyboardUtils.hideSoftInput(v)
                }
            }

            setOnClickListener {
                if (!hasFocus()) {
                    requestFocus()
                }
            }

            setText(myInfo?.userName)
            setSelection(text.length)

            post {
                requestFocus()
            }
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
     * 修改用户名
     */
    private fun rename() {
        val myInfo = (application as SAApplication).getCachedMyInfo()
        KeyboardUtils.hideSoftInput(this)
        if (input.text.toString().trim() == myInfo?.userName) {
            finish()
        } else {
            loading.show()
            val token = (application as SAApplication).getCachedToken()
            if (token != null && token.access_token != null) {
                viewModel.rename(token.access_token, input.text.toString().trim())
                        .observeOnce(this, {
                            loading.dismissWith {
                                if (it) {
                                    sendData(myInfo)
                                } else {
                                    DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
                                }
                            }
                        })
            }
        }
    }
}