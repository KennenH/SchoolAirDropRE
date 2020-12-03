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

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.databinding.FragmentUserEditBinding;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

import java.lang.reflect.InvocationTargetException;

import javadz.beanutils.BeanUtils;

public class UserModifyInfoActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    private String userAvatar;
    private String userName;

    private FragmentUserEditBinding binding;

    private Bundle bundle;

    private DomainToken token;
    private DomainBaseUserInfo info;

    /**
     * @param token  验证信息
     * @param myInfo 我的信息 必须直接包含基本信息的get set方法
     */
    public static void start(Context context, DomainToken token, Object myInfo) {
        if (token == null) return;

        Intent intent = new Intent(context, UserModifyInfoActivity.class);
        DomainBaseUserInfo my = new DomainBaseUserInfo();
        try {
            BeanUtils.copyProperties(my, myInfo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        intent.putExtra(ConstantUtil.KEY_USER_INFO, my);
        intent.putExtra(ConstantUtil.KEY_TOKEN, token);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, UserActivity.REQUEST_UPDATE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentUserEditBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.toolbar));

        binding.userAvatar.setOnClickListener(this);
        binding.userName.setOnClickListener(this);

        userAvatar = getResources().getString(R.string.avatar);
        userName = getResources().getString(R.string.setName);

        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainToken) bundle.getSerializable(ConstantUtil.KEY_TOKEN);
        info = (DomainBaseUserInfo) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
        setUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UserActivity.REQUEST_UPDATE) {
                if (data != null) {
                    info = (DomainBaseUserInfo) data.getSerializableExtra(ConstantUtil.KEY_USER_INFO);
                    bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info);
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
            binding.userAvatar.setIconImage(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
            binding.userName.setDescription(info.getUname());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_avatar) {
            UserUpdateAvatarActivityKt.Companion.start(this, token, info);
        } else if (id == R.id.userName) {
            UserUpdateNameActivity.start(this, token, info);
        }
    }
}