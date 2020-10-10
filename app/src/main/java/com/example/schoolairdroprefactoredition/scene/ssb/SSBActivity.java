package com.example.schoolairdroprefactoredition.scene.ssb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.ActivitySsbBinding;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.main.MainActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SSBBaseFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter;

public class SSBActivity extends ImmersionStatusBarActivity implements View.OnClickListener, SSBPagerAdapter.OnSSBDataLenChangedIntermediaryListener {

    public static final int SELLING = R.string.selling;
    public static final int SOLD = R.string.sold;
    public static final int BOUGHT = R.string.purchased;

    public static final String PAGE_INDEX = "SSBPageIndex";

    private OnLoginStateChangeListener mOnLoginStateChangeListener;

    private OnChangedToSellingListener mOnChangedToSellingListener;
    private OnChangedToSoldListener mOnChangedToSoldListener;
    private OnChangedToBoughtListener mOnChangedToBoughtListener;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SSBActivity.class);
        intent.putExtras(bundle);

        if (context instanceof AppCompatActivity)
            ((MainActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
        else
            context.startActivity(intent);
    }

    private ActivitySsbBinding binding;
    private SSBPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySsbBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(findViewById(R.id.toolbar));

        binding.ssbSearch.setOnClickListener(this);
        binding.ssbTitle.setOnClickListener(this);

        Intent intent = getIntent();
        int index = intent.getIntExtra(PAGE_INDEX, 0);
        mPagerAdapter = new SSBPagerAdapter(this,getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPagerAdapter.setOnSSBDataLenChangedIntermediaryListener(this);
        binding.ssbSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(v);
            else
                KeyboardUtils.hideSoftInput(v);
        });
        binding.ssbPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case SSBBaseFragment.SELLING_POS:
                        if (mOnChangedToSellingListener != null)
                            mOnChangedToSellingListener.OnChangedToSelling();
                        binding.ssbTitle.setText(SELLING);
                        break;
                    case SSBBaseFragment.SOLD_POS:
                        if (mOnChangedToSoldListener != null)
                            mOnChangedToSoldListener.onChangedToSold();
                        binding.ssbTitle.setText(SOLD);
                        break;
                    case SSBBaseFragment.BOUGHT_POS:
                        if (mOnChangedToBoughtListener != null)
                            mOnChangedToBoughtListener.onChangedToBought();
                        binding.ssbTitle.setText(BOUGHT);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.ssbPager.setOffscreenPageLimit(2);
        binding.ssbPager.setAdapter(mPagerAdapter);
        binding.ssbPager.post(() -> binding.ssbPager.setCurrentItem(index));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                if (data != null) {
                    getIntent().putExtras(data);
                    if (mOnLoginStateChangeListener != null)
                        mOnLoginStateChangeListener.onLoginSSB();

                    setResult(Activity.RESULT_OK, data);
                }
            }
        }
    }

    public int getCurrentItem() {
        return binding.ssbPager.getCurrentItem();
    }

    @Override
    public void onSSBDataLenChangedIntermediary(int total) {
        binding.ssbFilter.setDataStatistics(total);
    }

    /**
     * 页面切换至Selling
     */
    public interface OnChangedToSellingListener {
        /**
         * 通知子fragment当前在哪个页面，以便对应的页面往activity发送数据，便于activity更新ui
         */
        void OnChangedToSelling();
    }

    public void setOnChangedToSellingListener(OnChangedToSellingListener listener) {
        mOnChangedToSellingListener = listener;
    }

    /**
     * 页面切换至Sold
     */
    public interface OnChangedToSoldListener {
        void onChangedToSold();
    }

    public void setOnChangedToSoldListener(OnChangedToSoldListener listener) {
        mOnChangedToSoldListener = listener;
    }

    /**
     * 页面切换至Bought
     */
    public interface OnChangedToBoughtListener {
        void onChangedToBought();
    }

    public void setOnChangedToBoughtListener(OnChangedToBoughtListener listener) {
        mOnChangedToBoughtListener = listener;
    }


    /**
     * 在{@link com.example.schoolairdroprefactoredition.scene.addnew.SellingAddNewActivity}中登录后
     * 将登录结果回调至ssb三个子fragment中
     */
    public interface OnLoginStateChangeListener {
        void onLoginSSB();
    }

    public void setOnLoginStateChangeListener(OnLoginStateChangeListener listener) {
        mOnLoginStateChangeListener = listener;
    }

    public void hideSearchBar() {
        binding.ssbTitle.requestFocus();
        binding.ssbSearch.clearFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int now = binding.ssbPager.getCurrentItem();
        final MenuItem add = menu.findItem(R.id.ssb_selling_add);
        if (add != null) {
            if (now == 0)
                add.setVisible(true);
            else
                add.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ssb_search) {
            // open search page
        }
    }
}