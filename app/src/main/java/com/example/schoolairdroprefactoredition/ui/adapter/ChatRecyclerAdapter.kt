package com.example.schoolairdroprefactoredition.ui.adapter

import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.database.pojo.ChatHistory
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.DomainSimpleUserInfo
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.example.schoolairdroprefactoredition.utils.TimeUtil
import com.lxj.xpopup.XPopup
import javadz.beanutils.BeanUtils
import java.util.*

class ChatRecyclerAdapter(private var myInfo: DomainUserInfo.DataBean?, counterpartInfo: DomainSimpleUserInfo?) : BaseDelegateMultiAdapter<ChatHistory, BaseViewHolder>() {

    companion object {
        /**
         * 我发送的文字消息
         */
        const val ITEM_CHAT_SEND = 0

        /**
         * 我收到的文字消息
         */
        const val ITEM_CHAT_RECEIVE = 1

        /**
         * 我发送的图片消息
         */
        const val ITEM_CHAT_SEND_IMAGE = 2

        /**
         * 我收到的图片消息
         */
        const val ITEM_CHAT_RECEIVE_IMAGE = 3
    }

    /**
     * 消息发送状态
     */
    internal annotation class MessageSendStatus {
        companion object {
            /**
             * 正在发送
             * 显示loading动画
             * 与failed图标不可同时出现
             */
            const val SENDING = 0

            /**
             * 隐藏其他两个状态
             */
            const val SUCCESS = 1

            /**
             * 发送失败
             * 显示failed图标，点击可以重新发送
             * 与loading动画不可同时出现
             */
            const val FAILED = 2
        }
    }

    /**
     * 对方的基本信息
     */
    private var mCounterpartInfo: DomainSimpleUserInfo? = counterpartInfo

    /**
     * 上一条消息的时间
     *
     * 当上一条消息与即将要显示的消息间隔不超过5分钟时，将不会显示下一条消息的时间
     */
    private var lastMessageTime: Date? = null

    /**
     * 重新设置对方信息
     *
     * 比如从聊天页面进入对方的个人主页获取了用户信息之后返回到聊天页面
     * 用户缓存信息可能会被更新，此时需要重新设置对方的信息以刷新ui
     */
    fun setCounterPartInfo(counterpartInfo: DomainUserInfo.DataBean?) {
        if (counterpartInfo != null) {
            BeanUtils.copyProperties(mCounterpartInfo, counterpartInfo)
        }
    }

    /**
     * 更新消息发送状态
     *
     * 更新的消息一定是我发送的消息
     * 我接受到的消息一定是发送和接收成功了的
     */
    @Synchronized
    fun updateStatus(fingerprint: String, @MessageSendStatus status: Int) {
        for ((index, datum) in data.withIndex()) {
            if (datum.fingerprint == fingerprint) {
                datum.status = status
                notifyItemChanged(index)
                return // 找到了直接返回
            }
        }
    }

    /**
     * 批量更新消息发送失败，只有失败时使用该方法，其余都使用以上更新方法
     */
    @Synchronized
    fun updateStatus(fingerprints: List<String>) {
        for (fingerprint in fingerprints) {
            for ((index, datum) in data.withIndex()) {
                if (datum.fingerprint == fingerprint) {
                    datum.status = MessageSendStatus.FAILED
                    notifyItemChanged(index)
                    break // 找到了直接找下一个
                }
            }
        }
    }

    /**
     * 当[com.example.schoolairdroprefactoredition.scene.chat.ChatActivity]
     * 收到登录状态改变回调时列表也将同时改变
     */
    fun updateMyUserInfo(myInfo: DomainUserInfo.DataBean?) {
        if (myInfo != null) {
            this.myInfo = myInfo
        }
    }

    /**
     * 我的头像url
     */
    private val myAvatarUrl by lazy {
        ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(myInfo?.userAvatar)
    }

    /**
     * 对方的头像url
     */
    private var counterpartUrl: String? = null


    init {
        setMultiTypeDelegate(ChatEntityDelegate(myInfo?.userId.toString()))
    }

    /**
     * 显示消息时间
     *
     * 当上一条消息的时间与将要显示的时间间隔不超过5分钟时，将要显示的消息将不会显示时间
     */
    private fun showTime(holder: BaseViewHolder, time: Date, isSend: Boolean) {
        val id = if (isSend) R.id.send_time else R.id.receive_time
        if (lastMessageTime != null) {
            val timeSpan = TimeUtils.getTimeSpan(time, lastMessageTime, TimeConstants.SEC)
            if (timeSpan > 300) {
                holder.setText(id, TimeUtil.getChatTimeSpanByNow(time))
            } else {
                holder.setGone(id, true)
            }
        } else {
            holder.setText(id, TimeUtil.getChatTimeSpanByNow(time))
        }
        lastMessageTime = time
    }

