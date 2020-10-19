package com.example.schoolairdroprefactoredition.scene.addnew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityAddNewSuccessBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;

public class AddNewResultActivity extends ImmersionStatusBarActivity {

    public static final String IS_SUCCESS = "?isSuccess";
    public static final String PAGE_TIP = "?pageTip";

    public static final int ICON_SUCCESS = R.drawable.ic_success;
    public static final int ICON_FAILED = R.drawable.ic_failed;

    public static final int TITLE_ADD_NEW_SUCCESS = R.string.submitSuccess;
    public static final int TITLE_ADD_NEW_FAILED = R.string.submitFailed;

    public static final int TIP_ADD_NEW_SUCCESS = R.string.submitSuccessTip;
    public static final int TIP_MODIFY_SUCCESS = R.string.modifySuccessTip;
    public static final int TIP_ADD_NEW_FAILED = R.string.submitFailedTip;
    public static final int TIP_MODIFY_FAILED = R.string.modifyFailedTip;
    public static final int TIP_ADD_NEW_LOCATION_FAILED = R.string.submitLocationFailedTip;
    public static final int TIP_MODIFY_LOCATION_FAILED = R.string.modifyLocationFailedTip;

    public static void start(Context context, boolean isSuccess, int tip) {
        Intent intent = new Intent(context, AddNewResultActivity.class);
        intent.putExtra(IS_SUCCESS, isSuccess);
        intent.putExtra(PAGE_TIP, tip);
        context.startActivity(intent);
        if (context instanceof AppCompatActivity)
            AnimUtil.activityStartAnimUp((AppCompatActivity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.schoolairdroprefactoredition.databinding.ActivityAddNewSuccessBinding binding
                = ActivityAddNewSuccessBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        boolean isSuccess = false;
        int pageTip = TIP_ADD_NEW_FAILED;
        if (intent != null) {
            pageTip = intent.getIntExtra(PAGE_TIP, pageTip);
            isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);
        }

        if (isSuccess) {
            binding.resultTitle.setText(getString(TITLE_ADD_NEW_SUCCESS));
            binding.resultIcon.setImageResource(ICON_SUCCESS);
        } else {
            binding.resultTitle.setText(getString(TITLE_ADD_NEW_FAILED));
            binding.resultIcon.setImageResource(ICON_FAILED);
        }

        binding.resultTip.setText(getString(pageTip));

        binding.resultConfirm.setOnClickListener((v) -> {
            finish();
            AnimUtil.activityExitAnimDown(this);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.activityExitAnimDown(this);
    }
}