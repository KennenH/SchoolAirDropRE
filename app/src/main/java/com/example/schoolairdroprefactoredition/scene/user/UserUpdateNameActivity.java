package com.example.schoolairdroprefactoredition.scene.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

public class UserUpdateNameActivity extends ImmersionStatusBarActivity implements View.OnClickListener {

    public static void startForResult(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UserUpdateNameActivity.class);
        intent.putExtras(bundle);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, UserActivity.REQUEST_UPDATE);
        }
    }

    private ResultViewModel viewModel;

    private EditText mInput;
    private TextView mTitle;
    private TextView mTip;

    private String name;

    private Bundle bundle;

    private DomainAuthorize token;
    private DomainUserInfo.DataBean info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_add_set);
        viewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        setSupportActionBar(findViewById(R.id.toolbar));

        mTip = findViewById(R.id.input_tip);
        mTitle = findViewById(R.id.title);
        mInput = findViewById(R.id.input);
        mInput.setMaxLines(1);
        mInput.setOnClickListener(this);
        mInput.requestFocus();
        KeyboardUtils.showSoftInput();

        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        info = (DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.toolbar_done, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.done) {
            rename();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mTitle.setText(R.string.setName);

        if (bundle == null)
            bundle = new Bundle();

        token = (DomainAuthorize) bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE);
        info = ((DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO));
        if (info != null) name = info.getUname();
        mInput.setText(name);
        mInput.setSelection(name.length());
    }

    /**
     * 将数据回到上一个Activity
     */
    private void sendData() {
        info.setUname(mInput.getText().toString().trim());
        bundle.putSerializable(ConstantUtil.KEY_USER_INFO, info);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 网络请求修改名字
     */
    private void rename() {
        KeyboardUtils.hideSoftInput(this);

        if (name.equals(mInput.getText().toString().trim()))
            finish();

        if (token != null && token.getAccess_token() != null) {
            viewModel.rename(token.getAccess_token(), mInput.getText().toString().trim())
                    .observe(this, data -> {
                        if (data) {
                            sendData();
                        } else {
                            Toast.makeText(this, R.string.errorUpdatingName, Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input) {
            if (!mInput.hasFocus())
                mInput.requestFocus();
        }
    }

}