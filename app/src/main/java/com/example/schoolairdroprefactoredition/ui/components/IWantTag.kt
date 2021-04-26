package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.item_flex_tag.view.*


/**
 * @author kennen
 * @date 2021/4/23
 */
class IWantTag : ConstraintLayout {

    companion object {
        private var selectedTagPos = -1

        fun setselectedTagPos(pos: Int) {
            selectedTagPos = pos
        }
    }

    private var onIWantTagActionListener: OnIWantTagActionListener? = null

    constructor(context: Context, tag: DomainIWantTags.Data) : this(context, null, tag)
    constructor(context: Context, attrs: AttributeSet?, tag: DomainIWantTags.Data) : this(context, attrs, 0, tag)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, tag: DomainIWantTags.Data) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.item_flex_tag, this, true)

        val padding = SizeUtils.dp2px(3f)
        setPadding(padding, padding, padding, padding)
        iwant_tag.text = tag.tags_content
        iwant_tag.setOnClickListener {
            if (!iwant_tag.isSelected) {
                if (parent is FlexboxLayout) {
                    (parent as FlexboxLayout).let { parentView ->
                        // 取消选中之前选中的tag
                        if (selectedTagPos != -1) {
                            (parentView.getChildAt(selectedTagPos) as ViewGroup?)?.getChildAt(0)?.isSelected = false
                        }
                        // 将当前选中tag的pos记录下来
                        selectedTagPos = parentView.indexOfChild(this)
                    }
                }
                // 选中当前的tag
                iwant_tag.isSelected = true
                this@IWantTag.onIWantTagActionListener?.onIWantTagClick(tag)
            }
        }
    }

    interface OnIWantTagActionListener {
        /**
         * 标签被点击，将该标签所带信息给到外面
         */
        fun onIWantTagClick(tag: DomainIWantTags.Data)
    }

    fun setOnIWantTagActionListener(listener: OnIWantTagActionListener) {
        this.onIWantTagActionListener = listener
    }
}