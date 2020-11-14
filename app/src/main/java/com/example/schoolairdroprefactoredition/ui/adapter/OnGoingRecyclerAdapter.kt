package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemOnGoingBinding
import com.example.schoolairdroprefactoredition.domain.Data
import com.example.schoolairdroprefactoredition.scene.transactionstate.TransactionStateActivity.Companion.start
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class OnGoingRecyclerAdapter : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_on_going) {
    override fun convert(holder: BaseViewHolder, item: Data) {
        val binding = ItemOnGoingBinding.bind(holder.itemView)

        ImageUtil.loadRoundedImage(binding.onGoingImg, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + item.goods_info.goods_img_cover)
        binding.onGoingTitle.text = item.goods_info.goods_name
        binding.onGoingPrice.setPrice(item.price)

        binding.onGoingUserName.apply {
            if (item.seller_info != null)
                text = item.seller_info.uname
            else if (item.buyer_info != null)
                text = item.buyer_info.uname
        }

        binding.onGoingHeaderTitle.apply {
            when (item.step) {
                0 -> {
                    text = context.getString(R.string.eventStep1)
                }
                1 -> {
                    text = context.getString(R.string.eventStep2)
                }
                2 -> {
                    text = context.getString(R.string.eventStep3)
                }
                3 -> {
                    text = context.getString(R.string.eventStep4)
                }
            }
        }

        holder.itemView.setOnClickListener { start(context) }
    }
}