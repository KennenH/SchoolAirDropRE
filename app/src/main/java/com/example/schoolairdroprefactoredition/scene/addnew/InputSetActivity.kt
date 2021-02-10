package com.example.schoolairdroprefactoredition.scene.addnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import kotlinx.android.synthetic.main.activity_selling_add_set.*
import kotlin.math.abs

class InputSetActivity : ImmersionStatusBarActivity() {
    companion object {

        const val RESULT_CODE = 100

        const val RESULT = "Result"
        const val TITLE = "Title"
        const val TYPE = "Type"
        const val CONTENT = "Content"

        const val TYPE_TITLE = 0 // 类型 仅一行
        const val TYPE_DESCRIPTION = 1 // 类型 多行

        private const val MAX_TITLE = 30
        private const val MAX_DESCRIPTION = 500

        /**
         * @param context context
         * @param type    类型 one of [InputSetActivity.TYPE_TITLE] [InputSetActivity.TYPE_DESCRIPTION]
         * @param input   已有输入 方便用户修改而不是全部重写
         * @param title   标题
         */
        fun start(context: Context, type: Int, input: String?, title: String?) {
            val intent = Intent(context, InputSetActivity::class.java)
            intent.putExtra(TITLE, title) // 页面显示的标题
            intent.putExtra(TYPE, type) // 输入的类型
            intent.putExtra(CONTENT, input) // 输入框的内容
            (context as AppCompatActivity).startActivityForResult(intent, RESULT_CODE)
        }

    }

    private val type by lazy {
        intent.getIntExtra(TYPE, TYPE_TITLE)
    }

    private val content by lazy {
        intent.getStringExtra(CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selling_add_set)
        setSupportActionBar(findViewById(R.id.toolbar))

        val title = intent.getStringExtra(TITLE)

        set_title.text = title
        if (type == TYPE_TITLE) {
            input.maxLines = 2
        } else {
            input.minLines = 5
        }

        input_warning.visibility = View.GONE
        input.gravity = Gravity.START or Gravity.TOP

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable) {
                if (type == TYPE_TITLE) {
                    input_tip.text = getString(R.string.textRemainCount, MAX_TITLE - input.text.length)
                    if (s.toString().contains(" ") or s.toString().contains("\n")) {
                        input.visibility = View.VISIBLE
                    } else {
                        input_warning.visibility = View.GONE
                    }
                } else {
                    input_tip.text = getString(R.string.textRemainCount, MAX_DESCRIPTION - input.text.length)
                }
            }
        })

        input.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (hasFocus) {
                KeyboardUtils.showSoftInput(v!!)
            } else {
                KeyboardUtils.hideSoftInput(v!!)
            }
        }

        if (type == TYPE_TITLE) {
            input.filters = arrayOf<InputFilter>(LengthFilter(MAX_TITLE))
        } else {
            input.filters = arrayOf<InputFilter>(LengthFilter(MAX_DESCRIPTION))
        }

        input.setText(content)
        if (!input.hasFocus()) {
            input.requestFocus()
        }
    }

    private fun sendData() {
        val intent = Intent()
        intent.putExtra(RESULT, input.text.toString())
        intent.putExtra(TYPE, type)
        setResult(RESULT_OK, intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_done, menu)
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // 当本次修改后文本长度与上次保存的文本长度差超过10时默认为保存而不是丢弃修改
            if (abs(input.text.length - content.length) > 10) {
                sendData()
            }
            finish()
            return true
        } else if (id == R.id.done) {
            if (input_warning.visibility != View.VISIBLE) {
                sendData()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}