package com.example.schoolairdroprefactoredition.scene.goods

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.SheetQuoteBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.main.my.MyViewModel
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.components.ButtonDouble
import com.example.schoolairdroprefactoredition.ui.components.ButtonSingle
import com.example.schoolairdroprefactoredition.ui.components.GoodsInfo.OnUserInfoClickListener
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_goods.*

class GoodsActivity : ImmersionStatusBarActivity(), ButtonSingle.OnButtonClickListener, OnUserInfoClickListener, ButtonDouble.OnButtonClickListener {

    companion object {
        const val KEY_IS_FROM_SELLING = "fromSelling?"

        /**
         * 本页面中不存在KEY_USER_INFO
         * 1、账号本人信息由KEY_AUTHORIZE从服务器获取，但是它可为空即在未登录状态查看物品信息
         * 2、物品以及卖家信息由KEY_GOODS_INFO持有，不可以为空
         *
         * @param token         验证信息
         * @param goodsInfo     物品信息,包含卖家信息
         * @param isFromSelling 详见{@link GoodsInfo#hideSellerInfo()}
         */
        fun start(context: Context,
                  token: DomainToken?,
                  goodsInfo: DomainGoodsInfo.DataBean?,
                  isFromSelling: Boolean) {
            if (goodsInfo == null) return

            val intent = Intent(context, GoodsActivity::class.java)
            intent.apply {
                putExtra(ConstantUtil.KEY_GOODS_INFO, goodsInfo)
                putExtra(ConstantUtil.KEY_TOKEN, token)
                putExtra(KEY_IS_FROM_SELLING, isFromSelling)
            }

            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
            } else {
                context.startActivity(intent)
            }
        }
    }

    private val myViewModel by lazy {
        ViewModelProvider(this@GoodsActivity).get(MyViewModel::class.java)
    }

    private val goodsViewModel by lazy {
        ViewModelProvider(this@GoodsActivity).get(GoodsViewModel::class.java)
    }

    private var dialog: BottomSheetDialog? = null

    private var isNotMine = false // 是否是我的物品

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)
        init()
    }

    private fun init() {
        setBar()
        setSupportActionBar(goods_toolbar)

        goods_button_left.visibility = View.GONE
        goods_button_right.visibility = View.GONE

        goods_info_container.setOnUserInfoClickListener(this)
        goods_button_left.setOnButtonClickListener(this)
        goods_button_right.setOnButtonClickListener(this)

        goodsViewModel.mQuoteState.observe(this, {
            when (it) {
                LoadState.SUCCESS -> {
                    dismissLoading {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successQuote)
                    }
                }
                LoadState.ERROR -> {
                    dismissLoading {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
                    }
                }
                else -> {
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
     */
    private fun validateInfo() {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        val goodsInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO) as DomainGoodsInfo.DataBean

        // 若从用户信息页面进入的在售页面，则隐藏卖家信息，原因见方法本身
        if (intent.getBooleanExtra(KEY_IS_FROM_SELLING, false)) {
            goods_info_container.hideSellerInfo()
        }

        if (token != null && goodsInfo.seller_info != null) {
            checkIfItemFavored()
            myViewModel.getUserInfo((token as DomainToken).access_token).observe(this, {
                isNotMine = it.data[0].uid != goodsInfo.seller_info.uid
                if (isNotMine) {
                    goods_button_left.visibility = View.VISIBLE
                    goods_button_right.visibility = View.VISIBLE
                    goods_info_container.showBottom()
                }
                goods_info_container.stopShimming()
            })
        } else {
            goods_info_container.stopShimming()
        }

        goods_info_container.setData(goodsInfo)
    }

    /**
     * 检查物品是否已经被收藏
     */
    private fun checkIfItemFavored() {
    }

    /**
     * 在未登录时打开物品页面并进行操作时
     */
    private fun login() {
        LoginActivity.startForLogin(this@GoodsActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                if (data != null) {
                    intent.apply {
                        removeExtra(ConstantUtil.KEY_TOKEN)
                        removeExtra(ConstantUtil.KEY_USER_INFO)
                        putExtra(ConstantUtil.KEY_TOKEN,
                                data.getSerializableExtra(ConstantUtil.KEY_TOKEN))
                        putExtra(ConstantUtil.KEY_USER_INFO,
                                data.getSerializableExtra(ConstantUtil.KEY_USER_INFO))
                    }

                    validateInfo()
                    setResult(Activity.RESULT_OK, data)
                }
            }
        }
    }

    /**
     * 发起报价
     */
    private fun quote(input: String) {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        val goodsInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO)
        if (token != null) {
            if (goodsInfo != null && isNotMine) {
                showLoading {
                    goodsViewModel.quoteRequest((token as DomainToken).access_token, (goodsInfo as DomainGoodsInfo.DataBean).goods_id, input).observe(this@GoodsActivity, {
                        if (it != null) {
                            dismissLoading {
                                dialog?.dismiss()
                                DialogUtil.showCenterDialog(this@GoodsActivity,
                                        DialogUtil.DIALOG_TYPE.SUCCESS,
                                        R.string.successQuote)
                            }
                        }
                    })
                }
            } else {
                dialog?.dismiss()
                DialogUtil.showCenterDialog(this@GoodsActivity, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
            }
        } else {
            login()
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
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        if (token == null) {
            login()
        } else {
            ChatActivity.start(this@GoodsActivity)
        }
    }

    override fun onUserInfoClick(view: View?) {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        val goodsInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO)
        if (token != null && goodsInfo != null)
            UserActivity.start(this@GoodsActivity,
                    false,
                    token as DomainToken,
                    (goodsInfo as DomainGoodsInfo.DataBean).seller_info)
    }

    override fun onLeftButtonClick() {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        if (token == null) {
            login()
        } else {
            goods_button_right.toggleFavor()
        }
    }

    override fun onRightButtonClick() {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        val goodsInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO)

        if (token == null) {
            login()
        } else {
            if (dialog == null) {
                dialog = BottomSheetDialog(this@GoodsActivity)
                val binding = SheetQuoteBinding.inflate(LayoutInflater.from(this@GoodsActivity))
                dialog?.setContentView(binding.root)

                try {
                    binding.apply {
                        warning.visibility = View.GONE

                        if ((goodsInfo as DomainGoodsInfo.DataBean).goods_is_brandNew != 1) {
                            secondHandTip.visibility = View.VISIBLE
                        } else {
                            secondHandTip.visibility = View.GONE
                        }

                        if (goodsInfo.goods_is_quotable == 1) {
                            quotePrice.filters = arrayOf<InputFilter>(DecimalFilter())
                            quotePrice.setOnEditorActionListener { v, actionId, event ->
                                quotePrice.clearFocus()
                                KeyboardUtils.hideSoftInput(v)
                                true
                            }
                            quotePrice.setOnFocusChangeListener { v, hasFocus ->
                                if (hasFocus && warning.visibility == View.VISIBLE) {
                                    AnimUtil.collapse(warning)
                                }
                            }
                            notQuotableTip.visibility = View.GONE
                        } else {
                            notQuotableTip.visibility = View.VISIBLE
                            quotePrice.setText(goodsInfo.goods_price)
                            quotePrice.isEnabled = false
                        }

                        originPrice.text = getString(R.string.priceRMB, goodsInfo.goods_price)
                        title.setOnClickListener {
                            quotePrice.clearFocus()
                            KeyboardUtils.hideSoftInput(it)
                        }
                        notQuotableTip.setOnClickListener {
                            quotePrice.clearFocus()
                            KeyboardUtils.hideSoftInput(it)
                        }
                        secondHandTip.setOnClickListener {
                            quotePrice.clearFocus()
                            KeyboardUtils.hideSoftInput(it)
                        }

                        cancel.setOnClickListener { dialog?.dismiss() }
                        confirm.setOnClickListener {
                            if (quotePrice.text.toString() == "") {
                                if (warning.visibility != View.VISIBLE) {
                                    AnimUtil.expand(warning)
                                } else {
                                    AnimUtil.viewBlink(this@GoodsActivity, warning, R.color.colorPrimaryRed, R.color.white)
                                }
                            } else {
                                quote(quotePrice.text.toString())
                            }
                        }

                        quotePrice.clearFocus()

                        val view1 = dialog?.delegate?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                        if (view1 != null) {
                            view1.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent, theme)
                            val bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(view1)
                            bottomSheetBehavior.skipCollapsed = true
                            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                                override fun onStateChanged(bottomSheet: View, newState: Int) {
                                    when (newState) {
                                        BottomSheetBehavior.STATE_HIDDEN -> {
                                            dialog?.dismiss()
                                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                        }
                                        BottomSheetBehavior.STATE_DRAGGING -> {
                                            quotePrice.clearFocus()
                                            KeyboardUtils.hideSoftInput(bottomSheet)
                                        }
                                    }
                                }

                                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                                }
                            })
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            dialog?.show()
        }
    }
}