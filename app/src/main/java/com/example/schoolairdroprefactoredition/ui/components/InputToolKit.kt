package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.android.synthetic.main.component_input_tool_kit.view.*
import kotlinx.android.synthetic.main.dialog_text_size_select.view.*


/**
 * @author kennen
 * @date 2021/4/9
 *
 * 物品描述输入页面的富文本工具类
 */
class InputToolKit : ConstraintLayout {

    companion object {
        // 字体颜色
        const val TEXT_COLOR_BLACK = R.color.black
        const val TEXT_COLOR_RED = R.color.colorPrimaryRed
        const val TEXT_COLOR_GREEN = R.color.colorPrimaryGreen
        const val TEXT_COLOR_BLUE = R.color.colorAccentDark

        // 字体大小
        const val TEXT_SIZE_S = 12
        const val TEXT_SIZE_M = 15
        const val TEXT_SIZE_L = 20
        const val TEXT_SIZE_XL = 25
    }

    private var inputToolKitActionListener: InputToolKitActionListener? = null

    /**
     * 字体加粗
     */
    private var isBold = false

    /**
     * 字体是否为强调色
     */
    private var isStressColor = false

    /**
     * 字体中划线
     */
    private var isDeleteLine = false

    /**
     * 字体大小
     *
     * 小、中、大、特大
     */
    private var textSize = SizeUtils.dp2px(14f)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.component_input_tool_kit, this, true)

        input_tool_kit_text_bold.setOnClickListener {
            it.isSelected = it.isSelected.not()
            val isSelected = it.isSelected
            isBold = isSelected
            inputToolKitActionListener?.onTextBoldToggle(isSelected)
        }
        input_tool_kit_middle_line.setOnClickListener {
            it.isSelected = it.isSelected.not()
            val isSelected = it.isSelected
            isDeleteLine = isSelected
            inputToolKitActionListener?.onTextDeleteLineToggle(isSelected)
        }
        input_tool_kit_text_size.setOnClickListener {
            // 显示选择字体
            XPopup.Builder(context)
                    .hasShadowBg(false)
                    .isRequestFocus(false)
                    .asCustom(object : BasePopupView(context) {
                        override fun getPopupLayoutId(): Int {
                            return R.layout.dialog_text_size_select
                        }

                        override fun init() {
                            super.init()
                            val textS = findViewById<TextView>(R.id.dialog_text_size_s)
                            val textM = findViewById<TextView>(R.id.dialog_text_size_m)
                            val textL = findViewById<TextView>(R.id.dialog_text_size_l)
                            val textXL = findViewById<TextView>(R.id.dialog_text_size_xl)
                            deSelectAll(textS, textM, textL, textXL)
                            textS.setOnClickListener {
                                deSelectAll(textS, textM, textL, textXL)
                                textS.isSelected = true
                                textSize = TEXT_SIZE_S
                                this@InputToolKit.inputToolKitActionListener?.onTextSizeChange(textSize)
                            }
                            textM.setOnClickListener {
                                deSelectAll(textS, textM, textL, textXL)
                                textM.isSelected = true
                                textSize = TEXT_SIZE_M
                                this@InputToolKit.inputToolKitActionListener?.onTextSizeChange(textSize)
                            }
                            textL.setOnClickListener {
                                deSelectAll(textS, textM, textL, textXL)
                                textL.isSelected = true
                                textSize = TEXT_SIZE_L
                                this@InputToolKit.inputToolKitActionListener?.onTextSizeChange(textSize)
                            }
                            textXL.setOnClickListener {
                                deSelectAll(textS, textM, textL, textXL)
                                textXL.isSelected = true
                                textSize = TEXT_SIZE_XL
                                this@InputToolKit.inputToolKitActionListener?.onTextSizeChange(textSize)
                            }
                            when (textSize) {
                                TEXT_SIZE_S -> {
                                    textS.isSelected = true
                                }
                                TEXT_SIZE_M -> {
                                    textM.isSelected = true
                                }
                                TEXT_SIZE_L -> {
                                    textL.isSelected = true
                                }
                                TEXT_SIZE_XL -> {
                                    textXL.isSelected = true
                                }
                            }
                        }

                        fun deSelectAll(textS: View, textM: View, textL: View, textXL: View) {
                            textS.isSelected = false
                            textM.isSelected = false
                            textL.isSelected = false
                            textXL.isSelected = false
                        }
                    })
                    .show()
        }
        input_tool_kit_text_color.setOnClickListener {
            // 显示选择字体颜色
            isStressColor = isStressColor.not()
            if (isStressColor) {
                input_tool_kit_text_color.setImageResource(R.drawable.bg_round_red)
            } else {
                input_tool_kit_text_color.setImageResource(R.drawable.bg_round_black)
            }
            inputToolKitActionListener?.onTextStressColorToggle(isStressColor)
        }
    }

    interface InputToolKitActionListener {
        /**
         * 文本大小设置改变
         */
        fun onTextSizeChange(textSize: Int)

        /**
         * 粗体toggle
         */
        fun onTextBoldToggle(bold: Boolean)

        /**
         * 字体颜色改变
         */
        fun onTextStressColorToggle(isStressColor: Boolean)

        /**
         * 中划线toggle
         */
        fun onTextDeleteLineToggle(delete: Boolean)
    }

    fun setInputToolKitActionListener(listener: InputToolKitActionListener) {
        inputToolKitActionListener = listener
    }
}