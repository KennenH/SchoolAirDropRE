package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.MainActivity;
import com.example.schoolairdroprefactoredition.activity.map.AMapActivity;
import com.example.schoolairdroprefactoredition.databinding.FragmentHomeBinding;
import com.example.schoolairdroprefactoredition.ui.adapter.HomeNavigatorAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePagerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.Location;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class ParentPurchasingFragment extends BaseParentFragment implements View.OnClickListener, MainActivity.OnLocationListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setOnLocationListener(ParentPurchasingFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, HomePagerAdapter.PURCHASING);
        setUpPlaceHolderHAndGoodsContainer(binding.placeholder, binding.homeViewpager);
        MagicIndicator indicator = binding.homeIndicator;
        ViewPager viewPager = binding.homeViewpager;
        Location mLocation = binding.homeLocation;

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        HomeNavigatorAdapter adapter = new HomeNavigatorAdapter(getContext(), viewPager, HomeNavigatorAdapter.PURCHASING);
        commonNavigator.setAdapter(adapter);
        viewPager.setAdapter(homePagerAdapter);
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);

        binding.homeSearchBar.setOnClickListener(this);
        mLocation.setOnClickListener(this);

        return binding.getRoot();
    }

    /**
     * {@link BaseParentFragment#mOnLocationCallbackListener}
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_search_bar) {
            if (mOnSearchBarClickedListener != null) {
                mOnSearchBarClickedListener.onSearchBarClicked();
            }
        } else if (id == R.id.home_location) {
            AMapActivity.startForResult(getContext());
        }
    }
}
