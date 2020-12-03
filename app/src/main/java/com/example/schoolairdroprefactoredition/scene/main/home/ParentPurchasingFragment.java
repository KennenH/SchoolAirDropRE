package com.example.schoolairdroprefactoredition.scene.main.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseParentFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class ParentPurchasingFragment extends BaseParentFragment implements View.OnClickListener {

    public static ParentPurchasingFragment newInstance() {
        return new ParentPurchasingFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).autoLogin();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), HomePagerAdapter.PAGE_TYPE_PURCHASING);
        setUpPlaceHolderHAndContainerView(binding.placeholder, binding.homeViewpager);

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), binding.homeViewpager, HomeNavigatorAdapter.PURCHASING);
        commonNavigator.setAdapter(adapter);
        binding.homeViewpager.setAdapter(homePagerAdapter);
        binding.homeIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.homeIndicator, binding.homeViewpager);

        binding.homeSearchBar.setOnClickListener(this);
        binding.homeTopAdd.setOnClickListener(this);
        binding.homeTopAdd.setText(R.string.addNewSelling);
        binding.homeTopAdd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_logo_text_alpha_20, 0, 0, 0);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null) {
                mOnSearchBarClickedListener.onSearchBarClicked();
            }
        } else if (id == R.id.home_top_add) {
            onHomePostMyItem(v);
        }
    }
}
