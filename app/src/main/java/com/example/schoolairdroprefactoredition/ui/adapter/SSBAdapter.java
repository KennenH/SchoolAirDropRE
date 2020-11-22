package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemSsbSellingBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

public class SSBAdapter extends BaseQuickAdapter<DomainGoodsInfo.DataBean, BaseViewHolder> {

    private OnSSBItemActionListener mOnSSBItemActionListener;

    private final boolean isMine;

    private GeocodeSearch mGeocodeSearch;

    public SSBAdapter(boolean isMine) {
        super(R.layout.item_ssb_selling);
        this.isMine = isMine;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DomainGoodsInfo.DataBean bean) {
        if (bean != null) {
            final ItemSsbSellingBinding binding = ItemSsbSellingBinding.bind(holder.itemView);
            final boolean isQuotable = bean.getGoods_is_quotable() == 1;
            final boolean isSecondHand = bean.getGoods_is_brandNew() == 0;

            if (isQuotable && isSecondHand)
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemNSs, bean.getGoods_name()));
            else if (isQuotable)
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemNs, bean.getGoods_name()));
            else
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemSs, bean.getGoods_name()));

            binding.goodsLocation.setLocationName(getContext().getString(R.string.gettingGoodsLocation));
            mGeocodeSearch = new GeocodeSearch(getContext());
            mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 1000) {
                        RegeocodeAddress reGeoAddress = regeocodeResult.getRegeocodeAddress();
                        binding.goodsLocation.setLocationName(reGeoAddress.getCity().concat(" ").concat(reGeoAddress.getDistrict()));
                    } else {
                        binding.goodsLocation.setLocationName(getContext().getString(R.string.errorLocation));
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                }
            });
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(Double.parseDouble(bean.getGoods_latitude()),
                    Double.parseDouble(bean.getGoods_longitude())),
                    200, GeocodeSearch.AMAP);
            mGeocodeSearch.getFromLocationAsyn(query);

            ImageUtil.loadRoundedImage(binding.ssbSellingGoodsAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getGoods_img_cover());
            binding.ssbSellingGoodsPrice.setPrice(bean.getGoods_price());

            if (getContext() instanceof AppCompatActivity) {
                Bundle bundle = ((AppCompatActivity) getContext()).getIntent().getExtras();
                if (bundle != null)
                    holder.itemView.setOnClickListener(v -> GoodsActivity.Companion.start(getContext(),
                            (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE),
                            bean, true));
            }

            if (!isMine) {
                binding.goodsMoreAction.setVisibility(View.GONE);
            } else {
                binding.goodsMoreAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 弹出更多动作的弹窗
                        if (mOnSSBItemActionListener != null)
                            mOnSSBItemActionListener.onItemActionButtonClick(v, bean);
                    }
                });
            }
        }
    }

    public interface OnSSBItemActionListener {
        void onItemActionButtonClick(View view, DomainGoodsInfo.DataBean bean);
    }

    public void setOnSSBItemActionListener(OnSSBItemActionListener listener) {
        mOnSSBItemActionListener = listener;
    }
}
