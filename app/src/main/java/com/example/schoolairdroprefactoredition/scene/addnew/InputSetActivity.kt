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
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.qiniu.android.utils.LogUtil
import kotlinx.android.synthetic.main.activity_selling_add_set.*
import kotlin.math.abs

/**
 * [AddNewActivity]页面进入的输入标题和详细信息的页面
 */
class InputSetActivity : ImmersionStatusBarActivity() {

    companion object {

        /**
         * OnActivityResult的请求码 请求码
         */
        const val REQUEST_CODE = 77

        /**
         * 返回结果 键
         *
         * 在返回的页面OnActivity中获取结果值的键
         */
        const val RESULT = "KEY_Result"

        /**
         * 页面标题显示的文字 键
         *
         * one of
         * [R.string.title] 标题
         * [R.string.goods_description] 物品描述
         */
        const val TITLE = "KEY_Title"

        /**
         * 页面输入类型 键
         *
         * 选取以下对应的值之一
         * [TYPE_TITLE] 标题
         * [TYPE_DESCRIPTION] 详细描述
         */
        const val TYPE = "KEY_Type"

        /**
         * 页面进入时已输入的内容 键
         */
        const val CONTENT = "KEY_Content"


        /**
         * 标题 页面输入类型
         *
         * 是页面类型键对应的值之一[TYPE]
         * 标题类型不能输入换行字符
         */
        const val TYPE_TITLE = 0

        /**
         * 详述 页面输入类型
         *
         * 是页面类型键对应的值之一[TYPE]
         * 详述类型可以输入若干行文字和任意字符
         */
        const val TYPE_DESCRIPTION = 1


        /**
         * 标题 输入最大字数限制
         */
        private const val MAX_TITLE = 30

        /**
         * 详述 输入最大字数限制
         */
        private const val MAX_DESCRIPTION = 500

        /**
         * 完成 菜单项
         */
        private var doneMenu: MenuItem? = null

        /**
         * @param context context
         * @param type    页面输入类型 见[TYPE]
         * @param input   之前已有的输入 方便用户修改而不是全部重写 见[CONTENT]
         * @param title   标题 见[TITLE]
         *
         * requestCode [InputSetActivity.REQUEST_CODE]
         * 在OnActivity中获取结果的键 [InputSetActivity.RESULT]
         */
        fun start(context: Context, type: Int, input: String?, title: String?) {
            val intent = Intent(context, InputSetActivity::class.java)
            intent.putExtra(TITLE, title) // 页面显示的标题
            intent.putExtra(TYPE, type) // 输入的类型
            intent.putExtra(CONTENT, input) // 输入框的内容
            (context as AppCompatActivity).startActivityForResult(intent, REQUEST_CODE)
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

        input_warning.visibility = View.INVISIBLE
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

                    // 若含有空格或者换行符，将会提示无法提交
                    if (s.toString().contains(" ") || s.toString().contains("\n")) {
                        doneMenu?.isEnabled = false
                        input_warning.visibility = View.VISIBLE
                    } else {
                        doneMenu?.isEnabled = true
                        input_warning.visibility = View.INVISIBLE
                    }
                } else {
                    input_tip.text = getString(R.string.textRemainCount, MAX_DESCRIPTION - input.text.length)
                }
            }
        })

        input.onFocusChangeListener = OnFocusChangeListener { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                KeyboardUtils.showSoftInput(view)
            } else {
                KeyboardUtils.hideSoftInput(view)
            }
        }

        if (type == TYPE_TITLE) {
            input.filters = arrayOf<InputFilter>(LengthFilter(MAX_TITLE))
        } else {
            input.filters = arrayOf<InputFilter>(LengthFilter(MAX_DESCRIPTION))
        }

        input.setText(content)
        input.requestFocus()
    }

    /**
     * 将用户输入返回给上一个页面
     */
    private fun sendData() {
        intent.putExtra(RESULT, input.text.toString())
        intent.putExtra(TYPE, type)
        setResult(RESULT_OK, intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_done, menu)
        doneMenu = menu.getItem(0).also { it.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // 当本次修改后文本长度与上次保存的文本长度差超过10时默认为保存而不是丢弃修改
            if (abs(input.text.length - (content?.length ?: 0)) > 10) {
                sendData()
            }
            finish()
            return true
        } else if (id == R.id.done) {
            return if (input_warning.visibility != View.VISIBLE) {
                sendData()
                finish()
                true
            } else {
                false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}