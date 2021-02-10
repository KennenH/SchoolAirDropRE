package com.example.schoolairdroprefactoredition.scene.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.HomeGoodsListInfo
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo.OnUserInfoClickListener
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_goods.*
import kotlinx.android.synthetic.main.activity_logged_in.*

class GoodsActivity : ImmersionStatusBarActivity(), ButtonSingle.OnButtonClickListener, OnUserInfoClickListener, ButtonDouble.OnButtonClickListener {

    companion object {
        const val KEY_IS_FROM_SELLING = "fromSelling?"

        /**
         * 1、账号本人信息由KEY_AUTHORIZE从服务器获取，但是它可为空即在未登录状态查看物品信息
         * 2、物品信息由KEY_GOODS_ID从服务器获取，不可以为空
         *
         * @param goodsBaseInfo 物品基本信息
         * @param isFromSelling 详见{@link GoodsInfo#hideSellerInfo()}
         */
        fun start(context: Context,
                  goodsBaseInfo: HomeGoodsListInfo.DataBean,
                  isFromSelling: Boolean) {
            val intent = Intent(context, GoodsActivity::class.java)
            intent.apply {
                putExtra(ConstantUtil.KEY_GOODS_BASE_INFO, goodsBaseInfo)
                putExtra(KEY_IS_FROM_SELLING, isFromSelling)
            }

            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
            } else {
                context.startActivity(intent)
            }
        }
    }

    private val goodsViewModel by lazy {
        ViewModelProvider(this@GoodsActivity).get(GoodsViewModel::class.java)
    }

    private var dialog: BottomSheetDialog? = null

    /**
     * 是否不是我的物品
     */
    private var isNotMine = false

    private val goodsInfo by lazy {
        intent.getSerializableExtra(ConstantUtil.KEY_GOODS_BASE_INFO) as HomeGoodsListInfo.DataBean
    }

    /**
     * 物品详细信息
     */
    private var goodsDetailInfo: GoodsDetailInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)
        init()
    }

    private fun init() {
        setBar()
        setSupportActionBar(goods_toolbar)

        goods_info_container.setOnUserInfoClickListener(this)
        goods_button_left.setOnButtonClickListener(this)
        goods_button_right.setOnButtonClickListener(this)

        goodsViewModel.mQuoteState.observe(this, {
            when (it) {
                LoadState.ERROR -> {
                    goods_info_container.stopShimming()
                    DialogUtil.showCenterDialog(this@GoodsActivity, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
                }
            }
        })

        validateInfo()
    }

    /**
     * 设置底部栏和状态样式
     */
    private fun setBar() {
        StatusBarUtil.setTranslucentForImageView(this@GoodsActivity, 0, goods_toolbar)
        BarUtils.setStatusBarLightMode(this@GoodsActivity, !isDarkTheme)
        BarUtils.setNavBarLightMode(this@GoodsActivity, !isDarkTheme)
        status_bar_overlay.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
    }

    /**
     * 有效化页面状态等信息
     *
     * 用goodsInfo获取卖家信息，若卖家id与当前用户id一致
     * 则判定为是自己的物品，则隐藏底部交互按钮
     */
    private fun validateInfo() {
        val myInfo = (application as Application).getCachedMyInfo()

        // 若从用户信息页面进入的在售页面，则隐藏卖家信息，原因见方法本身
        if (intent.getBooleanExtra(KEY_IS_FROM_SELLING, false)) {
            goods_info_container.hideSellerInfo()
        }

        goodsViewModel.getGoodsDetailByID(goodsInfo.goods_id)
                .observe(this@GoodsActivity) {
                    goodsDetailInfo = it
                    isNotMine = it.data[0].uid != myInfo?.userId

                    showActionButtons(isNotMine)
                    goods_info_container.setData(goodsInfo, it.data[0])
                    goods_button_right.setFavor(it.isIs_in_favor)
                    goods_info_container.stopShimming()
                }
    }

    /**
     * 显示底部动作按钮
     */
    private fun showActionButtons(show: Boolean) {
        goods_button_left.visibility = if (show) View.VISIBLE else View.GONE
        goods_button_right.visibility = if (show) View.VISIBLE else View.GONE
        goods_info_container.showBottom(show)
    }

    /**
     * 在未登录时打开物品页面并进行操作时
     */
    private fun login() {
        LoginActivity.start(this@GoodsActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                if (data != null) {
                    (application as Application).cacheMyInfoAndToken(
                            data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean,
                            data.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken)

                    validateInfo()
                    setResult(Activity.RESULT_OK, data)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onButtonClick() {
        if ((application as Application).getCachedToken() == null) {
            login()
        } else {
            val userInfo = goodsDetailInfo?.data?.get(0)
            val user = DomainUserInfo.DataBean()
            if (userInfo != null) {
                user.userName = goodsInfo.seller_info
                user.userAvatar = userInfo.seller_img
                user.userId = userInfo.uid
                ChatActivity.start(this@GoodsActivity, user)
            }
        }
    }

    override fun onUserInfoClick(view: View?) {
        val userID = goodsDetailInfo?.data?.get(0)?.uid

        if (userID != null) {
            UserActivity.start(this@GoodsActivity, userID)
        } else {
            LogUtils.d(goodsDetailInfo.toString())
        }
    }

    override fun onLeftButtonClick() {
        if ((application as Application).getCachedToken() == null) {
            login()
        } else {
            goods_button_right.toggleFavor()
        }
    }

    override fun onRightButtonClick() {

    }
}