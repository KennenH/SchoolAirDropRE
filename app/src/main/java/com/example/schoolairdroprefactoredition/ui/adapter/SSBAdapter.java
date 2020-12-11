package com.example.schoolairdroprefactoredition.ui.adapter;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ItemSsbSellingBinding;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo;
import com.example.schoolairdroprefactoredition.scene.goods.GoodsActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

import org.jetbrains.annotations.NotNull;

public class SSBAdapter extends BaseQuickAdapter<HomeGoodsListInfo.DataBean, BaseViewHolder> {

    private OnSSBItemActionListener mOnSSBItemActionListener;

    private final boolean isMine;

//    private GeocodeSearch mGeocodeSearch;

    public SSBAdapter(boolean isMine) {
        super(R.layout.item_ssb_selling);
        this.isMine = isMine;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, HomeGoodsListInfo.DataBean bean) {
        if (bean != null) {
            final ItemSsbSellingBinding binding = ItemSsbSellingBinding.bind(holder.itemView);
            final boolean isQuotable = bean.getGoods_is_quotable() == 1;
            final boolean isSecondHand = bean.getGoods_is_brandNew() == 0;

            if (isQuotable && isSecondHand) {
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemNSs, bean.getGoods_name()));
            } else if (isQuotable) {
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemNs, bean.getGoods_name()));
            } else {
                binding.ssbSellingGoodsTitle.setText(getContext().getString(R.string.itemSs, bean.getGoods_name()));
            }

            binding.goodsLocation.setLocationName(getContext().getString(R.string.gettingGoodsLocation));
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

            ImageUtil.loadRoundedImage(binding.ssbSellingGoodsAvatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + bean.getGoods_img_cover());
            binding.ssbSellingGoodsPrice.setPrice(bean.getGoods_price());

            if (getContext() instanceof AppCompatActivity) {
                Bundle bundle = ((AppCompatActivity) getContext()).getIntent().getExtras();
                if (bundle != null) {
                    holder.itemView.setOnClickListener(v ->
                            GoodsActivity.Companion.start(
                                    getContext(),
                                    (DomainToken) bundle.getSerializable(ConstantUtil.KEY_TOKEN),
                                    bundle.getSerializable(ConstantUtil.KEY_USER_INFO),
                                    bean,
                                    true));
                }
            }

            if (!isMine) {
                binding.goodsMoreAction.setVisibility(View.GONE);
            } else {
                binding.goodsMoreAction.setOnClickListener(v -> {
                    // 弹出更多动作的弹窗
                    if (mOnSSBItemActionListener != null) {
                        mOnSSBItemActionListener.onItemActionButtonClick(v, bean);
                    }
                });
            }
        }
    }

    public interface OnSSBItemActionListener {
        void onItemActionButtonClick(View view, HomeGoodsListInfo.DataBean bean);
    }

    public void setOnSSBItemActionListener(OnSSBItemActionListener listener) {
        mOnSSBItemActionListener = listener;
    }
}
