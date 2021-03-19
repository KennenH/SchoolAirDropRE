package com.example.schoolairdroprefactoredition.scene.main.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.BaseFragment
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity.OnLoginStateChangedListener
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.scene.favorite.FavoriteActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class MyFragment : BaseFragment(), View.OnClickListener, OnLoginStateChangedListener {

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    private var mAvatar: ImageView? = null

    private var mName: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as? MainActivity)?.addOnLoginActivityListener(this)
            (activity as? MainActivity)?.autoLogin()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMyBinding.inflate(LayoutInflater.from(context))

        mAvatar = binding.myAvatar
        mName = binding.myName

        binding.myInfo.setOnClickListener(this)
        binding.mySelling.setOnClickListener(this)
        binding.myPosts.setOnClickListener(this)
        binding.myLikes.setOnClickListener(this)
        binding.mySettings.setOnClickListener(this)
        binding.rootLayout.setOnRefreshListener { it.finishRefresh() }

        setUserData()
        return binding.root
    }

    private val mainIntent: Intent
        get() = if (activity is MainActivity) {
            (activity as MainActivity).intent
        } else Intent()

    private fun getInfo(): DomainUserInfo.DataBean? {
        return mainIntent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as? DomainUserInfo.DataBean
    }

    private fun getToken(): DomainToken? {
        return mainIntent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as? DomainToken
    }

    /**
     * 使用当前页面保存的bundle来显示用户信息
     * 若用户token与info都存在 则使用服务器数据填充页面
     * 否则使用页面默认值填充
     */
    private fun setUserData() {
        val info = getInfo()
        if (getToken() == null) { // 无token认证信息，显示默认值
            mAvatar?.setImageResource(R.drawable.placeholder_rounded)
            mName?.text = getString(R.string.pleaseLogin)
        } else if (info != null) { // 设置页面数据
            ImageUtil.loadRoundedImage(mAvatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(info.userAvatar))
            mName?.text = info.userName
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        val intent: Intent = mainIntent
        when (id) {
            R.id.my_info -> {
                val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
                val userInfo = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO)
                if (token != null && userInfo != null) {
                    UserActivity.start(activity)
                } else {
                    LoginActivity.start(context)
                }
            }

            R.id.my_likes -> {
                FavoriteActivity.start(context)
            }

            R.id.my_settings -> {
                SettingsActivity.startForResultLogin(context, intent.extras)
            }

            R.id.my_selling -> {
                val token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN)
                if (token != null) {
                    SSBActivity.start(context, getInfo()?.userId, 0, true)
                } else {
                    LoginActivity.start(context)
                }
            }

            R.id.my_posts -> {
                Toast.makeText(context, getString(R.string.functionNotSupport), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 来自[MainActivity]的
     * 登录状态改变的回调
     */
    override fun onLoginStateChanged(intent: Intent) {
        setUserData()
    }

    override fun onLogging() {
        // do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? MainActivity)?.removeOnLoginActivityListener(this)
    }
}