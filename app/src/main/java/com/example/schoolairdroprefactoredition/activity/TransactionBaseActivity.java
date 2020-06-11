package com.example.schoolairdroprefactoredition.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;

public class TransactionBaseActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    protected TextView mName1;
    protected TextView mName2;
    private int lastStack = 0;

    private boolean flag = true;// 用于标识需要进行变换的标题
    private boolean actionLock = true;// false 时为锁定,true 为释放
    private String lastName;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        mName1 = findViewById(R.id.old);
        mName2 = findViewById(R.id.newcomer);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (actionLock) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (actionLock && id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
            return true;
        } else
            return false;
    }

    @Override
    public void onBackStackChanged() {
        actionLock = false;

        TextView alpha = flag ? mName1 : mName2;
        TextView translation = mName2 == alpha ? mName1 : mName2;

        int nowStack = getSupportFragmentManager().getBackStackEntryCount();

        String now = getSupportFragmentManager().findFragmentById(R.id.container).getTag();

        if (lastStack < nowStack) { // 开启新的fragment
            translation.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.enter_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);
        } else { // 关闭fragment
            flag = !flag;
            translation.setText(lastName);
            alpha.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.exit_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);

        }

        flag = !flag;// 交换职责
        lastStack = nowStack;
        lastName = now;
    }
}
