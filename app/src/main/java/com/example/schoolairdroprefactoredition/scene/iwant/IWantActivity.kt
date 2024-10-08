package com.example.schoolairdroprefactoredition.scene.iwant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.adapter.IWantHorizontalRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.TransitionAdapter
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import com.example.schoolairdroprefactoredition.utils.decoration.HorizontalItemMarginDecoration
import kotlinx.android.synthetic.main.activity_iwant_refactor.*

/**
 * [com.example.schoolairdroprefactoredition.scene.main.home.IWantFragment.onHomePostItemClicked]
 */
class IWantActivity : ImmersionStatusBarActivity() {

    companion object {

        const val KEY_IWANT_ITEM = "IWantActivityItem"

        @JvmStatic
        fun start(context: Context, card: View, item: DomainIWant.Data) {
            val intent = Intent(context, IWantActivity::class.java)
            intent.putExtra(KEY_IWANT_ITEM, item)
            val wrapper = Pair.create(card, context.getString(R.string.sharedElementPostActivityWrapper))
            val options = context.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(it as AppCompatActivity, wrapper)
            }
            context.startActivity(intent, options.toBundle())
        }
    }

    private val iwantEntity by lazy {
        intent.getSerializableExtra(KEY_IWANT_ITEM) as DomainIWant.Data
    }

    private val windowTransition by lazy {
        TransitionInflater.from(this).inflateTransition(R.transition.share_element_post_pager)
                .also {
                    it.duration = 230L
                    it.interpolator = AccelerateDecelerateInterpolator()
                }
    }

    private val adapter by lazy {
        IWantHorizontalRecyclerAdapter()
    }

    private val springForce by lazy {
        SpringForce(1f).also {
            it.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iwant_refactor)


        // 初始化视图
        initView()
        // 初始化元素共享动画
        initTransition()
    }

    private fun initView() {
        StatusBarUtil.setTranslucent(this, 0)

        iwant_tag.text = getString(R.string.iwant_tag, iwantEntity.tag.tags_content)
        iwant_content.text = iwantEntity.iwant_content

        val blackText = resources.getColor(R.color.black, theme)
        val blackAlwaysText = resources.getColor(R.color.blackAlways, theme)
        val whiteAlwaysText = resources.getColor(R.color.whiteAlways, theme)
        when (iwantEntity.iwant_color) {
            IWantRecyclerAdapter.COLOR_HEART -> {
                iwant_content.setTextColor(whiteAlwaysText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_red, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.heart, theme))
            }
            IWantRecyclerAdapter.COLOR_WARNING -> {
                iwant_content.setTextColor(blackAlwaysText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_yellow, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.yellow, theme))
            }
            IWantRecyclerAdapter.COLOR_PURPLE -> {
                iwant_content.setTextColor(whiteAlwaysText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_purple, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.colorPrimaryPurple, theme))
            }
            IWantRecyclerAdapter.COLOR_THEME -> {
                iwant_content.setTextColor(whiteAlwaysText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_theme, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.colorAccentDark, theme))
            }
            else -> {
                iwant_content.setTextColor(blackText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.primaryDark, theme))
            }
        }

        val images = iwantEntity.iwant_images.split(",")
        iwant_recycler.post {
            if (images[0].trim() != "") {
                iwant_recycler.apply {
                    layoutManager = LinearLayoutManager(this@IWantActivity, LinearLayoutManager.HORIZONTAL, false)
                    val padding = SizeUtils.dp2px(5f)
                    addItemDecoration(HorizontalItemMarginDecoration(padding.toFloat()))
                    setPadding(padding, padding * 2, padding, padding * 2)
                    adapter = this@IWantActivity.adapter
                }
                adapter.setList(images)
            }
        }

        ImageUtil.loadRoundedImage(iwant_user_avatar, ConstantUtil.QINIU_BASE_URL + iwantEntity.seller.user_avatar)
        iwant_user_name.text = iwantEntity.seller.user_name
        iwant_card.setOnClickListener { }
        iwant_user_name.setOnClickListener {
            UserActivity.start(this, iwantEntity.seller.user_id)
        }
        iwant_user_avatar.setOnClickListener {
            UserActivity.start(this, iwantEntity.seller.user_id)
        }

        val myInfo = (application as SAApplication).getCachedMyInfo()
        iwant_contact_owner.setOnClickListener {
            if (myInfo == null) {
                LoginActivity.start(this)
            } else {
                ChatActivity.start(this,
                        DomainUserInfo.DataBean().also {
                            it.userId = iwantEntity.seller.user_id
                            it.userName = iwantEntity.seller.user_name
                            it.userAvatar = iwantEntity.seller.user_avatar
                        })
            }
        }

        if (myInfo != null && myInfo.userId == iwantEntity.seller.user_id) {
            iwant_contact_owner.visibility = View.INVISIBLE
        }

        iwant_root.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                setResult(LoginActivity.LOGIN, data)
                (application as SAApplication).cacheMyInfoAndToken(
                        data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean,
                        data.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken)

            }
        }
    }

    /**
     * 初始化进入页面时的圆角变换动画
     */
    private fun initTransition() {
//        val (springAnimX, springAnimY) = iwant_card.let {
//            SpringAnimation(it, DynamicAnimation.SCALE_X) to
//                    SpringAnimation(it, DynamicAnimation.SCALE_Y)
//        }
//        springAnimX.setStartVelocity(1.47f)
//        springAnimY.setStartVelocity(1.47f)
//        springAnimX.spring = springForce
//        springAnimY.spring = springForce
        window.sharedElementEnterTransition = windowTransition
        window.sharedElementEnterTransition.apply {
            addListener(object : TransitionAdapter() {
                override fun onTransitionEnd(transition: Transition) {
//                    springAnimX.start()
//                    springAnimY.start()
                    removeListener(this)
                    // 下面的监听器将在退出的动画中被触发
                    addListener(object : TransitionAdapter() {
                        override fun onTransitionStart(transition: Transition) {
                            iwant_recycler.visibility = View.GONE
                            if (iwant_content.text.toString().length > 50) {
                                iwant_tag.visibility = View.GONE
                                iwant_user_avatar.visibility = View.GONE
                                iwant_user_name.visibility = View.GONE
                                iwant_contact_owner.visibility = View.GONE
                            }
                        }

                        override fun onTransitionEnd(transition: Transition) {
//                            springAnimX.setStartVelocity(-1.66f)
//                            springAnimY.setStartVelocity(-1.66f)
//                            springAnimX.start()
//                            springAnimY.start()
                        }
                    })
                }
            })
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
        super.onBackPressed()
    }
}