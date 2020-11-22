package com.example.schoolairdroprefactoredition.ui.adapter

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemOnGoingBinding
import com.example.schoolairdroprefactoredition.domain.OnGoingData
import com.example.schoolairdroprefactoredition.scene.transactionstate.TransactionStateActivity.Companion.start
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import java.lang.NullPointerException

class OnGoingRecyclerAdapter : BaseQuickAdapter<OnGoingData, BaseViewHolder>(R.layout.item_on_going) {
    override fun convert(holder: BaseViewHolder, item: OnGoingData) {
        val binding = ItemOnGoingBinding.bind(holder.itemView)

        try {
            ImageUtil.loadRoundedImage(binding.onGoingImg, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + item.onGoingGoods_info.goods_img_cover)
            binding.onGoingTitle.text = item.onGoingGoods_info.goods_name
            binding.onGoingPrice.setPrice(item.price)

            binding.onGoingUserName.apply {
                if (item.onGoingSeller_info != null) {
                    text = item.onGoingSeller_info.uname
                } else if (item.onGoingBuyer_info != null) {
                    text = item.onGoingBuyer_info.uname
                }
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
        } catch (e: NullPointerException) {
            LogUtils.d("null")
        }
    }
}