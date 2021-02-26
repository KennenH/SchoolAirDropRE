package com.example.schoolairdroprefactoredition.scene.main.messages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.databinding.FragmentMessagesBinding
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.BaseFragment
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.ui.adapter.MessagesRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.MessageViewModel
import com.github.ybq.android.spinkit.SpinKitView
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import net.x52im.mobileimsdk.server.protocal.Protocal
import kotlin.collections.ArrayList

class MessagesFragment : BaseFragment(), MainActivity.OnLoginStateChangedListener, Application.IMListener, MessagesRecyclerAdapter.UserInfoRequestListener, MainActivity.OnOfflineNumStateChangeListener {

    /**
     * 移动端IM的状态
     */
    annotation class MessageState {
        companion object {
            /**
             * 刷新当前状态
             */
            var REFRESH = 0

            /**
             * 正在连接服务器或者等待数据回调
             */
            var LOADING = 1
        }
    }

    /**
     * 当前是否已连接IM系统
     */
    private var isConnected = true

    private var title: TextView? = null

    private var loading: SpinKitView? = null

    private var recyclerView: SwipeRecyclerView? = null

    private var empty: View? = null

    private val mMessagesRecyclerAdapter by lazy {
        MessagesRecyclerAdapter()
    }

    private val manager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private val messageViewModel by lazy {
        MessageViewModel.MessageViewModelFactory((activity?.application as Application).chatRepository).create(MessageViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).apply {
                addOnLoginActivityListener(this@MessagesFragment)
                setOnPullingOfflineNum(this@MessagesFragment)
            }
            (activity?.application as Application).addOnIMListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMessagesBinding.inflate(inflater, container, false)

        title = binding.messagesTitle
        recyclerView = binding.messagesRecycler
        loading = binding.messagesTitleLoading
        empty = binding.messagesEmpty

        val myInfo = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean
        mMessagesRecyclerAdapter.setUserInfoRequestListener(this)
        recyclerView?.apply {
            layoutManager = manager
            setSwipeMenuCreator { _, rightMenu, position ->
                val delete = SwipeMenuItem(context)
                delete.text = getString(R.string.delete)
                delete.textSize = SizeUtils.dp2px(5f)
                delete.setTextColor(resources.getColor(R.color.whiteAlways, context?.theme))
                delete.setBackgroundColor(resources.getColor(R.color.colorPrimaryRed, context?.theme))
                delete.height = MATCH_PARENT
                rightMenu.addMenuItem(delete)
            }
            setOnItemMenuClickListener { menuBridge, adapterPosition ->
                // 任何操作之前都必须先关闭菜单，否则会出现菜单混乱
                menuBridge.closeMenu()
                // 若点击的是菜单中的第一个按钮，即删除，则隐藏会话
                if (menuBridge.position == 0) {
                    // 隐藏第position个会话
                    // 2021/2/23 Bug Fix： 这里如果直接调用data[adapterPosition]会莫名其妙 NPE crash
                    //           update： 只能多此一举地进行一次遍历才能正常
                    for ((index, datum) in mMessagesRecyclerAdapter.data.withIndex()) {
                        if (index == adapterPosition) {
                            mMessagesRecyclerAdapter.removeAt(adapterPosition)
                            if (mMessagesRecyclerAdapter.data.isEmpty()) {
                                empty?.visibility = View.VISIBLE
                            }
                            messageViewModel.swipeToHideChannel(myInfo?.userId.toString(), datum.counterpart_id)
                        }
                    }
                }
            }
            // 这一步设置adapter必须在上面两个步骤之后，否则会crash
            adapter = mMessagesRecyclerAdapter
        }
        if (myInfo != null) {
            // 观察本地消息数量列表，消息列表页面所有数据显示都通过该观察者，所以只需要在此判断数量显示即可
            messageViewModel.getChatList(myInfo.userId.toString()).observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    empty?.visibility = View.GONE
                    mMessagesRecyclerAdapter.setList(it)
                } else {
                    empty?.visibility = View.VISIBLE
                }
            })
        }
        return binding.root
    }

    /**
     * 更新消息页面的标题显示
     */
    private fun updateMessageState(@MessageState state: Int) {
        when (state) {
            MessageState.REFRESH -> {
                loading?.visibility = View.INVISIBLE
                if (isConnected) {
                    title?.text = getString(R.string.messages)
                } else {
                    title?.text = getString(R.string.disconnect)
                }
            }

            MessageState.LOADING -> {
                loading?.visibility = View.VISIBLE
                title?.text = getString(R.string.loading)
            }
        }
    }

    /**
     * 将消息列表滑动至最顶部
     */
    fun pageScrollToTop() {
        if (manager.findLastVisibleItemPosition() > 10) {
            recyclerView?.scrollToPosition(5)
        }
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun onLoginStateChanged(intent: Intent) {
        val userInfo = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean

        // 登录状态发生变化时，若发现是退出，则将消息列表清空
        if (userInfo != null) {
            // 重新观察本地消息数量列表，消息列表页面所有数据显示都通过该观察者，所以只需要在此判断数量显示即可
            messageViewModel.getChatList(userInfo.userId.toString()).observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    empty?.visibility = View.INVISIBLE
                    mMessagesRecyclerAdapter.setList(it)
                } else {
                    empty?.visibility = View.VISIBLE
                }
            })
        } else {
            mMessagesRecyclerAdapter.setList(ArrayList())
            empty?.visibility = View.VISIBLE
        }
    }

    override fun onLogging() {
        updateMessageState(MessageState.LOADING)
    }

    override fun onIMStartLogin() {
        updateMessageState(MessageState.LOADING)
    }

    override fun onIMLoginResponse(code: Int) {
        isConnected = code == 0
        updateMessageState(MessageState.REFRESH)
    }

    override fun onIMLinkDisconnect(code: Int) {
        isConnected = false
        updateMessageState(MessageState.REFRESH)
    }

    override fun onIMMessageLost(lostMessages: ArrayList<Protocal>) {
        // do nothing cause fragment has no ref on message's entity
    }

    override fun onIMMessageBeReceived(fingerprint: String) {
        // do nothing
    }

    override fun onIMReceiveMessage(fingerprint: String, senderID: String, content: String, typeu: Int) {
        // do nothing
    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
        // do nothing
    }

    /**
     * 用户信息的观察者是否已被添加
     */
    private var isUserInfoObserving = false

    /**
     * messageAdapter在显示用户信息时发现信息不全，请求外部获取
     */
    override fun onUserInfoRequest(userID: String, myID: String) {
        if (!isUserInfoObserving) {
            messageViewModel.getUserCache(userID.toInt()).observe(viewLifecycleOwner) {
                messageViewModel.getChatList(myID)
            }
        } else {
            messageViewModel.getUserCache(userID.toInt())
        }
    }

    /********************************************************************/
    /*********************主页获取离线消息数量状态回调**********************/
    /********************************************************************/
    override fun onPullingOfflineNum() {
        updateMessageState(MessageState.LOADING)
    }

    override fun onPullDone() {
        updateMessageState(MessageState.REFRESH)
    }
}