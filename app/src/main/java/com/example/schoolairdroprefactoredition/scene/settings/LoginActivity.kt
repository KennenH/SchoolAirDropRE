package com.example.schoolairdroprefactoredition.scene.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alipay.sdk.app.OpenAuthTask
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.cache.util.UserLoginCacheUtil
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : ImmersionStatusBarActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, SAApplication.OnApplicationLoginListener {

    companion object {
        const val LOGIN = 39726 // 请求码 网络登录请求

        /**
         * 所有在登录页面和主页面之间的可能的经过的页面，都需要实现
         * onActivityResult来帮助登录页传递登录信息
         */
        fun start(context: Context?, actionAfterLogin: Int? = null) {
            val intent = Intent(context, LoginActivity::class.java)
            if (actionAfterLogin != null) {
                intent.putExtra(ConstantUtil.KEY_ACTION_AFTER_LOGIN, actionAfterLogin)
            }
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    private val loginViewModel by lazy {
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
     * OpenAuthTask.OK =  9000               - 调用成功
     * OpenAuthTask.Duplex =  5000           -  3 s 内快速发起了多次支付 / 授权调用。稍后重试即可。
     * OpenAuthTask.NOT_INSTALLED =  4001    - 用户未安装支付宝 App。
     * OpenAuthTask.SYS_ERR =  4000          - 其它错误，如参数传递错误。
     */
    private val openAuthCallback = OpenAuthTask.Callback { resultCode, memo, bundle ->
        when (resultCode) {
            OpenAuthTask.OK -> {
                doLoginAppWithAuthCode(bundle.getString("auth_code"))
            }
            OpenAuthTask.Duplex -> {
                Toast.makeText(this, getString(R.string.actionTooFrequent), Toast.LENGTH_SHORT).show()
            }
            OpenAuthTask.NOT_INSTALLED -> {
                Toast.makeText(this, getString(R.string.AlipayNotFound), Toast.LENGTH_SHORT).show()
            }
            OpenAuthTask.SYS_ERR -> {
                Toast.makeText(this, getString(R.string.unexpectedError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as? SAApplication)?.addOnApplicationLoginListener(this)

        login_with_alipay.isEnabled = false
        checkbox.isChecked = false
        cancel.setOnClickListener(this)
        checkbox.setOnCheckedChangeListener(this)
        login_with_alipay.setOnClickListener(this)
        privacy_protocol.setOnClickListener(this)
        user_protocol.setOnClickListener(this)
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
        AnimUtil.activityExitAnimDown(this)
    }

    /**
     * 登录失败，将正在登录的标志置回false
     *
     * 所有登陆失败分支都必须调用此方法，否则用户在重新打开登录页面之前都不再能够登录
     */
    private fun loginError() {
        isLogging = false
        dismissLoading()
        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.errorLogin)
    }


    /**
     * 跳转支付宝进行app授权
     *
     * 用户授权之后将返回用户alipay id，用以登录
     */
    private fun openAuthScheme() {
        showLoading()

        // 传递给支付宝应用的业务参数
        val bizParams: MutableMap<String, String> = HashMap()
        bizParams["url"] = "https://authweb.alipay.com/auth?auth_type=PURE_OAUTH_SDK&app_id=" + ConstantUtil.ALIPAY_APP_ID + "&scope=auth_user&state=" + ConstantUtil.ALIPAY_AUTH_STATE
        // 支付宝授权后回跳scheme
        val scheme = "http://auth.schoolairdrop.com/alipaycallback"

        // 唤起授权业务
        val task = OpenAuthTask(this)
        task.execute(
                scheme,
                OpenAuthTask.BizType.AccountAuth,
                bizParams,
                openAuthCallback,
                true)

        dismissLoading()
    }

    /**
     * 按下登录按钮后的所有逻辑
     */
    private fun clickToLogin() {
        UserLoginCacheUtil.getInstance().getUserTokenAnyway().let { cache ->
            if (cache == null) {
                // 本地没有alipay id缓存，可能是没有登录过，也可能是账号手动退出，也可能是缓存被清理
                openAuthScheme()
            } else {
                showLoading()
                // 本地有缓存，直接使用之登录
                loginViewModel.refreshToken(cache).observeOnce(this) { pair ->
                    dismissLoading {
                        when (pair.second) {
                            // token刷新成功
                            ConstantUtil.HTTP_OK -> {
                                // 登录成功了，也不需要把isLogging置回false了
                                getUserInfoWithToken(pair.first)
                            }

                            // refresh token已经失效，需要用户重新授权登录
                            ConstantUtil.HTTP_INVALID_REFRESH_TOKEN -> {
                                loginViewModel.logout()
                                openAuthScheme()
                            }

                            // 请求失败
                            else -> {
                                loginViewModel.logout()
                                loginError()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_with_alipay -> {
                if (AppConfig.IS_DEBUG) {
                    showLoading()
                    loginViewModel.loginDebug().observeOnce(this) {
                        getUserInfoWithToken(it)
                    }
                } else {
                    clickToLogin()
                }
            }

            R.id.privacy_protocol -> {
                MyUtil.openPrivacyWebsite(this)
            }

            R.id.user_protocol -> {
                MyUtil.openServiceWebsite(this)
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
     * 在获取了auth code之后使用它换取alipay id并进行注册或登录
     *
     * 登录总共分三步
     * 1、使用传递进来的authCode访问后端callback接口，后端从支付宝后台获取alipay id后返回
     * 2、获取服务器公钥
     * 2、使用公钥加密获取的alipay id并访问服务器登录接口，未注册将自动注册
     */
    private fun doLoginAppWithAuthCode(authCode: String?) {
        // 暂时禁用提交按钮防止短时间内提交多次
        login_with_alipay.isEnabled = false

        // 开始转圈圈，这个调用里一旦出错中断了，必须调用loginError来提示用户登录请求中断
        showLoading {
            // 如果没网直接error
            if (NetworkUtils.isConnected()) {
                // 这里是唯一能将这个变量置为true的地方，在此方法之后如果失败一定要调用loginError
                // 否则用户重新打开页面之前再点也没办法登录了
                if (!isLogging) {
                    isLogging = true
                    loginViewModel.loginWithAlipayAuthCode(authCode).observeOnce(this) { token ->
                        if (token != null) {
                            // 登录成功了，也不需要把isLogging置回false了
                            getUserInfoWithToken(token)
                        } else {
                            // 这里是手动登录，要是返回了null说明是出问题了
                            loginError()
                        }
                    }
                }
            } else {
                loginError()
            }
        }

        // 已经在网络请求了，可以放开提交按钮了
        login_with_alipay.isEnabled = true
    }

    /**
     * 使用token获取用户信息
     */
    private fun getUserInfoWithToken(token: DomainToken?) {
        // 要是登录了发现token没了那也没救了
        if (token?.access_token != null) {
            loginViewModel.getMyInfo(token.access_token).observeOnce(this@LoginActivity, {
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