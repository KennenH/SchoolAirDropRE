package com.example.schoolairdroprefactoredition.scene.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.components.OverDragEventHeader
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.utils.MyUtil.ImageLoader
import com.example.schoolairdroprefactoredition.viewmodel.UserViewModel
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_user.*
import java.util.*

class UserActivity : ImmersionStatusBarActivity(), View.OnClickListener, OverDragEventHeader.OnHeaderOverDragEventListener {

    companion object {
        /**
         * 背景图初始宽度即为屏幕宽
         */
        val originBackgroundWidth = ScreenUtils.getAppScreenWidth()

        /**
         * 拉动这么多距离的时候会使背景放大
         */
        const val animOffset = 40

        /**
         * 请求更新我的用户信息 请求码
         *
         * 此更新不同于下面的更新，是我手动更新用户信息，比如修改头像名字等的更新，更新后返回首页将会重新
         * 获取我的用户信息
         */
        const val REQUEST_UPDATE_MY = 520

        /**
         * 请求更新别人的用户信息 请求码
         *
         * 不同与上面的更新的是，此更新非手动，而是自动完成，进入页面后网络请求了对方的用户信息，有可能更新对方
         * 的用户信息，此时回到上一个页面，便可以用最新的用户信息来填充ui
         */
        const val REQUEST_UPDATE_INFO = 1314

        /**
         * ********* 注意 *********
         *   仅用于打开自己的主页
         * ********* 注意 *********
         *
         * 从'我的'页面中打开个人信息
         * 直接传递我的信息即可
         */
        fun start(context: Context?) {
            if (context == null) {
                return
            }

            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, true)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, REQUEST_UPDATE_MY)
            }
        }

        /**
         * 从任何其他非'我的'的页面打开本页面
         * 需要一个userID且无论该页面是否查看的是本人的个人主页，都无法修改个人信息
         *
         * @param userID    要查看的用户id
         */
        fun start(context: Context?, userID: Int?) {
            if (context == null || userID == null || userID == -1) {
                return
            }

            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_USER_ID, userID)
            intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, false)
            context.run {
                if (this is AppCompatActivity) {
                    startActivityForResult(intent, REQUEST_UPDATE_INFO)
                }
            }
        }
    }

    private val userViewModel by lazy {
        UserViewModel.UserViewModelFactory((application as SAApplication).databaseRepository).create(UserViewModel::class.java)
    }

    /**
     * 只有从`我的`页面中进入的个人主页才是可修改的
     */
    private val isModifiable by lazy {
        intent.getBooleanExtra(ConstantUtil.KEY_INFO_MODIFIABLE, false)
    }

//    private val token by lazy {
//        (application as Application).getCachedToken()
//    }

    private var userInfo: DomainUserInfo.DataBean? = null

    private val originBackgroundHeight by lazy {
        user_background.layoutParams.height
    }

    /**
     * 判断是否是自己的个人主页
     */
    private var isMine: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar)

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_UPDATE_MY) { // 修改用户信息返回
                    (data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean)?.let {
                        intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
                        setUserInfo(userInfo)
                        data.putExtra(ConstantUtil.KEY_UPDATED, true)
                        setResult(Activity.RESULT_OK, data)
                    }
                }
            }
        }
    }

    private fun init() {
        BarUtils.setStatusBarLightMode(this@UserActivity, !isDarkTheme)
        BarUtils.setNavBarLightMode(this@UserActivity, !isDarkTheme)

        // 获取传递进来的userID，若有，则代表这不是从 `我的` 页面进入的个人主页
        val userID = intent.getIntExtra(ConstantUtil.KEY_USER_ID, -1)
        if (userID != -1) { // 如果传了userID，说明这个个人主页是别人的，去获取用户信息
            userViewModel.getUserInfo(userID, true).observe(this) {
                if (it != null) {
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, it)
                }
                setUserInfo(it)
            }
        } else {
            // 没传userID说明是我自己的页面，直接获取我的信息
            setUserInfo((application as SAApplication).getCachedMyInfo())
        }

        user_background_over_drag_header.setOnHeaderOverDragEventListener(this)
        user_avatar.setOnClickListener(this)
        user_more_selling.setOnClickListener(this)
        user_more_posts.setOnClickListener(this)
    }

    /**
     * 使用用户信息装填ui界面
     * 在 用户第一次进入页面时 以及 用户修改信息后返回时调用
     */
    private fun setUserInfo(userInfo: DomainUserInfo.DataBean?) {
        this.userInfo = userInfo
        if (userInfo == null) {
            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
            return
        }

        user_avatar.setActualImageResource(R.drawable.placeholder_round)
        user_background.setImageResource(R.drawable.logo_placeholder)

        ImageUtil.loadRoundImage(user_avatar,
                ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(userInfo.userAvatar),
                R.drawable.placeholder_round)

        ImageUtil.loadImage(user_background, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(userInfo.userAvatar),
                R.drawable.logo_placeholder)

        userInfo.createtime.let { millis ->
            if (millis == -1L) return
            val calendar = Calendar.getInstance().also { it.timeInMillis = millis }
            user_join_time.text = getString(
                    R.string.userJoinTime,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
        }
        description.text = getString(R.string.lastActiveTime, userInfo.lastLoginTime)
        userName.text = userInfo.userName
        user_more_selling.text = userInfo.userGoodsOnSaleCount.toString()
        user_more_posts.text = userInfo.userContactCount.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isModifiable) {
            menuInflater.inflate(R.menu.user_personal, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.modify_info -> {
                UserModifyInfoActivity.start(this)
                return isModifiable
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            // 用户头像
            R.id.user_avatar -> {
                XPopup.Builder(this@UserActivity)
                        .asImageViewer(v as ImageView,
                                ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(userInfo?.userAvatar),
                                false, -1, -1, ScreenUtils.getAppScreenWidth(),
                                true, getColor(R.color.blackAlways), ImageLoader())
                        .show()
            }

            // 在售
            R.id.user_more_selling -> {
                SSBActivity.start(this@UserActivity, userInfo?.userId, 0, isModifiable || isMine)
            }

            // 帖子
            R.id.user_more_posts -> {
                Toast.makeText(this, "功能尚在开发", Toast.LENGTH_SHORT).show()
                // TODO: 2020/12/5 点击进入用户发表的帖子
            }
        }
    }

    /**
     * 在拉动页面时背景图
     * 随着手指下滑而放大
     */
    override fun onOverDragging(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        if (offset >= animOffset) {
            // 下拉到一定程度放大并移动图片
            val extraOffset = offset - animOffset
            val width = (originBackgroundHeight + extraOffset) / originBackgroundHeight * originBackgroundWidth
            user_background.layoutParams = ConstraintLayout.LayoutParams(width, originBackgroundHeight + extraOffset)
        }
    }


}