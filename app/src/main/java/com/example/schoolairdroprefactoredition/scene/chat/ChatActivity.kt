package com.example.schoolairdroprefactoredition.scene.chat

import android.app.Activity
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.interfaces.ContentScrollMeasurer
import com.effective.android.panel.interfaces.listener.OnEditFocusChangeListener
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener
import com.effective.android.panel.view.panel.IPanelView
import com.effective.android.panel.view.panel.PanelView
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.DomainSimpleUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.panel.PanelMore
import com.example.schoolairdroprefactoredition.ui.adapter.ChatRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.MorePanelAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.decoration.DecorationUtil
import com.example.schoolairdroprefactoredition.utils.decoration.GridItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.ChatViewModel
import com.luck.picture.lib.PictureSelector
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import javadz.beanutils.BeanUtils
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.toolbar
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.component_panel_addition.*
import kotlinx.android.synthetic.main.component_panel_emotion.*
import kotlinx.android.synthetic.main.fragment_messages.*
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : ImmersionStatusBarActivity(), Application.IMListener, OnRefreshListener, Application.OnApplicationLoginListener {

    companion object {
        /**
         * @param userInfo 对方的用户信息
         */
        fun start(context: Context, userInfo: DomainUserInfo.DataBean?) {
            if (userInfo == null) {
                return
            }

            val simpleInfo = DomainSimpleUserInfo()
            BeanUtils.copyProperties(simpleInfo, userInfo)

            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra(ConstantUtil.KEY_USER_SIMPLE_INFO, simpleInfo)
            context.startActivity(intent)
        }

//        fun startForTest(context: Context) {
//            val userInfo = DomainUserInfo.DataBean()
//            userInfo.userId = 7
//            userInfo.userName = "user_7"
//            userInfo.userAvatar = "/uploads/img/user/default/default.jpg"
//
//            val simpleInfo = DomainSimpleUserInfo()
//            BeanUtils.copyProperties(simpleInfo, userInfo)
//
//            val intent = Intent(context, ChatActivity::class.java)
//
//            intent.putExtra(ConstantUtil.KEY_USER_SIMPLE_INFO, simpleInfo)
//            context.startActivity(intent)
//        }

        const val TAKE_PHOTO = 5972 // 拍照 请求码
        const val PICK_ALBUM = 9236 // 从相册选择照片 请求码
    }

    private val mHelper by lazy {
        PanelSwitchHelper.Builder(this)
                .addEditTextFocusChangeListener(object : OnEditFocusChangeListener {
                    override fun onFocusChange(view: View?, hasFocus: Boolean) {
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
                        scrollToFirst()
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
                        scrollToFirst()
                    }
                })
                .build()
    }

    private val chatViewModel by lazy {
        ChatViewModel.ChatViewModelFactory((application as Application).chatRepository, application).create(ChatViewModel::class.java)
    }

    private val mChatLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val mChatRecyclerAdapter by lazy {
        ChatRecyclerAdapter((application as Application).getCachedMyInfo(), counterpartInfo)
    }

    /**
     * 发送按钮出现 动画
     */
    private val sendOut by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right)
    }

    /**
     * 更多按钮出现 动画
     */
    private val moreOut by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_out_right)
    }

    /**
     * 按钮进入 动画
     */
    private val inAnim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.send_fade_in_right)
    }

    /**
     * 发送按钮是否正在显示的标志
     */
    private var isSendShowing = false

    /**
     * 是否第一次进入页面调用scrollToFirst
     *
     * 因为在进入页面之后view model将会观察后续的getChat操作
     * 但是后续的getChat不能调用scrollToFirst
     */
    private var isFirstGetChat = true

    /**
     * 键盘高度，框架会将其自动更新为正确值
     */
    private var mUnfilledHeight = 0

    /**
     * 正在对话的用户信息
     */
    private val counterpartInfo by lazy {
        intent.getSerializableExtra(ConstantUtil.KEY_USER_SIMPLE_INFO) as? DomainSimpleUserInfo
    }

    // todo 来自其他用户的消息数量，当在和一个用户的聊天页面时其他人发来消息，便在左上角显示消息数量
    private var newMessagesFromOthers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        (application as Application).addOnApplicationLoginListener(this)
        setSupportActionBar(toolbar)
        initRecyclerLists()
        initListeners()
        initAnim()

        val token = (application as Application).getCachedToken()
        val myInfo = (application as Application).getCachedMyInfo()
        // 观察本地消息记录
        chatViewModel.getChat(
                token?.access_token,
                myInfo?.userId.toString(),
                counterpartInfo?.userId.toString())
                .observe(this) {
                    // 刷新完毕要调用完成刷新操作
                    if (it.size < ConstantUtil.DATA_FETCH_DEFAULT_SIZE) {
                        recycler_container.finishRefreshWithNoMoreData()
                    } else {
                        recycler_container.finishRefresh(true)
                    }
                    // 将本地消息显示在对话列表中
                    mChatRecyclerAdapter.addData(mChatRecyclerAdapter.data.size, it)
                    if (isFirstGetChat) {
                        isFirstGetChat = false
                        recycler_view.scrollToPosition(0)
                    }
                    // ack会话消息数量
                    chatViewModel.ackOfflineNum(myInfo?.userId.toString(), counterpartInfo?.userId.toString())
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                // 拍照返回
                val picture = PictureSelector.obtainMultipleResult(data)[0]
                val path = picture.androidQToPath ?: picture.path
                val pathWrapper = ArrayList<String>(1)
                val myInfo = (application as Application).getCachedMyInfo()
                (application as Application).sendImageMessage(
                        counterpartInfo?.userId.toString(),
                        myInfo?.userId.toString(),
                        pathWrapper.also { it.add(path) },
                        WeakReference(mChatRecyclerAdapter))
                // 将镜头置于最新消息处
                scrollToFirst()
            } else if (requestCode == PICK_ALBUM) {
                // 相册选择照片返回
                val pictures = PictureSelector.obtainMultipleResult(data)
                val pathsWrapper = ArrayList<String>(pictures.size)
                for (picture in pictures) {
                    pathsWrapper.add(picture.androidQToPath ?: picture.path)
                }
                val myInfo = (application as Application).getCachedMyInfo()
                (application as Application).sendImageMessage(
                        counterpartInfo?.userId.toString(),
                        myInfo?.userId.toString(),
                        pathsWrapper,
                        WeakReference(mChatRecyclerAdapter))
                // 将镜头置于最新消息处
                scrollToFirst()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.more, menu)
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        val token = (application as Application).getCachedToken()
        val myInfo = (application as Application).getCachedMyInfo()
        if (token != null && token.access_token != null) {
            // 获取本地消息记录，由于前面已经设置观察者，此处只需要调用即可
            chatViewModel.getChat(
                    token.access_token,
                    myInfo?.userId.toString(),
                    counterpartInfo?.userId.toString(),
                    mChatRecyclerAdapter.data.last().fingerprint)
        }
    }

