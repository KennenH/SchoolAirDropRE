package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.HomeMessagesBean
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class HomeMessagesRecyclerAdapter : BaseQuickAdapter<HomeMessagesBean, BaseViewHolder>(R.layout.item_home_messages) {

    override fun convert(holder: BaseViewHolder, item: HomeMessagesBean) {
//        ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.messages_avatar), item.avatar)
//        holder.setText(R.id.messages_title, item.title)
//        holder.setText(R.id.messages_abstract, item.content)

        holder.itemView.setOnClickListener {
            ChatActivity.start(context)
        }
    }

}