package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsDetailRefactorBinding
import com.example.schoolairdroprefactoredition.domain.DomainGoodsAllDetailInfo
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.facebook.shimmer.ShimmerFrameLayout
import java.lang.Exception

class GoodsInfo
@JvmOverloads
constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShimmerFrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding by lazy {
        ComponentGoodsDetailRefactorBinding.inflate(LayoutInflater.from(context), this, true)
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
     */
    fun hideSellerInfo() {
        binding.goodsSellerInfo.visibility = GONE
    }

    /**
     * 收藏数量加一或减一
     * @param isFavor true加一，false减一
     */
    fun updateFavor(isFavor: Boolean) {
        if (isFavor) {
            binding.goodsPopularity.likes++
        } else {
            binding.goodsPopularity.likes--
        }
    }

    /**
     * 装填数据
     */
    fun setData(item: DomainGoodsAllDetailInfo.Data?) {
        if (item != null) {
            val negotiable = item.goods_is_bargain // 是否可议价
            val secondHand = item.goods_is_secondHand // 是否二手

            if (negotiable && secondHand) {
                binding.goodsName.text = context.resources.getString(R.string.itemNS, item.goods_name)
            } else if (negotiable) {
                binding.goodsName.text = context.resources.getString(R.string.itemN, item.goods_name)
            } else if (secondHand) {
                binding.goodsName.text = context.resources.getString(R.string.itemS, item.goods_name)
            } else {
                binding.goodsName.text = item.goods_name
            }

            binding.apply {
                goodsPrice.setPrice(item.goods_price, false)
                goodsImageLoader.setData(MyUtil.getArrayFromString(item.goods_images))

//                val spanned = SadSpannable(context,SadSpannable.parseJsonToSpannableJsonStyle(item.goods_content))
                goodsDescription.text = item.goods_content
                goodsPopularity.setWatches(item.goods_watch_count)
                goodsPopularity.likes = item.goods_favor_count

                try {
                    goodsUserLastLoginTime.text = context?.getString(R.string.lastActiveTime, TimeUtils.getFriendlyTimeSpanByNow(item.seller.last_login_time))
                } catch (e: Exception) {
                    goodsUserLastLoginTime.visibility = View.GONE
                }

                goodsUserName.text = item.seller.user_name
                ImageUtil.loadRoundedImage(goodsAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.seller.user_avatar))
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