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
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.AnimUtil
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : ImmersionStatusBarActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, SAApplication.OnApplicationLoginListener {

    companion object {
        const val LOGIN = 39726 // 请求码 网络登录请求

        fun start(context: Context?) {
            val intent = Intent(context, LoginActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
    }

    /**
     * 防止正在登录时重复发起登录请求
     *
     * 只需在登录失败时重置为false，成功后将自动关闭页面，无需将其重置
     */
    private var isLogging = false

    /**
     * 是否正在执行finish部分的代码
     */
    private var isExiting = false

    /**
     * 防止登录成功后用户信息获取失败再次登录，当有token的时候直接获取用户信息即可
     */
    private var token: DomainToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = (application as SAApplication).getCachedToken()
        val info = (application as SAApplication).getCachedMyInfo()
        // 已登录时
        if (info != null) {
            setContentView(R.layout.activity_logged_in)

            val phone = AppConfig.USER_ALIPAY
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
            (application as? SAApplication)?.addOnApplicationLoginListener(this)
            login_with_alipay.isEnabled = false
            cancel.setOnClickListener(this)
            checkbox.setOnCheckedChangeListener(this)
            checkbox.isChecked = false
            login_with_alipay.setOnClickListener(this)
        }
    }

    /**
     * 登录失败，将正在登录的标志置回false
     */
    private fun loginError() {
        isLogging = false
        dismissLoading { DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.errorLogin) }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
        AnimUtil.activityExitAnimDown(this)
    }

    /**
     * 防止网络意外时重复登录逻辑：
     *
     * .网络正常时点击登录，显示loading                 进入登录请求
     *   [LoginActivity.showLoading]  ---- >  [LoginActivity.loginWithAlipay]  ---- >
     * . 网络出现异常，按下返回使loading消失    网络恢复，再次按下登录，但正在登录标识符将请求拦截
     *   [LoginActivity.dismissLoading] ---- >  [LoginActivity.showLoading]
     *
     *   此时只会显示loading而不会重复请求
     *   [com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry]
     *   会在此时进行3次请求重试，从而完成登录
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_with_alipay -> {
                // 暂时禁用提交按钮防止短时间内提交多次
                v.isEnabled = false

                // 开始转圈圈，这个调用里一旦出错中断了，必须调用loginError来提示用户登录请求中断
                showLoading {
                    // 如果没网直接error
                    if (NetworkUtils.isConnected()) {
                        // 这里是唯一能将这个变量置为true的地方，失败或者功能之后一定要调用loginError
                        // 否则用户再点也没办法登录了
                        if (!isLogging) {
                            isLogging = true
                            loginWithAlipay()
                        }
                    } else {
                        loginError()
                    }
                }

                // 已经在网络请求了，可以放开提交按钮了
                v.isEnabled = true
            }

            R.id.cancel -> {
                setResult(RESULT_CANCELED)
                if (!isExiting) {
                    isExiting = true
                    finish()
                    AnimUtil.activityExitAnimDown(this)
                }
            }
        }
    }

    /**
     * 登录网络请求
     * 两次请求获取token信息
     * 最后使用token换取用户信息
     */
    @Synchronized
    private fun loginWithAlipay() {
        if (token != null) {
            // 如果之前某种情况登录成功但是用户信息获取失败，直接获取用户信息即可
            getUserInfoWithToken(token)
        } else {
            // 页面中没有token
            viewModel.getPublicKey().observeOnce(this, { publicKey ->
                if (publicKey != null) {
                    viewModel.authorizeWithAlipayID(
                            AppConfig.USER_ALIPAY, publicKey.public_key).observeOnce(this, { authorizedToken ->
                        if (authorizedToken != null) {
                            token = authorizedToken

                            // todo comment this when release
                            LogUtils.d("authorizedToken -- > $authorizedToken")

                            // 登录成功了，也不需要把isLogging置回false了
                            getUserInfoWithToken(authorizedToken)
                        } else {
                            loginError()
                        }
                    })
                } else {
                    loginError()
                }
            })
        }
    }

    /**
     * 使用token获取用户信息
     */
    private fun getUserInfoWithToken(token: DomainToken?) {
        // 要是登录了发现token没了那也没救了
        if (token != null) {
            viewModel.getMyInfo(token.access_token).observeOnce(this@LoginActivity, {
                if (it != null) {
                    // token换取的user info
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, it)
                    intent.putExtra(ConstantUtil.KEY_TOKEN, token)
                    setResult(RESULT_OK, intent)

                    if (!isExiting) {
                        isExiting = true
                        dismissLoading()
                        finish()
                        AnimUtil.activityExitAnimDown(this@LoginActivity)
                    }
                } else {
                    dismissLoading {
                        DialogUtil.showCenterDialog(this@LoginActivity, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                    }
                }
            })
        } else {
            loginError()
        }
    }

    /**
     * 用户协议勾选事件回调
     */
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        login_with_alipay.isEnabled = isChecked
    }

    /**
     * app登陆状态改变回调
     *
     * 当进入页面的时候未登录但是在用户再次按下登录按钮或者登录事件回调之前
     */
    override fun onApplicationLoginStateChange(isLogged: Boolean) {
        if (!isExiting) {
            isExiting = true
            dismissLoading()
            finish()
            AnimUtil.activityExitAnimDown(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as? SAApplication)?.removeOnApplicationLoginListener(this)
    }
}