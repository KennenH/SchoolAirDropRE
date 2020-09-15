package com.example.schoolairdroprefactoredition.scene.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserModifyInfoActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    private String userAvatar;
    private String userName;
    private String userSex;
    private String female;
    private String male;
    private String hermaphrodite;

    private PageItem mAvatar;
    private PageItem mName;
    private PageItem mSex;

    private BottomSheetDialog dialog;

    private Bundle bundle;

    private DomainAuthorize token;
    private DomainUserInfo.DataBean info;

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserModifyInfoActivity.class);
        intent.putExtras(bundle);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, UserActivity.REQUEST_UPDATE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_edit);
        setSupportActionBar(findViewById(R.id.toolbar));

        mAvatar = findViewById(R.id.user_avatar);
        mName = findViewById(R.id.user_name);
        mSex = findViewById(R.id.user_sex);

        mAvatar.setOnClickListener(this);
        mName.setOnClickListener(this);
        mSex.setOnClickListener(this);

        userAvatar = getResources().getString(R.string.avatar);
        userName = getResources().getString(R.string.setName);
        userSex = getResources().getString(R.string.sex);
        female = getResources().getString(R.string.female);
        male = getResources().getString(R.string.male);
        hermaphrodite = getResources().getString(R.string.hermaphrodite);

        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        setUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UserActivity.REQUEST_UPDATE) {
                if (data != null) {
                    info = (DomainUserInfo.DataBean) data.getSerializableExtra(ConstantUtil.KEY_USER_INFO);
                    setUserInfo();
                    setResult(Activity.RESULT_OK, data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /**
     * 使用info填充ui，在用户第一次打开页面时和用户修改信息后回到页面时调用
     */
    private void setUserInfo() {
        if (info != null) {
            mAvatar.setIconImage(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
            mName.setDescription(info.getUname());
            String gender = info.getUgender();
            if (gender.equals("m"))
                mSex.setDescription(male);
            else if (gender.equals("f"))
                mSex.setDescription(female);
            else
                mSex.setDescription(hermaphrodite);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_avatar) {
            UserUpdateAvatarActivity.startForResult(this, bundle);
        } else if (id == R.id.user_name) {
            UserUpdateNameActivity.startForResult(this, bundle);
        } else if (id == R.id.user_sex) {
            showSexDialog();
        }
    }

    private void showSexDialog() {
        if (dialog == null) {
            dialog = new BottomSheetDialog(this);
            dialog.setContentView(LayoutInflater.from(this).inflate(R.layout.sheet_sex, null));

            try {
                dialog.findViewById(R.id.female).setOnClickListener(v -> {
                    mSex.setDescription(female);
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.male).setOnClickListener(v -> {
                    mSex.setDescription(male);
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.hermaphrodite).setOnClickListener(v -> {
                    mSex.setDescription(hermaphrodite);
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(v -> {
                    dialog.dismiss();
                });

                View view1 = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
                view1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, this.getTheme()));
                final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
                bottomSheetBehavior.setSkipCollapsed(true);
                bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dialog.dismiss();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            } catch (NullPointerException ignored) {
            }
        }
        dialog.show();
    }
}