///////////////////////////////////////////////////////////////////////////////
////////////////////////////////// 初始化页面 //////////////////////////////////
//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
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
    private fun initListeners() {
        // 监听IM聊天系统回调
        (application as Application).addOnIMListener(this)

        // 随意调用一个方法以初始化helper
        mHelper.resetState()

        // 输入框输入监听
        edit_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (edit_view.text.toString().isNotEmpty()) {
                    showSend()
                } else {
                    hideSend()
                }
            }
        })

        // 发送按钮按下监听
        chat_bar_send.setOnClickListener {
            // 发送消息
            val myInfo = (application as Application).getCachedMyInfo()
            if (myInfo != null) {
                // 获取输入框内容
                val content = edit_view.text.toString()
                // 将输入框清空
                edit_view.setText("")
                // 委托顶层类发送消息
                (application as Application).sendTextMessage(
                        counterpartInfo?.userId.toString(),
                        myInfo.userId.toString(),
                        content,
                        WeakReference(mChatRecyclerAdapter))
                // 对话窗滑动至最新消息
                scrollToFirst()
            }
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
                if (recyclerView.layoutManager is LinearLayoutManager) {
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

        /**
         * 上拉加载更多监听器
         */
        recycler_container.setOnRefreshListener(this)
    }

    /**
     * 初始化所有RV
     */
    private fun initRecyclerLists() {
        user_name.text = counterpartInfo?.userName ?: counterpartInfo?.userId.toString()
        chat_bar_send.visibility = View.GONE

        // 聊天列表倒序manager
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
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑//
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
     * 将列表滑动至最新消息处
     */
    private fun scrollToFirst() {
        val firstVisible = mChatLayoutManager.findFirstVisibleItemPosition()
        if (firstVisible > 20) {
            recycler_view.scrollToPosition(10)
        }
        recycler_view.smoothScrollToPosition(0)
    }

    /////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// 回调方法 ///////////////////////////////////////
//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    override fun onIMStartLogin() {
        // do nothing
    }

    override fun onIMLoginResponse(code: Int) {
        // do nothing
    }

    override fun onIMLinkDisconnect(code: Int) {
        // do nothing
    }

    override fun onIMMessageLost(lostMessages: ArrayList<Protocal>) {
        // do nothing cause messages that sent failed were handled after async callback
    }

    override fun onIMMessageBeReceived(fingerprint: String) {
        // do nothing, same reason as above
    }

    override fun onIMReceiveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        val myInfo = (application as Application).getCachedMyInfo()
        // 若正好是对方用户发来的消息则将其显示在聊天框中
        if (senderID == counterpartInfo?.userId.toString()) {
            mChatRecyclerAdapter.addData(0, ChatHistory(fingerprint, senderID, myInfo?.userId.toString(), typeu, content, Date(), 0))
            // 若当前已经在最底下，则列表将会自动跟踪至最新消息处
            if (!recycler_view.canScrollVertically(-1)) {
                scrollToFirst()
            }
        }
    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
        // do nothing
    }

    override fun onApplicationLoginStateChange(isLogged: Boolean) {
        if (isLogged) {
            val myInfo = (application as Application).getCachedMyInfo()
            val token = (application as Application).getCachedToken()
            if (myInfo != null && token != null) {
                chatViewModel.getChat(token.access_token, myInfo.userId.toString(), counterpartInfo?.userId.toString())
                mChatRecyclerAdapter.updateMyUserInfo(myInfo)
            }
        }
    }
}