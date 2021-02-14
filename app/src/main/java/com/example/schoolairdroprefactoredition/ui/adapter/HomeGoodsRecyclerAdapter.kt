package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemHomeGoodsInfoBinding
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

/**
 * 附近在售列表的adapter
 */
class HomeGoodsRecyclerAdapter : BaseFooterAdapter<DomainPurchasing.DataBean, BaseViewHolder>(R.layout.item_home_goods_info) {
    override fun convert(holder: BaseViewHolder, item: DomainPurchasing.DataBean) {
        try {
            val binding = ItemHomeGoodsInfoBinding.bind(holder.itemView)
            val negotiable = item.goods_is_quotable == 1
            val secondHand = item.goods_is_brandNew == 0

            ImageUtil.loadRoundedImage(binding.itemImage, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ImageUtil.fixUrl(item.goods_img_cover))
            binding.itemPrice.setPrice(item.goods_price)
            binding.itemSeller.text = item.seller_info
            if (context is AppCompatActivity) {
                binding.root.setOnClickListener {
                    GoodsActivity.start(context, item, false)
                }
            }
            if (negotiable && secondHand) {
                binding.itemTitle.text = context.resources.getString(R.string.itemNSs, item.goods_name)
            } else if (negotiable) {
                binding.itemTitle.text = context.resources.getString(R.string.itemNs, item.goods_name)
            } else if (secondHand) {
                binding.itemTitle.text = context.resources.getString(R.string.itemSs, item.goods_name)
            } else {
                binding.itemTitle.text = item.goods_name
            }
        } catch (ignored: NullPointerException) {
        }
    }
}