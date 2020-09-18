package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;

public class SearchBar extends ConstraintLayout implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener {

    private OnSearchActionListener mOnSearchActionListener;

    private ImageView mClear;
    private EditText mSearch;

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_search_bar, this, true);

        mClear = findViewById(R.id.search_clear);
        mSearch = findViewById(R.id.search_bar);

        mClear.setVisibility(GONE);
        mClear.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSearch.addTextChangedListener(this);
        mSearch.setOnEditorActionListener(this);
        mSearch.setOnFocusChangeListener(this);
    }

    /**
     * 设置当前输入
     *
     * @param key 搜索关键词
     */
    public void setInputKey(String key) {
        mSearch.setText(key);
    }

    public void openSearch() {
        mSearch.requestFocus();
    }

    public void closeSearch() {
        mSearch.clearFocus();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (mOnSearchActionListener != null) {
            mOnSearchActionListener.onSearch(mSearch.getText().toString().trim(), v);
        }
        return true;
    }

    public interface OnSearchActionListener {
        /**
         * 按关键字搜索
         *
         * @param key 关键字
         * @param v   search bar
         */
        void onSearch(String key, View v);

        /**
         * 输入改变
         *
         * @param input 当前输入
         */
        void onInputChanged(String input);

        /**
         * 搜索框焦点改变
         *
         * @param hasFocus 是否有焦点
         */
        void onFocusChanged(View view, boolean hasFocus);
    }

    public void setOnSearchActionListener(OnSearchActionListener onSearchActionListener) {
        this.mOnSearchActionListener = onSearchActionListener;
    }

    private void hideKeyboard(View view) {
        KeyboardUtils.hideSoftInput(view);
    }

    private void showKeyboard() {
        KeyboardUtils.showSoftInput();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_clear) {
            mSearch.setText("");
        } else if (id == R.id.search_bar) {
            openSearch();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = mSearch.getText().toString();

        if (mOnSearchActionListener != null)
            mOnSearchActionListener.onInputChanged(input);

        if (input.equals(""))
            mClear.setVisibility(GONE);
        else
            mClear.setVisibility(VISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

            showKeyboard();
        } else {

            hideKeyboard(v);
        }

        if (mOnSearchActionListener != null) {
            mOnSearchActionListener.onFocusChanged(v, hasFocus);
        }
    }
}
