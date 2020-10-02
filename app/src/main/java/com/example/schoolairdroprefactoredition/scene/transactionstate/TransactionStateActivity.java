package com.example.schoolairdroprefactoredition.scene.transactionstate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.BarUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityTransactionStateBinding;
import com.example.schoolairdroprefactoredition.scene.arrangeplace.ArrangePositionActivity;
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.google.zxing.client.android.CaptureActivity;

public class TransactionStateActivity extends AppCompatActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, TransactionStateActivity.class);
        context.startActivity(intent);
    }

    private ActivityTransactionStateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionStateBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        BarUtils.setNavBarLightMode(this, true);
        BarUtils.setNavBarColor(this, getColor(R.color.primary));

        binding.location.setOnClickListener(this);
        binding.incompatible.setOnClickListener(this);
        binding.QRCode.setOnClickListener(this);
        binding.avatar.setOnClickListener(this);
        binding.userName.setOnClickListener(this);
        binding.chat.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ArrangePositionActivity.SELECT_POSITION) {
                if (data != null) {
                    binding.location.setText(data.getStringExtra(ArrangePositionActivity.SELECT_POSITION_KEY));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.location:
                ArrangePositionActivity.startForResult(this);
                break;
            case R.id.incompatible:

                break;
            case R.id.QR_code:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.avatar:

                break;
            case R.id.user_name:

                break;
            case R.id.chat:
                ChatActivity.start(this);
                break;
            default:
                break;
        }
    }
}
