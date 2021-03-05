package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsDetailBinding
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil.getArrayFromString
import com.facebook.shimmer.ShimmerFrameLayout

class GoodsInfo @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ShimmerFrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding by lazy {
        ComponentGoodsDetailBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var mOnUserInfoClickListener: OnUserInfoClickListener? = null

    init {
        binding.goodsAvatar.setOnClickListener(this)
        binding.goodsUserName.setOnClickListener(this)
        binding.goodsBottom.visibility = GONE
    }

    /**
     * 是否隐藏页面最底部为三个按钮的留白
     * 当为自己的物品时，三个按钮隐藏，留白也应该隐藏
     */
    fun showBottom(show: Boolean) {
        binding.goodsBottom.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 隐藏卖家信息
     * 从在售页面进入的物品界面要隐藏卖家信息
     * 防止在查看他人在售列表时
     * 点击物品 -> 查看用户信息 -> 点击在售 -> 再点击物品…
     * 无限套娃访问
     */
    fun hideSellerInfo() {
        binding.goodsSellerInfo.visibility = GONE
    }

    fun setData(baseInfo: DomainGoodsAllDetailInfo.Data?) {
        if (baseInfo != null) {
            val negotiable = baseInfo.goods_is_bargain // 是否可议价
            val secondHand = baseInfo.goods_is_secondHand // 是否二手

            if (negotiable && secondHand) {
                binding.goodsName.text = context.resources.getString(R.string.itemNS, baseInfo.goods_name)
            } else if (negotiable) {
                binding.goodsName.text = context.resources.getString(R.string.itemN, baseInfo.goods_name)
            } else if (secondHand) {
                binding.goodsName.text = context.resources.getString(R.string.itemS, baseInfo.goods_name)
            } else {
                binding.goodsName.text = baseInfo.goods_name
            }

            binding.apply {
                goodsPrice.setPrice(baseInfo.goods_price)
                goodsPager.setData(getArrayFromString(baseInfo.goods_images).also {
                    it.drop(0)
                }, false)
                goodsDescription.text = baseInfo.goods_content
                goodsPopularity.setWatches(baseInfo.goods_watch_count)
                goodsPopularity.setLikes(baseInfo.goods_favor_count)
                goodsPopularity.setComments(baseInfo.goods_chat_count)
                goodsUserName.text = baseInfo.seller.user_name
                ImageUtil.loadRoundedImage(goodsAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(baseInfo.seller.user_avatar))
            }

        }
    }

    /**
     * 若仍在闪烁则停止闪烁
     */
    fun stopShimming() {
        if (isShimmerVisible || isShimmerStarted) {
            stopShimmer()
            hideShimmer()
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.goodsAvatar || id == R.id.goods_user_name) {
            mOnUserInfoClickListener?.onUserInfoClick(v)
        }
    }

    interface OnUserInfoClickListener {
        fun onUserInfoClick(view: View?)
    }

    fun setOnUserInfoClickListener(listener: OnUserInfoClickListener?) {
        mOnUserInfoClickListener = listener
    }
}