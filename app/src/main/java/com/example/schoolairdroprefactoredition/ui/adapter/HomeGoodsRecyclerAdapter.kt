package com.example.schoolairdroprefactoredition.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemHomeGoodsInfoBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

/**
 * 附近在售列表的adapter
 */
class HomeGoodsRecyclerAdapter : BaseFooterAdapter<HomeGoodsListInfo.DataBean, BaseViewHolder>(R.layout.item_home_goods_info) {
    override fun convert(holder: BaseViewHolder, item: HomeGoodsListInfo.DataBean) {
        try {
            val binding = ItemHomeGoodsInfoBinding.bind(holder.itemView)
            val negotiable = item.goods_is_quotable == 1
            val secondHand = item.goods_is_brandNew == 0

            ImageUtil.loadRoundedImage(binding.itemImage, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + item.goods_img_cover)
            binding.itemPrice.setPrice(item.goods_price)
            binding.itemSeller.text = item.seller_info
            if (context is AppCompatActivity) {
                val bundle = (context as AppCompatActivity).intent.extras

                binding.root.setOnClickListener {
                    GoodsActivity.start(
                            context,
                            bundle?.getSerializable(ConstantUtil.KEY_TOKEN) as? DomainToken,
                            bundle?.getSerializable(ConstantUtil.KEY_USER_INFO),
                            item,
                            false)
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