package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemSsbSellingBinding
import com.example.schoolairdroprefactoredition.domain.DomainSelling
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class SellingAdapter(private val isMine: Boolean?) : BaseQuickAdapter<DomainSelling.Data?, BaseViewHolder>(R.layout.item_ssb_selling) {

    private var mOnSSBItemActionListeners: OnSSBItemActionListener? = null

    private val geocodeSearch by lazy {
        GeocodeSearch(context)
    }

    override fun convert(holder: BaseViewHolder, item: DomainSelling.Data?) {
        if (item != null) {
            val binding = ItemSsbSellingBinding.bind(holder.itemView)
            val isQuotable = item.goods_is_bargain
            val isSecondHand = item.goods_is_secondHand

            if (isQuotable && isSecondHand) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNSs, item.goods_name)
            } else if (isQuotable) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNs, item.goods_name)
            } else {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemSs, item.goods_name)
            }

            binding.goodsWatches.text = item.goods_watch_count.toString()
            binding.goodsLocating.visibility = View.VISIBLE
            geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                    binding.goodsLocating.visibility = View.GONE
                    if (p1 == 1000) {
                        val address = p0?.regeocodeAddress
                        binding.goodsLocation.text = address?.formatAddress
                                ?.replace(address.province, "")
                                ?.replace(address.city, "")
                                ?.replace(address.district, "")
                                ?.replace(address.township, "")
                    } else {
                        binding.goodsLocation.text = context.getString(R.string.errorLocation)
                    }
                }

                override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                    // do nothing
                }
            })

            geocodeSearch.getFromLocationAsyn(
                    RegeocodeQuery(LatLonPoint(item.goods_latitude,
                            item.goods_longitude),
                            200f, GeocodeSearch.AMAP))

            ImageUtil.loadRoundedImage(binding.ssbSellingGoodsAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.goods_cover_image))
            binding.ssbSellingGoodsPrice.setPrice(item.goods_price, true)
            holder.itemView.setOnClickListener { GoodsActivity.start(context, item.goods_id, true) }

            if (isMine == true) {
                binding.goodsMoreAction.setOnClickListener { v: View ->
                    // 弹出更多动作的弹窗
                    mOnSSBItemActionListeners?.onItemActionButtonClick(v, item)

                }
            } else {
                binding.goodsMoreAction.visibility = View.GONE
            }
        }
    }

    interface OnSSBItemActionListener {
        fun onItemActionButtonClick(view: View, bean: DomainSelling.Data?)
    }

    fun setOnSSBItemActionListener(listener: OnSSBItemActionListener) {
        mOnSSBItemActionListeners = listener
    }
}