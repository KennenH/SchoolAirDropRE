package com.example.schoolairdroprefactoredition.scene.addnew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivityAddNewSuccessBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.why.WhyActivity;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;

public class AddNewResultActivity extends ImmersionStatusBarActivity {

    /**
     * @param isSuccess 是否显示为成功页面
     * @param tip       页面提示 one of {@link AddNewResultActivity.AddNewResultTips}
     */
    public static void start(Context context, boolean isSuccess, @AddNewResultTips int tip) {
        Intent intent = new Intent(context, AddNewResultActivity.class);
        intent.putExtra(IS_SUCCESS, isSuccess);
        intent.putExtra(PAGE_TIP, tip);

        context.startActivity(intent);
        if (context instanceof AppCompatActivity) {
            AnimUtil.activityStartAnimUp((AppCompatActivity) context);
        }
    }

    /**
     * 页面提示标语
     */
    public @interface AddNewResultTips {
        int SUCCESS_NEW_ITEM = R.string.submitSuccessTip;
        int SUCCESS_NEW_POST = R.string.newPostSuccessTip;
        int SUCCESS_MODIFY = R.string.modifySuccessTip;

        int FAILED_ADD = R.string.submitFailedTip;
        int FAILED_MODIFY = R.string.modifyFailedTip;

        int LOCATION_FAILED_NEW_ITEM = R.string.submitItemLocationFailedTip;
        int LOCATION_FAILED_NEW_POST = R.string.submitPostLocationFailedTip;
        int LOCATION_FAILED_MODIFY = R.string.modifyLocationFailedTip;
    }

    private static final String IS_SUCCESS = "?isSuccess";
    private static final String PAGE_TIP = "?pageTip";

    private static final int ICON_SUCCESS = R.drawable.ic_success;
    private static final int ICON_FAILED = R.drawable.ic_failed;

    private static final int TITLE_ADD_NEW_SUCCESS = R.string.submitSuccess;
    private static final int TITLE_ADD_NEW_FAILED = R.string.submitFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.schoolairdroprefactoredition.databinding.ActivityAddNewSuccessBinding binding
                = ActivityAddNewSuccessBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        boolean isSuccess = false;
        int pageTip = AddNewResultTips.FAILED_ADD;

        if (intent != null) {
            pageTip = intent.getIntExtra(PAGE_TIP, pageTip);
            isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);
        }

        if (isSuccess) {
            binding.resultWhy.setVisibility(View.GONE);
            binding.resultTitle.setText(getString(TITLE_ADD_NEW_SUCCESS));
            binding.resultIcon.setImageResource(ICON_SUCCESS);
        } else {
            // TODO: 2020/11/27 为什么出错页面
            binding.resultWhy.setVisibility(View.GONE);
            binding.resultTitle.setText(getString(TITLE_ADD_NEW_FAILED));
            binding.resultIcon.setImageResource(ICON_FAILED);
        }

        binding.resultTip.setText(getString(pageTip));

        binding.resultConfirm.setOnClickListener(v -> {
            finish();
            AnimUtil.activityExitAnimDown(this);
        });

        binding.resultWhy.setOnClickListener(v ->
                WhyActivity.Companion.start(this)
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.activityExitAnimDown(this);
    }
}