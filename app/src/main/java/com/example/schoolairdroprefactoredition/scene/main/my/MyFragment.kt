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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentMyBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.capture.CaptureActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.scene.main.MainActivity.OnLoginStateChangedListener
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity.Companion.startForLogin
import com.example.schoolairdroprefactoredition.scene.settings.SettingsActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.components.SSBInfo
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DemoConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.google.zxing.integration.android.IntentIntegrator

class MyFragment : Fragment(), View.OnClickListener, OnLoginStateChangedListener, BaseStateViewModel.OnRequestListener {

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    private var mAvatar: ImageView? = null

    private var mName: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MainActivity) {
            (activity as MainActivity).addOnLoginActivityListener(this)
            (activity as MainActivity).autoLogin()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMyBinding.inflate(LayoutInflater.from(context))

        mAvatar = binding.myAvatar
        mName = binding.myName

        viewModel.setOnRequestListener(this)
        binding.myInfo.setOnClickListener(this)
        binding.mySelling.setOnClickListener(this)
        binding.myPosts.setOnClickListener(this)
        binding.myLikes.setOnClickListener(this)
        binding.mySettings.setOnClickListener(this)
        binding.rootLayout.setOnRefreshListener { it.finishRefresh() }
//        binding.mySellingPosts.setOnSSBActionListener(this)

        setUserData()
        return binding.root
    }

    private val mainIntent: Intent
        get() = if (activity is MainActivity) {
            (activity as MainActivity).intent
        } else Intent()

    private fun getInfo(): DomainUserInfo.DataBean? {
        val info = mainIntent.getSerializableExtra(ConstantUtil.KEY_USER_INFO)

        return info as? DomainUserInfo.DataBean

//        return if (info == null) {
//            null
//        } else {
//            info as DomainUserInfo.DataBean
//        }
    }

    private fun getToken(): DomainToken? {
        val token = mainIntent.getSerializableExtra(ConstantUtil.KEY_TOKEN)

        return token as? DomainToken

//        return if (token == null) {
//            null
//        } else {
//            token as DomainToken
//        }
    }

    /**
     * 使用当前页面保存的bundle来显示用户信息
     * 若用户token与info都存在 则使用服务器数据填充页面
     * 否则使用页面默认值填充
     */
    private fun setUserData() {
        val info = getInfo()
        if (getToken() == null) { // 无token认证信息，显示默认值
            mAvatar?.setImageResource(R.drawable.ic_logo_alpha)
            mName?.text = getString(R.string.pleaseLogin)
        } else if (info != null) { // 设置页面数据
            ImageUtil.loadRoundedImage(mAvatar, DemoConstantUtil.DEMO_BASE_URL + info.user_img_path)
            mName?.text = info.uname
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        val intent: Intent = mainIntent
        when (id) {
            R.id.my_info -> {
                if (intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) != null && intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) != null) {
                    UserActivity.start(
                            activity,
                            intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken,
                            intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO))
                } else {
                    startForLogin(context)
                }
            }

            R.id.my_likes ->                 // list my likes
            {
                IntentIntegrator.forSupportFragment(this@MyFragment)
                        .setCaptureActivity(CaptureActivity::class.java)
                        .setBeepEnabled(false)
                        .initiateScan()
            }

            R.id.my_settings -> {
                SettingsActivity.startForResultLogin(context, intent.extras)
            }

            R.id.my_selling -> {
                SSBActivity.start(context, getToken(), getInfo(), 0, true)
            }

            R.id.my_posts -> {

            }
        }
    }

    /**
     * 来自[MainActivity]的
     * 登录状态改变的回调
     */
    override fun onLoginStateChanged(intent: Intent) {
        val token = getToken()
        setUserData()
        if (token != null) viewModel.getUserInfo(token.access_token).observe(viewLifecycleOwner, { setUserData() })
    }

    override fun onError() {
        Toast.makeText(context, "Error getting user info", Toast.LENGTH_SHORT).show()
    }

}