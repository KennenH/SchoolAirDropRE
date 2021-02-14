package com.example.schoolairdroprefactoredition.scene.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity.Companion.start
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity
import com.example.schoolairdroprefactoredition.scene.settings.fragment.SettingsFragment
import com.example.schoolairdroprefactoredition.scene.switchaccount.SwitchAccountActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class SettingsActivity : TransitionBaseActivity(), FragmentManager.OnBackStackChangedListener {
    companion object {
        fun startForResultLogin(context: Context?, bundle: Bundle?) {
            val intent = Intent(context, SettingsActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
            }
        }
    }

    private var mOnLoginListener: OnLoginListener? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstTransact(SettingsFragment.newInstance(intent), getString(R.string.setting))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                setResult(RESULT_OK, data) // 将登录结果返回至MainActivity
                mOnLoginListener?.onLogged(data)
            } else if (requestCode == SwitchAccountActivity.SWITCH_ACCOUNT) {
                if (data != null) {
                    val user = data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean
                    if (user != null) {
                        start(this@SettingsActivity)
                    }
                }
            }
        }
    }

    /**
     * [SettingsFragment]监听的登录回调
     * 以便登陆后它立即获取用户信息来填充页面
     */
    interface OnLoginListener {
        fun onLogged(intent: Intent?)
    }

    fun setOnLoginListener(listener: OnLoginListener?) {
        mOnLoginListener = listener
    }
}