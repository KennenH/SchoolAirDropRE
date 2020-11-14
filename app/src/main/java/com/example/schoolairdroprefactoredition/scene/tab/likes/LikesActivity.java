package com.example.schoolairdroprefactoredition.scene.tab.likes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.databinding.ActivityLikesBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration;

public class LikesActivity extends ImmersionStatusBarActivity {

    private ActivityLikesBinding binding;
    private LikesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLikesBinding.inflate(LayoutInflater.from(this));
        viewModel = new ViewModelProvider(this).get(LikesViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.favoriteRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.favoriteRecycler.addItemDecoration(new MarginItemDecoration(SizeUtils.dp2px(8), false));
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}