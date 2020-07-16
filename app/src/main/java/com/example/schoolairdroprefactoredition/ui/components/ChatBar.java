package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.schoolairdroprefactoredition.R;

public class ChatBar extends ConstraintLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private EditText mInput;
    private TextView mSend;
    private ImageView mStriker;
    private ImageView mMore;

    private OnChatBarActionListener mOnChatBarActionListener;

    public ChatBar(Context context) {
        this(context, null);
    }

    public ChatBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_chat_bar, this, true);

        mInput = findViewById(R.id.chat_bar_input);
        mSend = findViewById(R.id.chat_bar_send);
        mStriker = findViewById(R.id.chat_bar_striker);
        mMore = findViewById(R.id.chat_bar_more);

        mInput.setOnClickListener(this);
        mInput.addTextChangedListener(this);
        mInput.setOnFocusChangeListener(this);
        mSend.setOnClickListener(this);
        mStriker.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }

    public interface OnChatBarActionListener {
        /**
         * 消息发送
         */
        void onSend();

        /**
         * 输入框焦点改变
         */
        void onInputFocusChanged(View v, boolean hasFocus);
    }

    public void setOnChatBarActionListener(OnChatBarActionListener listener) {
        this.mOnChatBarActionListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.chat_bar_input:

                break;
            case R.id.chat_bar_send:
                if (mOnChatBarActionListener != null) {
                    mOnChatBarActionListener.onSend();
                }
                break;
            case R.id.chat_bar_striker:
                // todo switch to striker panel
                break;
            case R.id.chat_bar_more:
                // todo switch to more panel
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = mInput.getText().toString();
        if (input.equals("")) {
            // todo send animate in
        } else {

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (mOnChatBarActionListener != null) {
            mOnChatBarActionListener.onInputFocusChanged(v, hasFocus);
        }
    }
}
