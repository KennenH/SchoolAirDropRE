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
class PurchasingRecyclerAdapter : BaseFooterAdapter<DomainPurchasing.DataBean, BaseViewHolder>(R.layout.item_home_goods_info) {
    override fun convert(holder: BaseViewHolder, item: DomainPurchasing.DataBean) {

        val binding = ItemHomeGoodsInfoBinding.bind(holder.itemView)
        val negotiable = item.isGoods_is_bargain
        val secondHand = item.isGoods_is_secondHand

        ImageUtil.loadRoundedImage(binding.itemImage, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.goods_cover_image))
        binding.itemPrice.setPrice(item.goods_price)
        binding.itemSeller.text = item.seller.user_name
        if (context is AppCompatActivity) {
            binding.root.setOnClickListener {
                GoodsActivity.start(context, item.goods_id, false)
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
    }
}