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
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo;
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewItemActivity;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.scene.ssb.fragment.SSBBaseFragment;
import com.example.schoolairdroprefactoredition.ui.adapter.SSBPagerAdapter;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.lang.reflect.InvocationTargetException;

import javadz.beanutils.BeanUtils;

public class SSBActivity extends ImmersionStatusBarActivity implements View.OnClickListener, SSBPagerAdapter.OnSSBDataLenChangedIntermediaryListener {

    public static final int SELLING = R.string.selling;
    public static final int SOLD = R.string.sold;
    public static final int BOUGHT = R.string.purchased;

    public static final String PAGE_INDEX = "SSBPage?Index";

    private OnLoginStateChangeListener mOnLoginStateChangeListener;
    private OnChangedToSellingListener mOnChangedToSellingListener;
    private OnChangedToSoldListener mOnChangedToSoldListener;
    private OnChangedToBoughtListener mOnChangedToBoughtListener;

    /**
     * 三种页面进入方式
     * 1、未登录 没有token
     * 2、登录查看自己的页面 有token
     * 3、登录查看他人的页面 有token
     */
    public static void start(Context context, DomainAuthorize token, Object info, int page, boolean isMine) {
        if (info == null) return;
        DomainBaseUserInfo userInfo = new DomainBaseUserInfo();
        try {
            BeanUtils.copyProperties(userInfo, info);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, SSBActivity.class);
        intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token);
        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo);
        intent.putExtra(ConstantUtil.KEY_IS_MINE, isMine);
        intent.putExtra(PAGE_INDEX, page);

        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
        else
            context.startActivity(intent);
    }

    private ActivitySsbBinding binding;
    private SSBPagerAdapter mPagerAdapter;
    private boolean isMine = false;

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
        isMine = intent.getBooleanExtra(ConstantUtil.KEY_IS_MINE, isMine);
        int index = intent.getIntExtra(PAGE_INDEX, 0);
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

        mPagerAdapter = new SSBPagerAdapter(this,
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, isMine);
        mPagerAdapter.setOnSSBDataLenChangedIntermediaryListener(this);

        binding.ssbPager.setOffscreenPageLimit(2);
        binding.ssbPager.setAdapter(mPagerAdapter);
        binding.ssbPager.setCurrentItem(index);
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
     * 在{@link AddNewItemActivity}中登录后
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOnLoginStateChangeListener = null;
        mOnChangedToBoughtListener = null;
        mOnChangedToSellingListener = null;
        mOnChangedToSoldListener = null;
    }
}