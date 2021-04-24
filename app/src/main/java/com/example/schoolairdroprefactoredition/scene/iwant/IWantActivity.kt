package com.example.schoolairdroprefactoredition.scene.iwant

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
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.scene.user.UserActivity
import com.example.schoolairdroprefactoredition.ui.adapter.IWantHorizontalRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.TransitionAdapter
import com.example.schoolairdroprefactoredition.database.BaseIWantEntity
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
        fun start(context: Context, card: View, item: BaseIWantEntity) {
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
        intent.getSerializableExtra(KEY_IWANT_ITEM) as BaseIWantEntity?
    }

    private val windowTransition by lazy {
        TransitionInflater.from(this).inflateTransition(R.transition.share_element_post_pager)
                .also {
                    it.duration = 200L
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

        iwant_tag.text = getString(R.string.iwant_tag, iwantEntity?.tag)
        iwant_content.text = iwantEntity?.content

        val blackText = resources.getColor(R.color.black, theme)
        val blackAlwaysText = resources.getColor(R.color.blackAlways, theme)
        val whiteAlwaysText = resources.getColor(R.color.whiteAlways, theme)
        when (iwantEntity?.color) {
            IWantRecyclerAdapter.COLOR_RED -> {
                iwant_content.setTextColor(whiteAlwaysText)
                iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_red, theme)
                iwant_recycler.setBackgroundColor(resources.getColor(R.color.heart, theme))
            }
            IWantRecyclerAdapter.COLOR_ORANGE -> {
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
        iwant_recycler.apply {
            layoutManager = LinearLayoutManager(this@IWantActivity, LinearLayoutManager.HORIZONTAL, false)
            val padding = SizeUtils.dp2px(5f)
            addItemDecoration(HorizontalItemMarginDecoration(padding.toFloat()))
            setPadding(padding, padding * 2, padding, padding * 2)
            adapter = this@IWantActivity.adapter
        }
        if (iwantEntity?.content?.isEmpty() == false) {
            adapter.setList(iwantEntity?.content?.split(","))
        }

        iwant_card.setOnClickListener { }
        iwant_user_name.setOnClickListener {
            UserActivity.start(this, iwantEntity?.userID)
        }
        iwant_user_avatar.setOnClickListener {
            UserActivity.start(this, iwantEntity?.userID)
        }
        iwant_contact_owner.setOnClickListener {
            ChatActivity.start(this, iwantEntity?.userID)
        }
        iwant_root.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

    /**
     * 初始化进入页面时的圆角变换动画
     */
    private fun initTransition() {
        val (springAnimX, springAnimY) = iwant_card.let {
            SpringAnimation(it, DynamicAnimation.SCALE_X) to
                    SpringAnimation(it, DynamicAnimation.SCALE_Y)
        }
        springAnimX.setStartVelocity(1.4f)
        springAnimY.setStartVelocity(1.4f)
        springAnimX.spring = springForce
        springAnimY.spring = springForce
        window.sharedElementEnterTransition = windowTransition
        window.sharedElementEnterTransition.apply {
            addListener(object : TransitionAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    springAnimX.start()
                    springAnimY.start()
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
                            springAnimX.setStartVelocity(-1.6f)
                            springAnimY.setStartVelocity(-1.6f)
                            springAnimX.start()
                            springAnimY.start()
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