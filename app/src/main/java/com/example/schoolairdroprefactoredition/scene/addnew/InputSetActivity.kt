package com.example.schoolairdroprefactoredition.scene.addnew

import android.animation.ObjectAnimator
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
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.components.InputToolKit
import kotlinx.android.synthetic.main.activity_selling_add_set.*


/**
 * [AddNewActivity]页面进入的输入标题和详细信息的页面
 */
class InputSetActivity : ImmersionStatusBarActivity(), InputToolKit.InputToolKitActionListener {

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
         * [R.string.goodsDescription] 物品描述
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
        intent.getStringExtra(CONTENT) ?: ""
    }

    private val sadSpannable by lazy {
//        SadSpannable(this, SadSpannable.parseJsonToSpannableJsonStyle(contentJson))
//        SadSpannable(this, SadSpannable.parseJsonToSpannableJsonStyle("{\n" +
//                "  \"string\": \"1234567890abcdefghijklmnopqrstuvwxyz\",\n" +
//                "  \"style\": {\n" +
//                "    \"bold\": {\n" +
//                "      \"range\": \"1,5|5,7|7,10|12,18|18,19|19,20|\",\n" +
//                "      \"value\": \"\"\n" +
//                "    },\n" +
//                "    \"color\": {\n" +
//                "      \"range\": \"1,5|10,12|12,18|18,19|20,25|\",\n" +
//                "      \"value\": \"red|red|red|red|red|\"\n" +
//                "    },\n" +
//                "    \"delete\": {\n" +
//                "      \"range\": \"12,18|18,19|19,20|\",\n" +
//                "      \"value\": \"\"\n" +
//                "    },\n" +
//                "    \"size\": {\n" +
//                "      \"range\": \"1,5|7,10|10,12|18,19|19,20|20,25|25,29|\",\n" +
//                "      \"value\": \"l|s|s|xl|xl|xl|xl|\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}"))
    }

    /**
     * 当前键盘高度
     */
    private var keyboardHeight = 0f

    /**
     * 当前是否加粗
     */
    private var isBold = false

    /**
     * 当前是否删除线
     */
    private var isDelete = false

    /**
     * 当前文字字体大小
     */
    private var spannableTextSize = InputToolKit.TEXT_SIZE_M

    /**
     * 当前是否是强调色
     */
    private var isStressTextColor = false

    /**
     * 文字是否被初始化解析
     *
     * 在页面进入时input view会初始化并解析spannable字符串
     * 解析完毕后置为true
     */
    private var isSpannableInitialized = false

    /**
     * 弹出工具栏动画
     */
    private val ejectToolKit by lazy {
        Runnable {
            if (keyboardHeight > 300) {
                ObjectAnimator.ofFloat(
                        input_tool_kit_bar,
                        "translationY",
                        input_tool_kit_bar.translationY,
                        -keyboardHeight - 25,
                        -keyboardHeight).apply {
                    duration = 550L
                    interpolator = DecelerateInterpolator(0.8f)
                }.start()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selling_add_set)
        setSupportActionBar(findViewById(R.id.toolbar))

        val title = intent.getStringExtra(TITLE)
        set_title.text = title
        if (type == TYPE_TITLE) {
            input.maxLines = 2
            input_tool_kit_bar.visibility = View.GONE
        } else if (type == TYPE_DESCRIPTION) {
            input_tip.visibility = View.GONE
            input_tool_kit_bar.visibility = View.GONE
            input.minLines = 5
//            input.maxLines = 7
//            input_tool_kit_bar.setInputToolKitActionListener(this)
//            KeyboardUtils.registerSoftInputChangedListener(this) {
//                // 记录键盘高度，在键盘高度不变后将根据此高度拉工具栏
//                keyboardHeight = it.toFloat()
//                if (it > 300) {
////                 上面是工具栏已经打开的状态那么这里就是没有打开而准备打开的状态
////                 这也意味着，在回调附近时间内工具栏不应该是可见的
////                 若回调时工具栏高度大于0说明是隐藏键盘，直接隐藏工具栏
//                    input_tool_kit_bar.postDelayed({
//                        ejectToolKit.run()
//                    }, 120L)
//                } else {
//                    input_tool_kit_bar.translationY = 0f
//                }
//            }
        }

        input_warning.visibility = View.INVISIBLE
        input.gravity = Gravity.START or Gravity.TOP
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 初始化使的改变不做处理
//                if (!isSpannableInitialized) {
//                    isSpannableInitialized = true
//                    return
//                }
//
//                if (s.isBlank()) return
//
//                (s as SpannableStringBuilder).apply {
//                    val end = start + count
//                    if (isBold) {
//                        setSpan(
//                                StyleSpan(Typeface.BOLD),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    } else {
//                        setSpan(
//                                NonBoldSpan(),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    }
//
//                    if (isDelete) {
//                        setSpan(
//                                StrikethroughSpan(),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    } else {
//                        setSpan(
//                                NonStrikethroughSpan(),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    }
//
//                    if (isStressTextColor) {
//                        setSpan(
//                                ForegroundColorSpan(resources.getColor(InputToolKit.TEXT_COLOR_RED, theme)),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    } else {
//                        setSpan(
//                                ForegroundColorSpan(resources.getColor(InputToolKit.TEXT_COLOR_BLACK, theme)),
//                                start,
//                                end,
//                                Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                    }
//
//                    setSpan(
//                            AbsoluteSizeSpan(MyUtil.dp2px(spannableTextSize)),
//                            start,
//                            end,
//                            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                }
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
            input.setText(content)
        } else {
//            input.filters = arrayOf<InputFilter>(LengthFilter(MAX_DESCRIPTION))
//            input.text = sadSpannable
            input.setText(content)
        }

        input.requestFocus()


//        set_title.setOnClickListener {
//            val json = SadSpannable.generateJson(this, input.text)
//            LogUtils.d(json)
//            input.text = SadSpannable(this,SadSpannable.parseJsonToSpannableJsonStyle(json))
//        }
    }

    /**
     * 将用户输入返回给上一个页面
     */
    private fun sendData() {
        // 直接将spannable传出去，让add new页面去生成json字符串
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
//            if (abs(input.text.length - (content?.length ?: 0)) > 10) {
//                sendData()
//            }
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

    override fun onTextSizeChange(textSize: Int) {
        spannableTextSize = textSize
    }

    override fun onTextBoldToggle(bold: Boolean) {
        isBold = bold
    }

    override fun onTextStressColorToggle(isStressColor: Boolean) {
        isStressTextColor = isStressColor
    }

    override fun onTextDeleteLineToggle(delete: Boolean) {
        isDelete = delete
    }
}