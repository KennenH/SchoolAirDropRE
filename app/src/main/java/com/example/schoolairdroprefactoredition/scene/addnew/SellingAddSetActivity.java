package com.example.schoolairdroprefactoredition.scene.addnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;

public class SellingAddSetActivity extends AppCompatActivity {

    public static void start(Context context, int type, String content) {
        Intent intent = new Intent(context, SellingAddSetActivity.class);
        intent.putExtra("Type", type);
        intent.putExtra("Content", content);
        ((AppCompatActivity) context).startActivityForResult(intent, RESULT_CODE);
    }

    public static final int RESULT_CODE = 100;
    public static final String RESULT = "Result";
    public static final String TYPE = "Type";

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_DESCRIPTION = 1;

    private static final int MAX_TITLE = 30;
    private static final int MAX_DESCRIPTION = 500;

    private int type;
    private String content;

    private EditText mInput;
    private TextView mRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_add_set);
        setSupportActionBar(findViewById(R.id.toolbar));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        type = getIntent().getIntExtra("Type", TYPE_TITLE);
        content = getIntent().getStringExtra("Content");

        mInput = findViewById(R.id.input);
        mRemaining = findViewById(R.id.input_remaining_count);

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (type == TYPE_TITLE)
                    mRemaining.setText(getString(R.string.textRemainCount, MAX_TITLE - mInput.getText().length()));
                else
                    mRemaining.setText(getString(R.string.textRemainCount, MAX_DESCRIPTION - mInput.getText().length()));
            }
        });

        mInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(v);
            else
                KeyboardUtils.hideSoftInput(v);
        });

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