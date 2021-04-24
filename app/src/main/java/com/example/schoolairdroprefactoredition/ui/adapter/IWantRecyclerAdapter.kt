package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.database.BaseIWantEntity
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantRecyclerAdapter : BaseMultiItemQuickAdapter<BaseIWantEntity, BaseViewHolder>() {

    companion object {
        /**
         * 普通item类型
         */
        const val TYPE_ONE = 0


        /**
         * 求购卡片的默认颜色，在不同模式下的表现不同
         *
         * primaryDark
         */
        const val COLOR_DEFAULT = 0

        /**
         * 求购卡片紫色
         *
         * colorPrimaryPurple
         */
        const val COLOR_PURPLE = 1

        /**
         * 求购卡片主题色
         *
         * colorAccentDark
         */
        const val COLOR_THEME = 2

        /**
         * 求购卡片心色
         *
         * heart
         */
        const val COLOR_RED = 3

        /**
         * 求购卡片橘色
         *
         * yellow
         */
        const val COLOR_ORANGE = 4
    }

    init {
        addItemType(TYPE_ONE, R.layout.item_iwant)
    }

    private var mOnNoMoreDataListener: ArrayList<OnNoMoreDataListener> = ArrayList()

    private var mOnInquiryItemClickListener: ArrayList<OnInquiryItemClickListener> = ArrayList()

    override fun convert(holder: BaseViewHolder, item: BaseIWantEntity) {
//        when (holder.itemViewType) {
//            TYPE_ONE -> {
        val cardView: CardView = holder.itemView.findViewById(R.id.item_iwant_color_wrapper)
        val constraintView: ConstraintLayout = holder.itemView.findViewById(R.id.item_iwant_content_wrapper)
        val contentView: TextView = holder.itemView.findViewById(R.id.item_iwant_content)
        val hasImageView: ImageView = holder.itemView.findViewById(R.id.item_iwant_has_image)

        if (item.images?.isEmpty() == true) {
            hasImageView.visibility = View.GONE
        }
        val resource = context.resources
        val theme = context.theme
        val blackText = resource.getColor(R.color.black, theme)
        val blackAlwaysText = resource.getColor(R.color.blackAlways, theme)
        val whiteAlwaysText = resource.getColor(R.color.whiteAlways, theme)
        when (item.color) {
            COLOR_RED -> {
                constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_red, theme)
                contentView.setTextColor(whiteAlwaysText)
                hasImageView.setImageResource(R.drawable.ic_has_image_white)
            }
            COLOR_ORANGE -> {
                constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_yellow, theme)
                contentView.setTextColor(blackAlwaysText)
                hasImageView.setImageResource(R.drawable.ic_has_image_black)
            }
            COLOR_PURPLE -> {
                constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_purple, theme)
                contentView.setTextColor(whiteAlwaysText)
                hasImageView.setImageResource(R.drawable.ic_has_image_white)
            }
            COLOR_THEME -> {
                constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_theme, theme)
                contentView.setTextColor(whiteAlwaysText)
                hasImageView.setImageResource(R.drawable.ic_has_image_white)
            }
            else -> {
                constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded, theme)
                contentView.setTextColor(blackText)
                hasImageView.setImageResource(R.drawable.ic_has_image)
            }
        }

        holder.setText(R.id.item_iwant_tag, context.getString(R.string.iwant_tag, item.tag))
        holder.itemView.setOnClickListener {
            for (listener in mOnInquiryItemClickListener) {
                listener.onHomePostItemClicked(cardView, item)
            }
        }
        holder.setText(R.id.item_iwant_content, item.content)
//            }
//        }
    }

    /**
     * 求购页面recycler view中item的点击监听器
     * 将页面打开时的元素共享逻辑交由外部实现
     */
    interface OnInquiryItemClickListener {
        /**
         * item按下，外部开始共享元素动画并打开页面
         */
        fun onHomePostItemClicked(card: View, item: BaseIWantEntity)
    }

    fun setOnInquiryItemClickListener(listener: OnInquiryItemClickListener) {
        mOnInquiryItemClickListener.add(listener)
    }

    override fun setList(list: Collection<BaseIWantEntity>?) {
        super.setList(list)
        for (listener in mOnNoMoreDataListener) {
            listener.onNoMoreDataRefresh()
        }
        removeAllFooterView()
        if (list != null && list.size < ConstantUtil.DEFAULT_PAGE_SIZE) {
            addFooter()
        }
    }

    override fun addData(newData: Collection<BaseIWantEntity>) {
        super.addData(newData)
        if (newData.size < ConstantUtil.DEFAULT_PAGE_SIZE) {
            addFooter()
        }
    }

    private fun addFooter() {
        for (listener in mOnNoMoreDataListener) {
            listener.onNoMoreData()
        }
        val footer = RecyclerFooter(context)
        footer.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addFooterView(footer)
    }

    interface OnNoMoreDataListener {
        /**
         * 加载的数据已经不足默认数据条数，说明已经没有更多数据
         */
        fun onNoMoreData()

        /**
         * recycler已重新刷新，将没有更多数据标志位恢复
         */
        fun onNoMoreDataRefresh()
    }

    fun setOnNoMoreDataListener(listener: OnNoMoreDataListener) {
        mOnNoMoreDataListener.add(listener)
    }
}