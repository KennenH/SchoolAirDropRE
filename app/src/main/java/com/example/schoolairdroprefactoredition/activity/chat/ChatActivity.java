package com.example.schoolairdroprefactoredition.activity.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.presenter.impl.ChatImpl;
import com.example.schoolairdroprefactoredition.ui.components.ChatBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class ChatActivity extends ImmersionStatusBarActivity implements OnRefreshListener, ChatBar.OnChatBarActionListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    private ChatImpl chatImpl;
    private SmartRefreshLayout mRefresh;
    private RecyclerView mChatList;
    private ChatBar mChatBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatImpl = new ViewModelProvider(this).get(ChatImpl.class);

        mRefresh = findViewById(R.id.chat_refresh);
        mChatList = findViewById(R.id.chat_list);
        mChatBar = findViewById(R.id.chat_bar);

        mRefresh.setOnRefreshListener(this);
        mChatBar.setOnChatBarActionListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        // todo 加载10条历史记录
    }

    @Override
    public void onSend() {

    }

    @Override
    public void onInputFocusChanged(View v, boolean hasFocus) {

    }
}