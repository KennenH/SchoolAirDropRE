package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemSsbSellingBinding
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity.Companion.start
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class SSBAdapter(private val isMine: Boolean) : BaseQuickAdapter<DomainPurchasing.DataBean?, BaseViewHolder>(R.layout.item_ssb_selling) {

    private var mOnSSBItemActionListeners = ArrayList<OnSSBItemActionListener>()

    override fun convert(holder: BaseViewHolder, bean: DomainPurchasing.DataBean?) {
        if (bean != null) {
            val binding = ItemSsbSellingBinding.bind(holder.itemView)
            val goodsType = bean.goods_type.split(',')
            val isQuotable = goodsType.contains(ConstantUtil.GOODS_TYPE_BARGAIN)
            val isSecondHand = goodsType.contains(ConstantUtil.GOODS_TYPE_SECONDHAND)

            if (isQuotable && isSecondHand) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNSs, bean.goods_name)
            } else if (isQuotable) {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemNs, bean.goods_name)
            } else {
                binding.ssbSellingGoodsTitle.text = context.getString(R.string.itemSs, bean.goods_name)
            }

            binding.goodsLocation.setLocationName(context.getString(R.string.gettingGoodsLocation))
            //            mGeocodeSearch = new GeocodeSearch(getContext());
//            mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//                @Override
//                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//                    if (i == 1000) {
//                        RegeocodeAddress reGeoAddress = regeocodeResult.getRegeocodeAddress();
//                        binding.goodsLocation.setLocationName(reGeoAddress.getCity().concat(" ").concat(reGeoAddress.getDistrict()));
//                    } else {
//                        binding.goodsLocation.setLocationName(getContext().getString(R.string.errorLocation));
//                    }
//                }
//
//                @Override
//                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//                }
//            });
//
//            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(Double.parseDouble(bean.getGoods_latitude()),
//                    Double.parseDouble(bean.getGoods_longitude())),
//                    200, GeocodeSearch.AMAP);
//            mGeocodeSearch.getFromLocationAsyn(query);

            ImageUtil.loadRoundedImage(binding.ssbSellingGoodsAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ImageUtil.fixUrl(bean.goods_cover_image))
            binding.ssbSellingGoodsPrice.setPrice(bean.goods_price)

            if (context is AppCompatActivity) {
                holder.itemView.setOnClickListener { start(context, bean, true) }
            }

            if (!isMine) {
                binding.goodsMoreAction.visibility = View.GONE
            } else {
                binding.goodsMoreAction.setOnClickListener { v: View? ->
                    // 弹出更多动作的弹窗

                    for (listener in mOnSSBItemActionListeners) {
                        listener.onItemActionButtonClick(v, bean)
                    }
                }
            }
        }
    }

    interface OnSSBItemActionListener {
        fun onItemActionButtonClick(view: View?, bean: DomainPurchasing.DataBean?)
    }

    fun addOnSSBItemActionListener(listener: OnSSBItemActionListener) {
        mOnSSBItemActionListeners.add(listener)
    }
}