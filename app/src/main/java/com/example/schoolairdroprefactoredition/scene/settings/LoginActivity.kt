package com.example.schoolairdroprefactoredition.scene.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.AnimUtil
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : ImmersionStatusBarActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private val viewModel by lazy {
        ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
    }

    private var isLogging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val info = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO)

        // 已登录时
        if (info != null) {
            setContentView(R.layout.activity_logged_in)
            val phone = (info as DomainUserInfo.DataBean).ualipay

//          todo 发布时取消注释下面这句话
            // final String priPhone = phone.substring(0, 3).concat("****").concat(phone.substring(7));
            val isPri = booleanArrayOf(true)
            userName.text = phone
            userName.setOnClickListener {
                userName.text = if (isPri[0]) phone else phone
                isPri[0] = !isPri[0]
            }
            close.setOnClickListener {
                finish()
                AnimUtil.activityExitAnimDown(this)
            }
            ClickUtils.applyPressedViewAlpha(close, 0.6f)
        } else {
            setContentView(R.layout.activity_login)
            viewModel.getLoadState().observe(this, { state: LoadState ->
                if (state === LoadState.LOADING) showLoading() else if (state === LoadState.ERROR) {
                    isLogging = false
                    dismissLoading { DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.errorLogin) }
                }
            })
            login_with_alipay.isEnabled = false
            cancel.setOnClickListener(this)
            checkbox.setOnCheckedChangeListener(this)
            checkbox.isSelected = false
            login_with_alipay.setOnClickListener(this)
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
        AnimUtil.activityExitAnimDown(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_with_alipay -> {
                // 请求公钥与sessionID
                showLoading()
                if (NetworkUtils.isConnected()) {
                    if (!isLogging) {
                        isLogging = true
                        loginWithAlipay()
                    }
                } else {
                    dismissLoading { DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_NETWORK, R.string.dialogNetWorkError) }
                }
            }
            R.id.cancel -> {
                setResult(RESULT_CANCELED)
                finish()
                AnimUtil.activityExitAnimDown(this)
            }
        }
    }

    /**
     * 登录网络请求
     * 两次请求获取token信息
     * 最后使用token换取用户信息
     */
    private fun loginWithAlipay() {
        viewModel.getPublicKey().observe(this, { key ->
            if (key != null) {
                viewModel.authorizeWithAlipayID(key.cookie,
                        intent.getStringExtra(ConstantUtil.KEY_ALIPAY_FOR_LOGIN)
                                ?: AppConfig.USER_ID, key.public_key)
                        .observe(this, { token ->
                            if (token != null) {
                                dismissLoading()

                                // todo comment this when release
                                LogUtils.d("token -- > $token")

                                // token
                                intent.putExtra(ConstantUtil.KEY_AUTHORIZE, token)
                                userInfoWithToken()
                            }
                        })
            }
        })
    }

    /**
     * 使用token获取用户信息
     */
    private fun userInfoWithToken() {
        val token = intent.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE)
        if (token != null) {
            showLoading()
            viewModel.getUserInfo((token as DomainAuthorize).access_token).observe(this, { info ->
                if (info != null) {
                    isLogging = false
                    val userInfo = info.data[0]
                    // token换取的user info
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
                    setResult(RESULT_OK, intent)
                    dismissLoading {
                        finish()
                        AnimUtil.activityExitAnimDown(this)
                    }
                }
            })
        } else DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        login_with_alipay.isEnabled = isChecked
    }

    companion object {
        const val LOGIN = 1212 // 请求码 网络登录请求

        /**
         * 尚未登录时
         */
        fun startForLogin(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }

        /**
         * 尚未登录且带着想要登录的alipayId进行登录请求
         *
         * @param alipayID 要登陆的账号
         */
        @JvmStatic
        fun startForLogin(context: Context, alipayID: String) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_ALIPAY_FOR_LOGIN, alipayID)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }

        /**
         * 已登录时
         */
        @JvmStatic
        fun startAfterLogin(context: Context, userInfo: DomainUserInfo.DataBean?) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_USER_INFO, userInfo)
            context.startActivity(intent)
            if (context is AppCompatActivity) {
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }
}