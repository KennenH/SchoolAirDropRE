package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSettingsHomeBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity.OnLoginListener
import com.example.schoolairdroprefactoredition.scene.switchaccount.SwitchAccountActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.LoginViewModel

/**
 * 设置的主页面
 */
class SettingsFragment : TransitionBaseFragment(), View.OnClickListener, OnLoginListener {


    companion object {
        const val LOGOUT = 1205 // 请求码 退出本地登录
        fun newInstance(intent: Intent): SettingsFragment {
            val fragment = SettingsFragment()
            fragment.arguments = intent.extras
            return fragment
        }
    }

    private val loginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val manager by lazy {
        activity?.supportFragmentManager
    }

    private lateinit var binding: FragmentSettingsHomeBinding

    private val notificationName by lazy {
        resources.getString(R.string.notification)
    }

    private val privacyName by lazy {
        resources.getString(R.string.privacy)
    }

    private val generalName by lazy {
        resources.getString(R.string.general)
    }

    private val aboutName by lazy {
        resources.getString(R.string.about)
    }

    private var bundle: Bundle? = null

    private var userInfo: DomainUserInfo.DataBean? = null

    private var token: DomainToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is SettingsActivity) {
            (activity as? SettingsActivity)?.setOnLoginListener(this)
        }

        bundle = arguments
        if (bundle == null) {
            bundle = Bundle()
        } else {
            userInfo = bundle?.getSerializable(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean?
            token = bundle?.getSerializable(ConstantUtil.KEY_TOKEN) as? DomainToken?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsHomeBinding.bind(LayoutInflater.from(context).inflate(R.layout.fragment_settings_home, container, false))
                .apply {
                    settingsHomeAlipay.setOnClickListener(this@SettingsFragment)
                    settingsHomePrivacy.setOnClickListener(this@SettingsFragment)
                    settingsHomeNotification.setOnClickListener(this@SettingsFragment)
                    settingsHomeGeneral.setOnClickListener(this@SettingsFragment)
                    settingsHomeAbout.setOnClickListener(this@SettingsFragment)
                    settingsHomeSwitchAccount.visibility = View.GONE
                    settingsHomeSignOut.setOnClickListener(this@SettingsFragment)
                }
        validateState()
        return binding.root
    }

    /**
     * [SettingsActivity]登录监听回调
     */
    override fun onLogged(intent: Intent?) {
        userInfo = intent?.extras?.getSerializable(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean?
        token = intent?.extras?.getSerializable(ConstantUtil.KEY_TOKEN) as? DomainToken?
        validateState()
    }

    /**
     * 有效化登录状态或退登状态
     * 使用bundle填充页面用户信息或清除页面数据
     */
    private fun validateState() {
        if (isLoggedIn) {
            binding.settingsHomeAlipay.setDescription(getString(R.string.loggedIn, userInfo?.userName))
            binding.settingsHomeSwitchAccount.visibility = View.VISIBLE
            binding.settingsHomeSignOut.visibility = View.VISIBLE
        } else {
            binding.settingsHomePrivacy.visibility = View.GONE
            binding.settingsHomeNotification.visibility = View.GONE
            binding.settingsHomeSwitchAccount.visibility = View.GONE
            binding.settingsHomeSignOut.visibility = View.GONE
        }
    }

    /**
     * 是否已登录
     *
     * @return 是否登登录
     */
    private val isLoggedIn: Boolean
        get() = (userInfo != null
                && token != null)

    /**
     * 已登录时点击支付宝登录10次将会出现
     */
    private var clickTimes = 0
    override fun onClick(v: View) {
        when (v.id) {
            R.id.settings_home_alipay -> {
                val token = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_TOKEN)
                if (token == null) {
                    LoginActivity.start(context)
                } else {
                    clickTimes++
                    if (clickTimes == 7) {
                        DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.FAVOR, R.string.gratefulToHaveU)
                    }
                }
            }
            R.id.settings_home_privacy -> {
                transact(manager, SettingsPrivacyFragment(), privacyName)
            }
            R.id.settings_home_notification -> {
                transact(manager, SettingsNotificationFragment(), notificationName)
            }
            R.id.settings_home_general -> {
                transact(manager, SettingsGeneralFragment(), generalName)
            }
            R.id.settings_home_about -> {
                transact(manager, SettingsAboutFragment(), aboutName)
            }
            R.id.settings_home_switch_account -> {
                SwitchAccountActivity.start(context, activity?.intent?.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean?)
            }
            R.id.settings_home_sign_out -> {
                DialogUtil.showConfirm(context, getString(R.string.logout), getString(R.string.confirmLogout)) {
                    loginViewModel.logout()
                    activity?.setResult(Activity.RESULT_OK, null)
                    activity?.finish()
                }
            }
        }
    }
}