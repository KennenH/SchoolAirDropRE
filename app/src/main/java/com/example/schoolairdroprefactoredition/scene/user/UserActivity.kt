package com.example.schoolairdroprefactoredition.scene.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainBaseUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.components.OverDragEventHeader
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil.ImageLoader
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import com.example.schoolairdroprefactoredition.viewmodel.UserViewModel
import com.lxj.xpopup.XPopup
import javadz.beanutils.BeanUtils
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : ImmersionStatusBarActivity(), View.OnClickListener, OverDragEventHeader.OnHeaderOverDragEventListener {

    companion object {
        /**
         * 背景图初始宽度即为屏幕宽
         */
        val originBackgroundWidth = ScreenUtils.getAppScreenWidth()

        /**
         * 拉动这么多距离的时候会使背景
         */
        const val animOffset = 40

        /**
         * 请求码
         * 请求更新用户信息
         */
        const val REQUEST_UPDATE = 520

        /**
         * 从'我的'页面中打开个人信息
         * 直接传递我的信息即可
         * @param myInfo 当前登录账号的个人信息
         * @param token  当前登录账号的token
         */
        fun start(context: Context?, token: DomainToken?, myInfo: Any?) {
            if (context == null || myInfo == null) return

            val myBaseInfo = DomainBaseUserInfo()
            BeanUtils.copyProperties(myBaseInfo, myInfo)

            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_TOKEN, token)
            intent.putExtra(ConstantUtil.KEY_USER_BASE_INFO, myBaseInfo)
            intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, true)
            if (context is AppCompatActivity) context.startActivityForResult(intent, REQUEST_UPDATE)
        }

        /**
         * 从任何其他非'我的'的页面打开本页面
         * 需要一个userID且无论该页面是否查看的是
         * 本人的个人主页，都无法修改个人信息
         * @param userID    卖家uID
         * @param token     当前账号token
         */
        fun start(context: Context?, userID: Int, token: DomainToken?) {
            if (context == null || userID == -1) return

            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_TOKEN, token)
            intent.putExtra(ConstantUtil.KEY_USER_ID, userID)
            intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, false)
            context.startActivity(intent)
        }
    }

    private val userViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    /**
     * 只有从`我的`页面中进入的个人主页才是可修改的
     */
    private val isModifiable by lazy {
        intent.getBooleanExtra(ConstantUtil.KEY_INFO_MODIFIABLE, false)
    }

    private val token by lazy {
        intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken
    }

    private var userInfo: DomainBaseUserInfo? = null

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
                if (requestCode == REQUEST_UPDATE) {
                    userInfo = data.getSerializableExtra(ConstantUtil.KEY_USER_BASE_INFO) as DomainBaseUserInfo?
                    intent.putExtra(ConstantUtil.KEY_USER_BASE_INFO, userInfo)

                    LogUtils.d(userInfo.toString())
                    setUserInfo(userInfo)

                    data.putExtra(ConstantUtil.KEY_UPDATED, true)
                    setResult(Activity.RESULT_OK, data)
                }
            }
        }
    }

    private fun init() {
        BarUtils.setStatusBarLightMode(this@UserActivity, !isDarkTheme)
        BarUtils.setNavBarLightMode(this@UserActivity, !isDarkTheme)

        userInfo = intent.getSerializableExtra(ConstantUtil.KEY_USER_BASE_INFO) as DomainBaseUserInfo?
        val sellerID = intent.getIntExtra(ConstantUtil.KEY_USER_ID, -1)

        if (userInfo == null && sellerID != -1) {
            userViewModel.getUserBaseInfoByID(sellerID).observe(this) {
                intent.putExtra(ConstantUtil.KEY_USER_BASE_INFO, it)
                setUserInfo(it)
            }
        } else if (userInfo != null && sellerID == -1) {
            setUserInfo(userInfo)
        } else {
            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
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
    private fun setUserInfo(baseUserInfo: DomainBaseUserInfo? = null) {
        userInfo = baseUserInfo
                ?: intent.getSerializableExtra(ConstantUtil.KEY_USER_BASE_INFO) as DomainBaseUserInfo?

        ImageUtil.loadRoundImage(user_avatar,
                ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + userInfo?.user_img_path,
                R.drawable.placeholder_round)

        ImageUtil.loadImage(user_background, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + userInfo?.user_img_path,
                R.drawable.logo_placeholder)

        userName.text = userInfo?.uname
        user_more_posts.text = "0"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_personal, menu)
        menu?.getItem(0)?.isVisible = isModifiable
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.modify_info -> {
                UserModifyInfoActivity.start(this, token, userInfo)
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
                                ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + userInfo?.user_img_path,
                                false, -1, -1, 50,
                                true, ImageLoader())
                        .show()
            }

            // 在售
            R.id.user_more_selling -> {
                SSBActivity.start(this@UserActivity, token, userInfo, 0, isModifiable || isMine)
            }

            // 帖子
            R.id.user_more_posts -> {
                // TODO: 2020/12/5 查看该用户帖子
            }
        }
    }

    /**
     * 在拉动页面时背景图随着手指下滑距离而改变大小
     */
    override fun onOverDragging(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        if (offset >= animOffset) {
            val extraOffset = offset - animOffset
            val width = (originBackgroundHeight + extraOffset) / originBackgroundHeight * originBackgroundWidth
            user_background.layoutParams = ConstraintLayout.LayoutParams(width, originBackgroundHeight + extraOffset)
        }
    }
}