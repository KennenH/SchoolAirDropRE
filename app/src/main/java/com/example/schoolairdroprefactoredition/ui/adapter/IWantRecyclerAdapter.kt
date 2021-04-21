package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantRecyclerAdapter : BaseMultiItemQuickAdapter<BaseIWantEntity, BaseViewHolder>() {

    companion object {
        /**
         * 普通item类型
         */
        const val TYPE_ONE = 0


        const val COLOR_GREY = 0

        const val COLOR_PURPLE = 1

        const val COLOR_THEME = 2
    }

    init {
//        addItemType(TYPE_ONE, R.layout.item_home_news_1)
        addItemType(TYPE_ONE, R.layout.item_iwant)
//        addItemType(TYPE_TWO, R.layout.item_home_news_2)
    }

    private var mOnNoMoreDataListener: ArrayList<OnNoMoreDataListener> = ArrayList()

    private var mOnInquiryItemClickListener: ArrayList<OnInquiryItemClickListener> = ArrayList()

    override fun convert(holder: BaseViewHolder, item: BaseIWantEntity) {
        when (holder.itemViewType) {
            TYPE_ONE -> {
                val cardView: CardView = holder.itemView.findViewById(R.id.item_iwant_color_wrapper)
                val constraintView: ConstraintLayout = holder.itemView.findViewById(R.id.item_iwant_content_wrapper)
                val contentView: TextView = holder.itemView.findViewById(R.id.item_iwant_content)
//                val avatarView: ImageView = holder.itemView.findViewById(R.id.item_idesire_avatar)
//                val nameView: TextView = holder.itemView.findViewById(R.id.item_idesire_name)

                val resource = context.resources
                val theme = context.theme
                val blackText = resource.getColor(R.color.black, theme)
                val whiteAlwaysText = resource.getColor(R.color.whiteAlways, theme)
                when (item.color) {
                    COLOR_PURPLE -> {
                        constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_purple, theme)
                        contentView.setTextColor(whiteAlwaysText)
                    }
                    COLOR_THEME -> {
                        constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded_theme, theme)
                        contentView.setTextColor(whiteAlwaysText)
                    }
                    else -> {
                        constraintView.background = ResourcesCompat.getDrawable(resource, R.drawable.bg_top_rounded, theme)
                        contentView.setTextColor(blackText)
                    }
                }

                holder.itemView.setOnClickListener {
                    for (listener in mOnInquiryItemClickListener) {
                        listener.onHomePostItemClicked(cardView, item)
                    }
                }
                holder.setText(R.id.item_iwant_content, item.content)
//                val title: CardView = holder.itemView.findViewById(R.id.root)
//                holder.itemView.setOnClickListener {
//                    for (listener in mOnInquiryItemClickListener) {
//                        listener.onHomePostItemClicked(title)
//                    }
//                }
//                ImageUtil.loadRoundImage(holder.itemView.findViewById(R.id.news_avatar), "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606042040652&di=38cf0771633e1235ad6ce15ababccf93&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201410%2F22%2F20141022073033_nBdWH.jpeg", R.drawable.ic_logo_text_alpha_20)
//                ImageUtil.loadImage(holder.itemView.findViewById(R.id.news_image), item.url, R.drawable.placeholder_rounded)
//                holder.setText(R.id.news_title, item.title)
            }

//            TYPE_TWO -> {
//                ImageUtil.loadImage(holder.itemView.findViewById(R.id.news_image2), item.url, R.drawable.logo_placeholder)
//                holder.setText(R.id.news_title2, item.title)
//            }
        }
    }

    /**
     * 求购页面recycler view中item的点击监听器
     * 将页面打开时的元素共享逻辑交由外部实现
     */
    interface OnInquiryItemClickListener {
        /**
         * item按下回调
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