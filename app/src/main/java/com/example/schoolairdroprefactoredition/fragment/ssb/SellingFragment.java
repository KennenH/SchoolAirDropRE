package com.example.schoolairdroprefactoredition.fragment.ssb;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.addnew.SellingAddNewActivity;
import com.example.schoolairdroprefactoredition.model.databean.TestSSBItemBean;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBAdapter;
import com.example.schoolairdroprefactoredition.ui.components.SSBFilter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.util.List;

/**
 * {@link SoldFragment}
 * {@link BoughtFragment}
 */
public class SellingFragment extends Fragment implements SSBFilter.OnFilterListener {
    public static SellingFragment newInstance(String param) {
        SellingFragment fragment = new SellingFragment();

        return fragment;
    }

    private SellingViewModel viewModel;

    private RecyclerView mRecycler;
    private SSBAdapter mAdapter;

    private SSBFilter mFilter;

    private List<TestSSBItemBean> mList;

    private long lastClickTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ssb, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (SystemClock.elapsedRealtime() - lastClickTime < ConstantUtil.MENU_CLICK_GAP)
            return false;
        lastClickTime = SystemClock.elapsedRealtime();

        int id = item.getItemId();
        if (id == R.id.toolbar) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        } else if (id == R.id.ssb_selling_add) {
            if (getActivity() != null) {
                SellingAddNewActivity.start(getContext());
                getActivity().overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding binding
                = com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SellingViewModel.class);

        mRecycler = binding.ssbRecycler;
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mFilter = new SSBFilter(getContext());
        mAdapter = new SSBAdapter();

        mFilter.setOnFilterListener(this);
        mAdapter.addHeaderView(mFilter);
        mRecycler.setAdapter(mAdapter);

        viewModel.getSellingBeans().observe(getViewLifecycleOwner(), data -> {
            mList = data;
            mAdapter.setList(data);
        });

        return binding.getRoot();
    }

    @Override
    public void onFilterTimeAsc() {
        // 将data按时间正序排列
        /**
         * Sort(mList);
         * mAdapter.setList(mList);
         **/
    }

    @Override
    public void onFilterTimeDesc() {
        // 将data按时间倒序排列
    }

    @Override
    public void onFilterWatches() {
        // 将data按浏览量排序
    }
}
