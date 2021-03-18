package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.databinding.ItemFavoriteBinding
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class FavoriteRecyclerAdapter : BaseQuickAdapter<Favorite, BaseViewHolder>(R.layout.item_favorite) {

    private var mOnFavoriteItemActionListener: OnFavoriteItemActionListener? = null

    interface OnFavoriteItemActionListener {
        /**
         * 切换物品收藏状态
         */
        fun onToggleFavorite(button: View, item: Favorite)
    }

    fun setOnFavoriteItemActionListener(listener: OnFavoriteItemActionListener) {
        mOnFavoriteItemActionListener = listener
    }

    override fun convert(holder: BaseViewHolder, item: Favorite) {
        val binding = ItemFavoriteBinding.bind(holder.itemView)
        val isQuotable = item.goods_is_bargain
        val isSecondHand = item.goods_is_secondHand

        if (isQuotable && isSecondHand) {
            binding.favoriteGoodsTitle.text = context.getString(R.string.itemNSs, item.goods_name)
        } else if (isQuotable) {
            binding.favoriteGoodsTitle.text = context.getString(R.string.itemNs, item.goods_name)
        } else {
            binding.favoriteGoodsTitle.text = context.getString(R.string.itemSs, item.goods_name)
        }

        if (item.goods_is_favor) {
            binding.favoriteMoreAction.setImageResource(R.drawable.ic_favorite_red)
        } else {
            binding.favoriteMoreAction.setImageResource(R.drawable.ic_favorite_white)
        }

        ImageUtil.loadRoundedImage(binding.favoriteGoodsAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.goods_cover_image))
        binding.favoriteGoodsPrice.setPrice(item.goods_price, true)
        binding.favoriteContact.setOnClickListener {
            // 未登录时点击联系先登录
            if (context is AppCompatActivity) {
                if (((context as AppCompatActivity).application as SAApplication).getCachedToken() != null) {
                    ChatActivity.start(context, item.user_id)
                } else {
                    LoginActivity.start(context)
                }
            }
        }
        binding.favoriteMoreAction.setOnClickListener {
            it.isEnabled = false

            mOnFavoriteItemActionListener?.onToggleFavorite(it, item)
        }
        binding.root.setOnClickListener {
            GoodsActivity.start(context, item.goods_id, false)
        }
    }

    fun updateFavor(goodsId: Int, isFavor: Boolean) {
        for ((index, datum) in data.withIndex()) {
            if (datum.goods_id == goodsId) {
                datum.goods_is_favor = isFavor
                notifyItemChanged(index)
                return
            }
        }
    }
}