package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemOnGoingBinding
import com.example.schoolairdroprefactoredition.domain.DomainOnGoing
import com.example.schoolairdroprefactoredition.scene.transactionstate.TransactionStateActivity.Companion.start
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class OnGoingRecyclerAdapter : BaseQuickAdapter<DomainOnGoing.Data, BaseViewHolder>(R.layout.item_on_going) {
    override fun convert(holder: BaseViewHolder, item: DomainOnGoing.Data) {
        val binding = ItemOnGoingBinding.bind(holder.itemView)
        ImageUtil.loadImage(binding.onGoingImg, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + item.goods_info[0].goods_img_cover)
        binding.onGoingTitle.text = item.goods_info[0].goods_name
        binding.onGoingPrice.setPrice(item.price)
        binding.onGoingUserName.text = item.sender_info.uname

        holder.itemView.setOnClickListener { start(context) }
    }
}