package com.example.schoolairdroprefactoredition.scene.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.interfaces.ContentScrollMeasurer
import com.effective.android.panel.interfaces.listener.OnEditFocusChangeListener
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener
import com.effective.android.panel.view.panel.IPanelView
import com.effective.android.panel.view.panel.PanelView
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.entity.ChatReceiveMessageEntity
import com.example.schoolairdroprefactoredition.scene.chat.entity.ChatSendMessageEntity
import com.example.schoolairdroprefactoredition.scene.chat.panel.PanelMore
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.MorePanelAdapter
import com.example.schoolairdroprefactoredition.utils.decoration.DecorationUtil
import com.example.schoolairdroprefactoredition.utils.decoration.GridItemDecoration
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.component_panel_addition.*
import kotlinx.android.synthetic.main.component_panel_emotion.*
import kotlinx.android.synthetic.main.fragment_messages.*

class ChatActivity : ImmersionStatusBarActivity(), OnRefreshListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }

        const val TAKE_PHOTO = 111 // 请求码 拍照
        const val PICK_PHOTO = 222 // 请求码 从相册选择照片
    }

    private val mHelper by lazy {
        PanelSwitchHelper.Builder(this)
                .addEditTextFocusChangeListener(object : OnEditFocusChangeListener {
                    override fun onFocusChange(view: View?, hasFocus: Boolean) {
//                        LogUtils.d("focus changed -- > $hasFocus")
                        if (!hasFocus) {
                            KeyboardUtils.hideSoftInput(this@ChatActivity)
                        }
                    }
                })
                .addContentScrollMeasurer(object : ContentScrollMeasurer {
                    override fun getScrollDistance(defaultDistance: Int): Int {
                        return defaultDistance - mUnfilledHeight
                    }

                    override fun getScrollViewId(): Int {
                        return R.id.recycler_view
                    }
                })
                .addPanelChangeListener(object : OnPanelChangeListener {
                    override fun onPanelSizeChange(panel: IPanelView?, portrait: Boolean, oldWidth: Int, oldHeight: Int, width: Int, height: Int) {}
                    override fun onKeyboard() {
                        chat_bar_emotion.isSelected = false
                        chat_bar_addition.isSelected = false
                    }

                    override fun onNone() {
                        chat_bar_emotion.isSelected = false
                        chat_bar_addition.isSelected = false
                    }

                    override fun onPanel(panel: IPanelView?) {
                        edit_view.clearFocus()
                        if (panel is PanelView) {
                            chat_bar_emotion.isSelected = panel.id == R.id.panel_emotion
                            chat_bar_addition.isSelected = panel.id == R.id.panel_addition
                        }
                    }
                })
                .build()
    }

    private val chatViewModel by lazy {
        ViewModelProvider(this).get(ChatViewModel::class.java)
    }

    private val mChatLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val mChatRecyclerAdapter = ChatRecyclerAdapter()

    private val sendOut by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right)
    }

    private val moreOut by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right)
    }

    private val inAnim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_in_right)
    }

    // 发送按钮是否显示标志
    private var isSendShowing = false

    private var mUnfilledHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(toolbar)
        initRecyclerLists()
        initEvents()
        initAnim()
    }

    override fun onBackPressed() {
        if (mHelper.hookSystemBackByPanelSwitcher()) {
            return
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.more, menu)
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        // todo 加载15条历史记录
    }


    ///////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// 初始化页面 //////////////////////////////////
    /**
     * 初始化动画
     */
    private fun initAnim() {
        sendOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                chat_bar_send.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animation) {
                chat_bar_send.visibility = View.GONE
                chat_bar_send.isEnabled = true
            }

            override fun onAnimationRepeat(animation: Animation) {
                //nothing
            }
        })

        moreOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                chat_bar_addition.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animation) {
                chat_bar_addition.visibility = View.GONE
                chat_bar_addition.isEnabled = true
            }

            override fun onAnimationRepeat(animation: Animation) {
                //nothing
            }
        })
    }

    /**
     * 初始化监听事件
     */
    private fun initEvents() {
        // 随意调用一个方法以初始化helper
        mHelper.resetState()

        // 输入框输入监听
        edit_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val input = edit_view.text.toString()
                if (input.isNotEmpty()) {
                    showSend()
                } else if (input == "") {
                    hideSend()
                }
            }
        })

        // 发送按钮按下监听
        chat_bar_send.setOnClickListener {
            // TODO: 2020/12/3 以下代码仅用于debug
            if (edit_view.text.toString().startsWith("...")) {
                mChatRecyclerAdapter.addData(0, ChatReceiveMessageEntity(edit_view.text.toString().substring(3)))
            } else {
                mChatRecyclerAdapter.addData(0, ChatSendMessageEntity(edit_view.text.toString()))
            }

            edit_view.setText("")
            scrollToFirst()
        }

        // 聊天列表滑动监听
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) { // 消息列表滑动时隐藏键盘和面板
                    edit_view.clearFocus()
                    mHelper.resetState()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val childCount = recyclerView.childCount
                    if (childCount > 0) {
                        val newestChildView = recyclerView.getChildAt(0)
                        val bottom = newestChildView.bottom
                        val listHeight = recycler_view.height - recycler_view.paddingBottom
                        mUnfilledHeight = listHeight - bottom
                    }
                }
            }
        })
    }

    /**
     * 初始化所有RV
     */
    private fun initRecyclerLists() {
        chat_bar_send.visibility = View.GONE

        // 聊天列表
        mChatLayoutManager.stackFromEnd = true
        mChatLayoutManager.reverseLayout = true
        mChatLayoutManager.isSmoothScrollbarEnabled = true
        recycler_view.layoutManager = mChatLayoutManager
        recycler_view.adapter = mChatRecyclerAdapter


        // 键盘中Emotion按钮，一排七个，均匀分布
        val emojiManager = GridLayoutManager(this, 7)
        emoji_recycler.layoutManager = emojiManager
        emoji_recycler.addItemDecoration(GridItemDecoration(7, resources.getDimension(R.dimen.toolbar_icon)))


        // 键盘中Addition按键，一排四个，均匀分布
        val moreManager = GridLayoutManager(this@ChatActivity, 4)
        more_recycler.layoutManager = moreManager
        val space = DecorationUtil.getSpace(4, resources.getDimension(R.dimen.toolbar_center_margin))
        more_recycler.setPadding(space, space, space, space)
        more_recycler.addItemDecoration(GridItemDecoration(space))
        val morePanelAdapter = MorePanelAdapter()
        morePanelAdapter.setList(PanelMore.getPanelMore())
        more_recycler.adapter = morePanelAdapter

        morePanelAdapter.notifyDataSetChanged()
    }
    ////////////////////////////////// 初始化页面 //////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * 显示发送按钮，隐藏Addition按钮
     */
    private fun showSend() {
        if (!isSendShowing) {
            isSendShowing = true
            chat_bar_send.visibility = View.VISIBLE
            chat_bar_addition.startAnimation(moreOut)
            chat_bar_send.startAnimation(inAnim)
        }
    }

    /**
     * 显示Addition按钮，隐藏发送按钮
     */
    private fun hideSend() {
        if (isSendShowing) {
            isSendShowing = false
            chat_bar_addition.visibility = View.VISIBLE
            chat_bar_addition.startAnimation(inAnim)
            chat_bar_send.startAnimation(sendOut)
        }
    }

    /**
     * 将列表滑动至最底下
     */
    private fun scrollToFirst() {
        val firstVisible = mChatLayoutManager.findFirstVisibleItemPosition()
        if (firstVisible > 20) {
            recycler_view.scrollToPosition(10)
        }
        recycler_view.smoothScrollToPosition(0)
    }
}