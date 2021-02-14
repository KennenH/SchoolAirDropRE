package com.example.schoolairdroprefactoredition.scene.main.messages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.databinding.FragmentMessagesBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.ui.adapter.MessagesRecyclerAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.MessageViewModel
import com.example.schoolairdroprefactoredition.viewmodel.UserViewModel
import com.github.ybq.android.spinkit.SpinKitView
import com.xiaomi.push.it
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.util.*
import kotlin.collections.ArrayList

class MessagesFragment : Fragment(), MainActivity.OnLoginStateChangedListener, Application.IMListener {

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
        MessagesRecyclerAdapter(messageViewModel)
    }

    private val manager by lazy {
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private val messageViewModel by lazy {
        MessageViewModel.MessageViewModelFactory((activity?.application as Application).chatRepository).create(MessageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is MainActivity) {
            (activity as MainActivity).addOnLoginActivityListener(this)
            (activity?.application as Application).addOnIMListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMessagesBinding.inflate(inflater, container, false)

        title = binding.messagesTitle
        recyclerView = binding.messagesRecycler
        loading = binding.messagesTitleLoading
        empty = binding.messagesEmpty

        recyclerView?.layoutManager = manager
        recyclerView?.setSwipeMenuCreator { _, rightMenu, position ->
            val delete = SwipeMenuItem(context)
            delete.text = getString(R.string.delete)
            delete.textSize = SizeUtils.dp2px(5f)
            delete.setTextColor(resources.getColor(R.color.whiteAlways, context?.theme))
            delete.setBackgroundColor(resources.getColor(R.color.colorPrimaryRed, context?.theme))
            delete.height = MATCH_PARENT
            rightMenu.addMenuItem(delete)
        }
        recyclerView?.setOnItemMenuClickListener { menuBridge, adapterPosition ->
            // 任何操作之前都必须先关闭菜单，否则会出现菜单混乱
            menuBridge.closeMenu()

            // 隐藏第position个会话
            mMessagesRecyclerAdapter.removeAt(adapterPosition)
            val counterpartId = mMessagesRecyclerAdapter.getCounterpartIdAt(adapterPosition)
            messageViewModel.swipeToHideChannel(counterpartId)
        }
        recyclerView?.adapter = mMessagesRecyclerAdapter

        messageViewModel.offlineNumLoadState.observe(viewLifecycleOwner) {
            when {
                it == LoadState.LOADING -> {
                    updateMessageState(MessageState.LOADING)
                }

                it == LoadState.ERROR -> {
                    updateMessageState(MessageState.REFRESH)
                }
            }
        }

        val token = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_TOKEN) as? DomainToken
        val userInfo = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean

        if (userInfo != null) {
            // 观察本地消息数量列表
            messageViewModel.getChatList(userInfo.userId.toString()).observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    empty?.visibility = View.GONE
                    mMessagesRecyclerAdapter.setList(it)
                } else {
                    empty?.visibility = View.VISIBLE
                }
            })
        }

        if (token != null) {
            updateMessageState(MessageState.LOADING)
            messageViewModel.getOfflineNumOnline(token).observe(viewLifecycleOwner) {
                // 将服务器离线消息数量合并入本地服务器，此时本地服务器数据改变，理论上将会自动刷新消息列表
                messageViewModel.saveOfflineNum(it)
                updateMessageState(MessageState.REFRESH)
            }
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
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as? DomainToken
        val userInfo = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean

        // 登录状态发生变化时，若发现是退出，则将消息列表清空
        if (token != null && userInfo != null) {
            // 重新观察本地消息数量列表
            messageViewModel.getChatList(userInfo.userId.toString()).observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    empty?.visibility = View.GONE
                    mMessagesRecyclerAdapter.setList(it)
                } else {
                    empty?.visibility = View.VISIBLE
                }
            })

            updateMessageState(MessageState.LOADING)
            // 获取服务器离线消息数量
            messageViewModel.getOfflineNumOnline(token).observe(viewLifecycleOwner) {
                // 将服务器离线消息数量合并入本地服务器，此时本地服务器数据改变，理论上将会自动刷新消息列表
                messageViewModel.saveOfflineNum(it)
                updateMessageState(MessageState.REFRESH)
            }
        } else {
            mMessagesRecyclerAdapter.setList(ArrayList())
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
    }

    override fun onIMErrorResponse(errorCode: Int, message: String) {
        // do nothing
    }
}