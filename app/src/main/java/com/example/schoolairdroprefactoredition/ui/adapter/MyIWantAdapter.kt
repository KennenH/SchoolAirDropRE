package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ItemSsbIwantBinding
import com.example.schoolairdroprefactoredition.domain.DomainIWant


/**
 * @author kennen
 * @date 2021/4/28
 */
class MyIWantAdapter(private val isMine: Boolean?) : BaseQuickAdapter<DomainIWant.Data?, BaseViewHolder>(R.layout.item_ssb_iwant) {

    private var mOnSSBIWantItemActionListener: OnSSBIWantItemActionListener? = null

    private val geocodeSearch by lazy {
        GeocodeSearch(context)
    }

    override fun convert(holder: BaseViewHolder, item: DomainIWant.Data?) {
        if (item != null) {
            val binding = ItemSsbIwantBinding.bind(holder.itemView)
            binding.itemSsbIwantContent.text = item.iwant_content
            item.iwant_images.split(",").let {
                if (it.isEmpty()) {
                    binding.itemSsbIwantHasImage.visibility = View.GONE
                } else {
                    binding.itemSsbIwantImageNum.text = "${it.size}"
                }
            }
            binding.itemSsbIwantTag.text = context.getString(R.string.iwant_tag, item.tag)
            geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
//                    binding.goodsLocating.visibility = View.GONE
                    if (p1 == 1000) {
                        val address = p0?.regeocodeAddress
                        binding.itemSsbIwantLocation.text = address?.formatAddress
                                ?.replace(address.province, "")
                                ?.replace(address.city, "")
                                ?.replace(address.district, "")
                                ?.replace(address.township, "")
                    } else {
                        binding.itemSsbIwantLocation.text = context.getString(R.string.errorLocation)
                    }
                }

                override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                    // do nothing
                }
            })

            if (isMine == true) {
                binding.itemSsbIwantAction.setOnClickListener {
                    mOnSSBIWantItemActionListener?.onItemActionButtonClick(it, item)
                }
            } else {
                binding.itemSsbIwantAction.visibility = View.GONE
            }

//            geocodeSearch.getFromLocationAsyn(
//                    RegeocodeQuery(LatLonPoint(item.goods_latitude,
//                            item.goods_longitude),
//                            200f, GeocodeSearch.AMAP))
//
            val resources = context.resources
            val theme = context.theme
            val blackText = resources.getColor(R.color.black, theme)
            val blackAlwaysText = resources.getColor(R.color.blackAlways, theme)
            val whiteAlwaysText = resources.getColor(R.color.whiteAlways, theme)
            binding.apply {
                when (item.iwant_color) {
                    IWantRecyclerAdapter.COLOR_WARNING -> {
                        itemSsbIwantContent.setTextColor(blackAlwaysText)
                        itemSsbIwantContentWrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_yellow, theme)
                    }
                    IWantRecyclerAdapter.COLOR_HEART -> {
                        itemSsbIwantContent.setTextColor(whiteAlwaysText)
                        itemSsbIwantContentWrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_red, theme)
                    }
                    IWantRecyclerAdapter.COLOR_THEME -> {
                        itemSsbIwantContent.setTextColor(whiteAlwaysText)
                        itemSsbIwantContentWrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_theme, theme)
                    }
                    IWantRecyclerAdapter.COLOR_PURPLE -> {
                        itemSsbIwantContent.setTextColor(whiteAlwaysText)
                        itemSsbIwantContentWrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_purple, theme)
                    }
                    else -> {
                        itemSsbIwantContent.setTextColor(blackText)
                        itemSsbIwantContentWrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded, theme)
                    }
                }
            }
        }
    }

    interface OnSSBIWantItemActionListener {
        fun onItemActionButtonClick(view: View, bean: DomainIWant.Data?)
    }

    fun setOnSSBIWantItemActionListener(listener: OnSSBIWantItemActionListener) {
        mOnSSBIWantItemActionListener = listener
    }
}