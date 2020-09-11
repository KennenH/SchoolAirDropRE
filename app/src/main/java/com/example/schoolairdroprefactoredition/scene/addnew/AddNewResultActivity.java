package com.example.schoolairdroprefactoredition.scene.addnew;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.MyUtil;

public class AddNewResultActivity extends ImmersionStatusBarActivity {
    public static final String IS_SUCCESS = "?isSuccess";

    public static final int ICON_SUCCESS = R.drawable.ic_success;
    public static final int ICON_FAILED = R.drawable.ic_failed;

    public static final int TITLE_SUCCESS = R.string.submitSuccess;
    public static final int TITLE_FAILED = R.string.submitFailed;

    public static final int TIP_SUCCESS = R.string.submitSuccessTip;
    public static final int TIP_FAILED = R.string.submitFailedTip;

    public static void start(Context context, boolean success) {
        Intent intent = new Intent(context, AddNewResultActivity.class);
        intent.putExtra(IS_SUCCESS, success);
        context.startActivity(intent);
        if (context instanceof AddNewResultActivity)
            MyUtil.startAnimUp((AddNewResultActivity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_success);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        boolean isSuccess = false;
        if (intent != null)
            isSuccess = intent.getBooleanExtra(IS_SUCCESS, false);

        if (isSuccess) {
            ((ImageView) findViewById(R.id.result_icon)).setImageResource(ICON_SUCCESS);
            ((TextView) findViewById(R.id.result_title)).setText(TITLE_SUCCESS);
            ((TextView) findViewById(R.id.result_tip)).setText(TIP_SUCCESS);
        } else {
            ((ImageView) findViewById(R.id.result_icon)).setImageResource(ICON_FAILED);
            ((TextView) findViewById(R.id.result_title)).setText(TITLE_FAILED);
            ((TextView) findViewById(R.id.result_tip)).setText(TIP_FAILED);
        }

        findViewById(R.id.result_confirm).setOnClickListener((v) -> {
            finish();
            MyUtil.exitAnimDown(this);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyUtil.exitAnimDown(this);
    }
}