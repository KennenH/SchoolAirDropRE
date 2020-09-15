package com.example.schoolairdroprefactoredition.scene.addnew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;

public class InputSetActivity extends ImmersionStatusBarActivity {

    /**
     * @param context context
     * @param type    类型 one of {@link InputSetActivity#TYPE_TITLE} {@link InputSetActivity#TYPE_DESCRIPTION}
     * @param input   已有输入 方便用户修改而不是全部重写
     * @param title   标题
     */
    public static void start(Context context, int type, String input, String title) {
        Intent intent = new Intent(context, InputSetActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TYPE, type);
        intent.putExtra(CONTENT, input);
        ((AppCompatActivity) context).startActivityForResult(intent, RESULT_CODE);
    }

    public static final int RESULT_CODE = 100;
    public static final String RESULT = "Result";
    public static final String TITLE = "Title";
    public static final String TYPE = "Type";
    public static final String CONTENT = "Content";

    public static final int TYPE_TITLE = 0; // 类型 仅一行
    public static final int TYPE_DESCRIPTION = 1; // 类型 多行

    private static final int MAX_TITLE = 30;
    private static final int MAX_DESCRIPTION = 500;

    private int type;
    private String content;
    private String title = "";

    private TextView mTitle;
    private EditText mInput;
    private TextView mRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_add_set);
        setSupportActionBar(findViewById(R.id.toolbar));

        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        type = intent.getIntExtra(TYPE, TYPE_TITLE);
        content = intent.getStringExtra(CONTENT);

        mTitle = findViewById(R.id.title);
        mInput = findViewById(R.id.input);
        mRemaining = findViewById(R.id.input_tip);

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (type == TYPE_TITLE) {
                    mRemaining.setText(getString(R.string.textRemainCount, MAX_TITLE - mInput.getText().length()));
                    mInput.setMaxLines(2);
                } else
                    mRemaining.setText(getString(R.string.textRemainCount, MAX_DESCRIPTION - mInput.getText().length()));
            }
        });

        mInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(v);
            else
                KeyboardUtils.hideSoftInput(v);
        });

        init();
    }

    private void init() {
        mTitle.setText(title);
        if (type == TYPE_TITLE) {
            mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_TITLE)});
        } else {
            mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DESCRIPTION)});
        }
        mInput.setText(content);
        if (!mInput.hasFocus())
            mInput.requestFocus();
    }

    private void sendData() {
        Intent intent = new Intent();
        intent.putExtra(RESULT, mInput.getText().toString());
        intent.putExtra(TYPE, type);
        setResult(RESULT_OK, intent);
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
            return true;
        } else if (id == R.id.done) {
            sendData();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}