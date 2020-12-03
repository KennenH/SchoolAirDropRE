package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity
import com.example.schoolairdroprefactoredition.ui.components.RecyclerFooter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class HomeNewsRecyclerAdapter : BaseMultiItemQuickAdapter<BaseHomeNewsEntity, BaseViewHolder>() {

    companion object {
        const val TYPE_ONE = 0
        const val TYPE_TWO = 1
    }

    init {
        addItemType(TYPE_ONE, R.layout.item_home_news_1)
        addItemType(TYPE_TWO, R.layout.item_home_news_2)
    }

    private var mOnNoMoreDataListener: ArrayList<OnNoMoreDataListener> = ArrayList()

    private var mOnHomePostActionClickListener: ArrayList<OnHomePostActionClickListener> = ArrayList()

    override fun convert(holder: BaseViewHolder, item: BaseHomeNewsEntity) {
        when (holder.itemViewType) {
            TYPE_ONE -> {
                val pager: CardView = holder.itemView.findViewById(R.id.root)
                val title: TextView = holder.itemView.findViewById(R.id.news_title)
                holder.itemView.setOnClickListener {
                    for (listener in mOnHomePostActionClickListener) {
                        listener.onHomePostItemClicked(pager, title)
                    }
                }
                ImageUtil.loadRoundImage(holder.itemView.findViewById(R.id.news_avatar), "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606042040652&di=38cf0771633e1235ad6ce15ababccf93&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201410%2F22%2F20141022073033_nBdWH.jpeg")
                ImageUtil.loadImage(holder.itemView.findViewById(R.id.news_image), item.url, R.drawable.placeholder_rounded)
                holder.setText(R.id.news_title, item.title)
            }

            TYPE_TWO -> {
                ImageUtil.loadImage(holder.itemView.findViewById(R.id.news_image2), item.url, R.drawable.logo_placeholder)
                holder.setText(R.id.news_title2, item.title)
            }
        }
    }

    interface OnHomePostActionClickListener {
        fun onHomePostItemClicked(pager: CardView, title: TextView)
    }

    fun setOnHomePostItemClickListener(listener: OnHomePostActionClickListener) {
        mOnHomePostActionClickListener.add(listener)
    }

    override fun setList(list: Collection<BaseHomeNewsEntity>?) {
        super.setList(list)
        for (listener in mOnNoMoreDataListener) {
            listener.onNoMoreDataRefresh()
        }
        removeAllFooterView()
        if (list != null && list.size < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
            addFooter()
        }
    }

    override fun addData(newData: Collection<BaseHomeNewsEntity>) {
        super.addData(newData)
        if (newData.size < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
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