package com.example.schoolairdroprefactoredition.activity.transactionstate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.chat.ChatActivity;
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class TransactionStateActivity extends AppCompatActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, TransactionStateActivity.class);
        context.startActivity(intent);
    }

    private TextView mState;
    private TextView mRemaining;
    private StateProgressBar mProgress;
    private TextView mTip;
    private TextView mIncompatible;
    private TextView mQRCode;
    private SimpleDraweeView mAvatar;
    private TextView mUserName;
    private ImageView mChat;
    private ConstraintLayout mSellerWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_state);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        mState = findViewById(R.id.state);
        mRemaining = findViewById(R.id.remaining);
        mProgress = findViewById(R.id.state_progress_bar);
        mTip = findViewById(R.id.tip);
        mIncompatible = findViewById(R.id.incompatible);
        mQRCode = findViewById(R.id.QR_code);
        mAvatar = findViewById(R.id.avatar);
        mUserName = findViewById(R.id.user_name);
        mChat = findViewById(R.id.chat);
        mSellerWarning = findViewById(R.id.seller_warning);

        mIncompatible.setOnClickListener(this);
        mQRCode.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mChat.setOnClickListener(this);
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
            case R.id.incompatible:

                break;
            case R.id.QR_code:

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
