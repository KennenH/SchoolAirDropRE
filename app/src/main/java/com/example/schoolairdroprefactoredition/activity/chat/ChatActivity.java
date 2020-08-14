package com.example.schoolairdroprefactoredition.activity.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnEditFocusChangeListener;
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.interfaces.listener.OnViewClickListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.activity.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.model.adapterbean.MorePanelBean;
import com.example.schoolairdroprefactoredition.presenter.impl.ChatImpl;
import com.example.schoolairdroprefactoredition.ui.adapter.MorePanelAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.GridItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends ImmersionStatusBarActivity implements OnRefreshListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    private PanelSwitchHelper mHelper;

    private ChatImpl chatImpl;

    private TextView mUserName;
    private EditText mInput;
    private ImageView mEmoji;
    private ImageView mMore;
    private TextView mSend;

    private RecyclerView mChatRecycler;
    private LinearLayoutManager mChatLayoutManager;
    private RecyclerView mEmojiRecycler;
    private RecyclerView mMoreRecycler;

    @Override
    protected void onStart() {
        super.onStart();
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this)
                    .addEditTextFocusChangeListener((view, b) -> {
                        if (!b) KeyboardUtils.hideSoftInput(this);
                    })
                    .contentScrollOutsideEnable(true)
                    .build();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        chatImpl = new ViewModelProvider(this).get(ChatImpl.class);

        setSupportActionBar(findViewById(R.id.toolbar));
        mUserName = findViewById(R.id.user_name);
        mInput = findViewById(R.id.edit_text);
        mEmoji = findViewById(R.id.chat_bar_emoji);
        mMore = findViewById(R.id.chat_bar_more);
        mSend = findViewById(R.id.chat_bar_send);
        mChatRecycler = findViewById(R.id.recycler_view);
        mEmojiRecycler = findViewById(R.id.emoji_recycler);
        mMoreRecycler = findViewById(R.id.more_recycler);

        mChatLayoutManager = new LinearLayoutManager(this);
        mChatLayoutManager.setStackFromEnd(true);
        mChatLayoutManager.setReverseLayout(true);
        mChatLayoutManager.setSmoothScrollbarEnabled(true);
        mChatRecycler.setLayoutManager(mChatLayoutManager);

        GridLayoutManager emojiManager = new GridLayoutManager(this, 7);
        mEmojiRecycler.addItemDecoration(new GridItemDecoration(7, getResources().getDimension(R.dimen.toolbar_center_margin)));
        mEmojiRecycler.setLayoutManager(emojiManager);

        GridLayoutManager moreManager = new GridLayoutManager(this, 4);
        mMoreRecycler.addItemDecoration(new GridItemDecoration(4, getResources().getDimension(R.dimen.toolbar_center_margin)));
        MorePanelAdapter morePanelAdapter = new MorePanelAdapter();
        morePanelAdapter.setList(getMorePanelItem());
        mMoreRecycler.setLayoutManager(moreManager);
        mMoreRecycler.setAdapter(morePanelAdapter);
    }

    private List<MorePanelBean> getMorePanelItem() {
        List<MorePanelBean> list = new ArrayList<>();
        MorePanelBean quote = new MorePanelBean(R.drawable.ic_quote);
        MorePanelBean album = new MorePanelBean(R.drawable.ic_album);
        MorePanelBean camera = new MorePanelBean(R.drawable.ic_camera);
        MorePanelBean location = new MorePanelBean(R.drawable.ic_map);
        list.add(quote);
        list.add(album);
        list.add(camera);
        list.add(location);
        return list;
    }


    @Override
    public void onBackPressed() {
        if (mHelper != null && mHelper.hookSystemBackByPanelSwitcher()) return;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        else if (id == R.id.more) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.chat_more, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        // todo 加载10条历史记录
    }
}