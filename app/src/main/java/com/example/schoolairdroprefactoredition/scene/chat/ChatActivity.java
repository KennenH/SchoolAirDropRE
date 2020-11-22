package com.example.schoolairdroprefactoredition.scene.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.ContentScrollMeasurer;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.presenter.impl.ChatViewModel;
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity;
import com.example.schoolairdroprefactoredition.scene.chat.entity.ChatReceiveMessageEntity;
import com.example.schoolairdroprefactoredition.scene.chat.entity.ChatSendMessageEntity;
import com.example.schoolairdroprefactoredition.scene.chat.panel.PanelMore;
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.adapter.MorePanelAdapter;
import com.example.schoolairdroprefactoredition.utils.decoration.DecorationUtil;
import com.example.schoolairdroprefactoredition.utils.decoration.GridItemDecoration;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.Nullable;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class ChatActivity extends ImmersionStatusBarActivity implements OnRefreshListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    public static final int TAKE_PHOTO = 111; // requestCode take photo
    public static final int PICK_PHOTO = 222; // requestCode select from album

    private PanelSwitchHelper mHelper;

    private ChatViewModel chatViewModel;

    private TextView mUserName;
    private EditText mInput;
    private ImageView mEmoji;
    private ImageView mMore;
    private TextView mSend;

    private RecyclerView mChatRecycler;
    private LinearLayoutManager mChatLayoutManager;
    private RecyclerView mEmojiRecycler;
    private RecyclerView mMoreRecycler;

    private final ChatRecyclerAdapter mChatRecyclerAdapter = new ChatRecyclerAdapter();

    private Animation sendOut;
    private Animation moreOut;
    private Animation in;
    private boolean isSendShowing = false;

    private int mUnfilledHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        setSupportActionBar(findViewById(R.id.toolbar));
        mUserName = findViewById(R.id.userName);
        mInput = findViewById(R.id.edit_view);
        mEmoji = findViewById(R.id.chat_bar_emotion);
        mMore = findViewById(R.id.chat_bar_addition);
        mSend = findViewById(R.id.chat_bar_send);
        mChatRecycler = findViewById(R.id.recycler_view);
        mEmojiRecycler = findViewById(R.id.emoji_recycler);
        mMoreRecycler = findViewById(R.id.more_recycler);

        mSend.setVisibility(View.GONE);

        initRecyclerLists();
        initEvents();
        initAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this)
                    .addEditTextFocusChangeListener((view, b) -> {
                        if (!b) KeyboardUtils.hideSoftInput(this);
                    })
                    .addContentScrollMeasurer(new ContentScrollMeasurer() {
                        @Override
                        public int getScrollDistance(int defaultDistance) {
                            return defaultDistance - mUnfilledHeight;
                        }

                        @Override
                        public int getScrollViewId() {
                            return R.id.recycler_view;
                        }
                    })
                    .addPanelChangeListener(new OnPanelChangeListener() {
                        @Override
                        public void onPanelSizeChange(@Nullable IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                        }

                        @Override
                        public void onKeyboard() {
                            mEmoji.setSelected(false);
                            mMore.setSelected(false);
                        }

                        @Override
                        public void onNone() {
                            mEmoji.setSelected(false);
                            mMore.setSelected(false);
                        }

                        @Override
                        public void onPanel(IPanelView view) {
                            mInput.clearFocus();
                            if (view instanceof PanelView) {
                                mEmoji.setSelected(((PanelView) view).getId() == R.id.panel_emotion);
                                mMore.setSelected(((PanelView) view).getId() == R.id.panel_addition);
                            }
                        }
                    })
                    .build();
        }
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

    ///////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// 初始化页面 //////////////////////////////////

    /**
     * 初始化动画
     */
    private void initAnim() {
        sendOut = AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right);
        moreOut = AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right);
        in = AnimationUtils.loadAnimation(this, R.anim.send_fade_in_right);
        sendOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSend.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSend.setVisibility(View.GONE);
                mSend.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //nothing
            }
        });

        moreOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMore.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMore.setVisibility(View.GONE);
                mMore.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //nothing
            }
        });
    }

    /**
     * 初始化监听事件
     */
    private void initEvents() {
        // 输入框输入监听
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = mInput.getText().toString();
                if (input.length() != 0) showSend();
                else if (input.equals("")) hideSend();
            }
        });

        // 发送按钮按下监听
        mSend.setOnClickListener(v -> {
            if (mInput.getText().toString().startsWith("...")) {
                mChatRecyclerAdapter.addData(0, new ChatReceiveMessageEntity(mInput.getText().toString().substring(3)));
            } else {
                mChatRecyclerAdapter.addData(0, new ChatSendMessageEntity(mInput.getText().toString()));
            }
            mInput.setText("");
            scrollToFirst();
        });

        // 聊天列表滑动监听
        mChatRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_DRAGGING) { // 消息列表滑动时隐藏键盘和面板
                    if (mHelper.isKeyboardState())
                        mInput.clearFocus();
                    else if (mHelper.isPanelState())
                        mHelper.resetState();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int childCount = recyclerView.getChildCount();
                    if (childCount > 0) {
                        View newestChildView = recyclerView.getChildAt(0);
                        int bottom = newestChildView.getBottom();
                        int listHeight = mChatRecycler.getHeight() - mChatRecycler.getPaddingBottom();
                        mUnfilledHeight = listHeight - bottom;
                    }
                }
            }
        });
    }

    /**
     * 初始化所有RV
     */
    private void initRecyclerLists() {
        // 聊天列表
        mChatLayoutManager = new LinearLayoutManager(this);
        mChatLayoutManager.setStackFromEnd(true);
        mChatLayoutManager.setReverseLayout(true);
        mChatLayoutManager.setSmoothScrollbarEnabled(true);
        mChatRecycler.setLayoutManager(mChatLayoutManager);
        mChatRecycler.setAdapter(mChatRecyclerAdapter);


        // 键盘中Emotion按钮，一排七个，均匀分布
        GridLayoutManager emojiManager = new GridLayoutManager(this, 7);
        mEmojiRecycler.setLayoutManager(emojiManager);
        mEmojiRecycler.addItemDecoration(new GridItemDecoration(7, getResources().getDimension(R.dimen.toolbar_icon)));


        // 键盘中Addition按键，一排四个，均匀分布
        GridLayoutManager moreManager = new GridLayoutManager(ChatActivity.this, 4);
        mMoreRecycler.setLayoutManager(moreManager);
        int space = DecorationUtil.getSpace(4, getResources().getDimension(R.dimen.toolbar_center_margin));
        mMoreRecycler.setPadding(space, space, space, space);
        mMoreRecycler.addItemDecoration(new GridItemDecoration(space));
        MorePanelAdapter morePanelAdapter = new MorePanelAdapter();
        morePanelAdapter.setList(PanelMore.getPanelMore());
        mMoreRecycler.setAdapter(morePanelAdapter);
        morePanelAdapter.notifyDataSetChanged();

    }


    ////////////////////////////////// 初始化页面 //////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////


    /**
     * 显示发送按钮，隐藏Addition按钮
     */
    private void showSend() {
        if (!isSendShowing) {
            isSendShowing = true;
            mSend.setVisibility(View.VISIBLE);
            mMore.startAnimation(moreOut);
            mSend.startAnimation(in);
        }
    }

    /**
     * 显示Addition按钮，隐藏发送按钮
     */
    private void hideSend() {
        if (isSendShowing) {
            isSendShowing = false;
            mMore.setVisibility(View.VISIBLE);
            mMore.startAnimation(in);
            mSend.startAnimation(sendOut);
        }
    }

    /**
     * 将列表滑动至最底下
     */
    private void scrollToFirst() {
        mChatLayoutManager.scrollToPosition(0);
    }

}