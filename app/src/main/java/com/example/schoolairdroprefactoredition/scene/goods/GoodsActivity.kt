package com.example.schoolairdroprefactoredition.scene.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.domain.*
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.components.ButtonRight
import com.example.schoolairdroprefactoredition.ui.components.ButtonLeft
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo.OnUserInfoClickListener
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import kotlinx.android.synthetic.main.activity_goods.*
import kotlinx.android.synthetic.main.activity_logged_in.*
import java.net.HttpURLConnection

class GoodsActivity : ImmersionStatusBarActivity(), ButtonLeft.OnButtonClickListener, OnUserInfoClickListener, ButtonRight.OnButtonClickListener {

    companion object {
        const val KEY_IS_FROM_SELLING = "fromSelling?"

        /**
         * @param goodsID 物品id
         */
        fun start(context: Context,
                  goodsID: Int,
                  isFromSelling: Boolean) {
            val intent = Intent(context, GoodsActivity::class.java)
            intent.apply {
                putExtra(ConstantUtil.KEY_GOODS_ID, goodsID)
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
        GoodsViewModel.GoodsViewModelFactory((application as SAApplication).databaseRepository).create(GoodsViewModel::class.java)
    }

    private val goodsID by lazy {
        intent.getIntExtra(ConstantUtil.KEY_GOODS_ID, -1)
    }

    /**
     * 物品详细信息
     */
    private var goodsInfo: DomainGoodsAllDetailInfo.Data? = null

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

        goodsViewModel.getGoodsAllDetailByID(goodsID).observeOnce(this) {
            goods_info_container.stopShimming()
            if (it != null) {
                when (it.code) {
                    ConstantUtil.HTTP_OK -> {
                        goodsInfo = it.data
                        goods_info_container.setData(goodsInfo)
                        goods_button_right.setFavor(it.data.goods_is_favored)
                        validateInfo()
                    }
                    ConstantUtil.HTTP_NOT_FOUND -> {
                        DialogUtil.showCenterDialog(this@GoodsActivity, DialogUtil.DIALOG_TYPE.FAILED, R.string.goodsDeleted)
                    }
                    else -> {
                        DialogUtil.showCenterDialog(this@GoodsActivity, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
                    }
                }
            } else {
                DialogUtil.showCenterDialog(this@GoodsActivity, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
            }
        }
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
        // 若从用户信息页面进入的在售页面，则隐藏卖家信息，原因见方法本身
        if (intent.getBooleanExtra(KEY_IS_FROM_SELLING, false)) {
            goods_info_container.hideSellerInfo()
        }
        showActionButtons()
    }

    /**
     * 刷新底部动作按钮的显示
     */
    private fun showActionButtons() {
        val isGoodsMine = (application as? SAApplication)?.getCachedMyInfo()?.userId == goodsInfo?.seller?.user_id
        goods_button_left.visibility = if (!isGoodsMine) View.VISIBLE else View.GONE
        goods_button_right.visibility = if (!isGoodsMine) View.VISIBLE else View.GONE
        goods_info_container.showBottom(!isGoodsMine)
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
                // 在物品页面登录后返回
                if (data != null) {
                    (application as SAApplication).cacheMyInfoAndToken(
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

    override fun onUserInfoClick(view: View?) {
        UserActivity.start(this@GoodsActivity, goodsInfo?.seller?.user_id)
    }


    /**
     * 点击左下角的聊天按钮
     */
    override fun onLeftButtonClick() {
        if ((application as SAApplication).getCachedToken() == null) {
            login()
        } else {
            if (goodsInfo?.seller?.user_id != null) {
                val counterpartInfo = DomainUserInfo.DataBean()
                counterpartInfo.userId = goodsInfo?.seller?.user_id!!
                counterpartInfo.userName = goodsInfo?.seller?.user_name
                counterpartInfo.userAvatar = goodsInfo?.seller?.user_avatar
                ChatActivity.start(this@GoodsActivity, counterpartInfo)
            }
        }
    }

    /**
     * 点击右下角的收藏按钮
     */
    override fun onRightButtonClick() {
        if (goodsInfo?.goods_id != null &&
                goodsInfo?.seller?.user_id != null &&
                goodsInfo?.goods_name != null &&
                goodsInfo?.goods_cover_image != null &&
                goodsInfo?.goods_price != null &&
                goodsInfo?.goods_is_bargain != null &&
                goodsInfo?.goods_is_secondHand != null) {
            showLoading()
            goodsViewModel.toggleGoodsFavorite(Favorite(
                    goodsInfo?.goods_id!!,
                    goodsInfo?.seller?.user_id!!,
                    goodsInfo?.goods_name!!,
                    goodsInfo?.goods_cover_image!!,
                    goodsInfo?.goods_price.toString(),
                    goodsInfo?.goods_is_bargain!!,
                    goodsInfo?.goods_is_secondHand!!
            )).observeOnce(this) {
                dismissLoading {
                    goods_button_right.toggleFavor()
                    if (it) {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAVOR, R.string.favorDone)
                    } else {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAVOR, R.string.unfavorDone)
                    }
                }
            }
        }
    }
}