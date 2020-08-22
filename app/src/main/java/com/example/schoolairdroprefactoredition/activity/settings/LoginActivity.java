package com.example.schoolairdroprefactoredition.activity.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.MyUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final int REQUEST_LOGIN = 2143;

    public static void startForResult(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.startActivityForResult(intent, REQUEST_LOGIN);
            activity.overridePendingTransition(R.anim.enter_y_fragment, R.anim.alpha_out);
        }
    }

    private CheckBox mCheck;
    private TextView mLogin;
    private TextView mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        mCheck = findViewById(R.id.checkbox);
        mLogin = findViewById(R.id.login_with_alipay);
        mCancel = findViewById(R.id.cancel);

        mLogin.setEnabled(false);
        mCancel.setOnClickListener(this);
        mCheck.setOnCheckedChangeListener(this);
        mCheck.setSelected(false);

        mLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
        overrideAnimation();
    }

    private void overrideAnimation() {
        overridePendingTransition(0, R.anim.popexit_y_fragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_with_alipay) {
            setResult(Activity.RESULT_OK);
            finish();
            overrideAnimation();
        } else if (id == R.id.cancel) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            overrideAnimation();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mLogin.setEnabled(isChecked);
    }
}