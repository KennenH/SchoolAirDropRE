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
import com.example.schoolairdroprefactoredition.application.Application;
import com.example.schoolairdroprefactoredition.databinding.FragmentUserEditBinding;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;

public class UserModifyInfoActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    private FragmentUserEditBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserModifyInfoActivity.class);
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

        setUserInfo(((Application) getApplication()).getCachedMyInfo());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UserActivity.REQUEST_UPDATE) {
                if (data != null) {
                    DomainUserInfo.DataBean newInfo = (DomainUserInfo.DataBean) data.getSerializableExtra(ConstantUtil.KEY_USER_INFO);
                    getIntent().putExtra(ConstantUtil.KEY_USER_INFO, newInfo);
                    setUserInfo(newInfo);
                    setResult(Activity.RESULT_OK, data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 使用info填充ui，在用户第一次打开页面时和用户修改信息后回到页面时调用
     */
    private void setUserInfo(DomainUserInfo.DataBean info) {
        if (info != null) {
            binding.userAvatar.setIconImage(ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(info.getUserAvatar()));
            binding.userName.setDescription(info.getUserName());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_avatar) {
            UserUpdateAvatarActivity.Companion.start(this);
        } else if (id == R.id.userName) {
            UserUpdateNameActivity.Companion.start(this);
        }
    }
}