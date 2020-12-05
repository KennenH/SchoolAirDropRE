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
import com.blankj.utilcode.util.ScreenUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.main.my.MyViewModel
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.components.OverDragEventHeader
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil.ImageLoader
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import com.lxj.xpopup.XPopup
import javadz.beanutils.BeanUtils
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : ImmersionStatusBarActivity(), View.OnClickListener, OverDragEventHeader.OnHeaderOverDragEventListener {

    companion object {
        val originBackgroundWidth = ScreenUtils.getAppScreenWidth()
        const val animOffset = 40 // 拉动这么多距离时将会触发背景图放大效果

        const val REQUEST_UPDATE = 520 // 请求码 修改用户个人信息

        /**
         * 两个参数的Bean类需要 直接 包含用户基本信息的域和get set方法
         *
         * @param isMyOwnPageAndModifiable 是否可修改
         * 只有在[com.example.schoolairdroprefactoredition.scene.main.my.MyFragment]
         * 中进入自己的个人信息页才可修改
         * @param token                    验证信息
         * @param thisPersonInfo           被访问的这个人的信息
         */
        fun start(context: Context?, isMyOwnPageAndModifiable: Boolean, token: DomainToken?, thisPersonInfo: Any?) {
            if (thisPersonInfo == null) return
            val thisPerson = DomainBaseUserInfo()
            BeanUtils.copyProperties(thisPerson, thisPersonInfo)

            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_TOKEN, token)
            intent.putExtra(ConstantUtil.KEY_USER_INFO, thisPerson)
            intent.putExtra(ConstantUtil.KEY_INFO_MODIFIABLE, isMyOwnPageAndModifiable)
            if (context is AppCompatActivity) context.startActivityForResult(intent, REQUEST_UPDATE)
        }
    }

    private val myViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

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

    private var isMine: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_UPDATE) {
                    userInfo = data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainBaseUserInfo?

                    setUserInfo()

                    data.putExtra(ConstantUtil.KEY_UPDATED, true)
                    setResult(Activity.RESULT_OK, data)
                }
            }
        }
    }

    private fun init() {
        userInfo = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainBaseUserInfo

        setSupportActionBar(toolbar)
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar)
        status_bar_overlay.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())

        myViewModel.getUserInfo(token.access_token).observe(this) {
            if (it?.data?.get(0)?.uid == userInfo?.uid) {
                isMine = true
            }
        }

        user_background_over_drag_header.setOnHeaderOverDragEventListener(this)
        user_avatar.setOnClickListener(this)
        user_more_selling.setOnClickListener(this)
        user_more_posts.setOnClickListener(this)
        user_background.setOnClickListener(this)

        setUserInfo()
    }

    /**
     * 使用用户信息装填ui界面
     * 在 用户第一次进入页面时 以及 用户修改信息后返回时调用
     */
    private fun setUserInfo() {
        ImageUtil.loadRoundImage(user_avatar,
                ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + userInfo?.user_img_path,
                R.drawable.placeholder_round)
        userName.text = userInfo?.uname
        user_more_posts.text = userInfo?.credit_num.toString()
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

            // 背景图片
            R.id.user_background -> {
                // TODO: 2020/12/5 选择背景图片
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