package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemSsbSellingBinding
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity.Companion.start
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class SSBAdapter(private val isMine: Boolean?) : BaseQuickAdapter<DomainPurchasing.DataBean?, BaseViewHolder>(R.layout.item_ssb_selling) {

    private val mOnSSBItemActionListeners = ArrayList<OnSSBItemActionListener>()

    private val geocodeSearch by lazy {
        GeocodeSearch(context)
    }

    override fun convert(holder: BaseViewHolder, item: DomainPurchasing.DataBean?) {
        if (item != null) {
            val binding = ItemSsbSellingBinding.bind(holder.itemView)
            val isQuotable = item.isGoods_is_bargain
            val isSecondHand = item.isGoods_is_secondHande

            if (isQuotable && isSecondHand) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNSs, item.goods_name)
            } else if (isQuotable) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNs, item.goods_name)
            } else {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemSs, item.goods_name)
            }

            if (item.latitude != null && item.longitude != null) {
                binding.goodsLocating.visibility = View.VISIBLE
                geocodeSearch.apply {
                    setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                        override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                            binding.goodsLocating.visibility = View.GONE
                            if (p1 == 1000) {
                                val reGeoAddress = p0?.regeocodeAddress
                                binding.goodsLocation.text = "${reGeoAddress?.district}${reGeoAddress?.roads}${reGeoAddress?.streetNumber}"
                            } else {
                                binding.goodsLocation.text = context.getString(R.string.errorLocation)
                            }
                        }

                        override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                            // do nothing
                        }
                    })

                    getFromLocationAsyn(
                            RegeocodeQuery(LatLonPoint(item.latitude.toDouble(),
                                    item.longitude.toDouble()),
                                    200f, GeocodeSearch.AMAP))
                }
            } else {
                binding.goodsLocation.text = context.getString(R.string.errorLocation)
                binding.goodsLocating.visibility = View.GONE
            }

            ImageUtil.loadRoundedImage(binding.ssbSellingGoodsAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.goods_cover_image))
            binding.ssbSellingGoodsPrice.setPrice(item.goods_price)

            if (context is AppCompatActivity) {
                holder.itemView.setOnClickListener { start(context, item, true) }
            }

            if (isMine == true) {
                binding.goodsMoreAction.setOnClickListener { v: View ->
                    // 弹出更多动作的弹窗
                    for (listener in mOnSSBItemActionListeners) {
                        listener.onItemActionButtonClick(v, item)
                    }
                }
            } else {
                binding.goodsMoreAction.visibility = View.GONE
            }
        }
    }

    interface OnSSBItemActionListener {
        fun onItemActionButtonClick(view: View, bean: DomainPurchasing.DataBean?)
    }

    fun addOnSSBItemActionListener(listener: OnSSBItemActionListener) {
        mOnSSBItemActionListeners.add(listener)
    }

    fun removeOnSSBItemActionListener(listener: OnSSBItemActionListener) {
        mOnSSBItemActionListeners.remove(listener)
    }
}