    /**
     * 显示状态视图
     *
     * 正在发送显示小菊花、发送成功隐藏所有、发送失败显示感叹号
     */
    private fun showStatusView(holder: BaseViewHolder, item: ChatHistory) {
        val sending = holder.itemView.findViewById<View?>(R.id.send_sending)
        val failed = holder.itemView.findViewById<View?>(R.id.send_failed)
        when (item.status) {
            MessageSendStatus.SENDING -> {
                sending?.visibility = View.VISIBLE
                failed?.visibility = View.INVISIBLE
            }
            MessageSendStatus.FAILED -> {
                sending?.visibility = View.INVISIBLE
                failed?.visibility = View.VISIBLE
            }
            MessageSendStatus.SUCCESS -> {
                sending?.visibility = View.INVISIBLE
                failed?.visibility = View.INVISIBLE
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: ChatHistory) {
        val date = Date(item.send_time)
        when (holder.itemViewType) {
            // 我发送的文本消息
            ITEM_CHAT_SEND -> {
                // 获取头像的 view
                val avatarView = holder.itemView.findViewById<ImageView>(R.id.send_avatar)
                // 消息文本
                holder.setText(R.id.send_text, item.message)
                // 加载头像
                ImageUtil.loadRoundedImage(avatarView, myAvatarUrl)
                // 显示时间
                showTime(holder, date, true)
                // 点击头像打开个人主页
                avatarView.setOnClickListener {
                    UserActivity.start(context, myInfo?.userId)
                }
                // 显示发送状态
                showStatusView(holder, item)
            }
            // 我发送的图片消息
            ITEM_CHAT_SEND_IMAGE -> {
                // 获取头像view
                val avatarView = holder.itemView.findViewById<ImageView>(R.id.send_avatar)
                // 加载头像
                ImageUtil.loadRoundedImage(avatarView, myAvatarUrl)
                // 获取图片的 view
                val imageView = holder.itemView.findViewById<ImageView>(R.id.send_image)
                // 显示时间
                showTime(holder, date, true)
                // 加载图片
                ImageUtil.loadRoundedImage(imageView, item.message)
                // 点击头像进入个人主页
                avatarView.setOnClickListener {
                    UserActivity.start(context, myInfo?.userId)
                }
                // 点击图片查看
                imageView.setOnClickListener {
                    XPopup.Builder(context)
                            .isDarkTheme(true)
                            .asImageViewer(imageView, item.message, false, -1, -1, -1, true, context.getColor(R.color.blackAlways), MyUtil.ImageLoader())
                            .show()
                }
                // 显示发送状态
                showStatusView(holder, item)
            }
            // 我收到的文本消息
            ITEM_CHAT_RECEIVE -> {
                // 获取头像view
                val avatarView = holder.itemView.findViewById<ImageView>(R.id.receive_avatar)
                // 加载头像
                if (counterpartUrl == null) {
                    counterpartUrl = ConstantUtil.QINIU_BASE_URL + (ImageUtil.fixUrl(mCounterpartInfo?.userAvatar)
                            ?: ConstantUtil.DEFAULT_AVATAR)
                }
                ImageUtil.loadRoundedImage(avatarView, counterpartUrl)
                // 消息内容
                holder.setText(R.id.receive_text, item.message)
                // 设置时间
                showTime(holder, date, false)
                // 点击头像进入个人主页
                avatarView.setOnClickListener {
                    UserActivity.start(context, mCounterpartInfo?.userId)
                }
            }
            // 我收到的图片消息
            ITEM_CHAT_RECEIVE_IMAGE -> {
                // 获取头像view
                val avatarView = holder.itemView.findViewById<ImageView>(R.id.receive_avatar)
                // 加载头像
                if (counterpartUrl == null) {
                    counterpartUrl = ConstantUtil.QINIU_BASE_URL + (ImageUtil.fixUrl(mCounterpartInfo?.userAvatar)
                            ?: ConstantUtil.DEFAULT_AVATAR)
                }
                ImageUtil.loadRoundedImage(avatarView, counterpartUrl)
                // 获取图片view
                val imageView = holder.itemView.findViewById<ImageView>(R.id.receive_image)
                // 设置时间
                showTime(holder, date, false)
                // 加载图片
                ImageUtil.loadRoundedImage(imageView, item.message)
                // 点击头像进入个人主页
                avatarView.setOnClickListener {
                    UserActivity.start(context, mCounterpartInfo?.userId)
                }
                // 点击图片查看
                imageView.setOnClickListener {
                    XPopup.Builder(context)
                            .isDarkTheme(true)
                            .asImageViewer(imageView, item.message, false, -1, -1, -1, true, context.getColor(R.color.blackAlways), MyUtil.ImageLoader())
                            .show()
                }
            }
        }
    }

    /**
     * 消息类型代理类
     * 虽有的消息都是同一种bean，但是需要通过bean来区分消息显示的布局
     */
    class ChatEntityDelegate(private val myID: String) : BaseMultiTypeDelegate<ChatHistory>() {
        override fun getItemType(data: List<ChatHistory>, position: Int): Int {
            // 获取到本item的数据bean
            val history = data[position]
            // 判断消息的发送方
            return if (history.sender_id == myID) {
                // 判断消息的类型
                if (history.message_type == ConstantUtil.MESSAGE_TYPE_IMAGE) {
                    ITEM_CHAT_SEND_IMAGE
                } else {
                    ITEM_CHAT_SEND
                }
            } else {
                if (history.message_type == ConstantUtil.MESSAGE_TYPE_IMAGE) {
                    ITEM_CHAT_RECEIVE_IMAGE
                } else {
                    ITEM_CHAT_RECEIVE
                }
            }
        }

        init {
            addItemType(ITEM_CHAT_SEND, R.layout.item_chat_send)
                    .addItemType(ITEM_CHAT_RECEIVE, R.layout.item_chat_receive)
                    .addItemType(ITEM_CHAT_SEND_IMAGE, R.layout.item_chat_send_image)
                    .addItemType(ITEM_CHAT_RECEIVE_IMAGE, R.layout.item_chat_receive_image)
        }
    }
}