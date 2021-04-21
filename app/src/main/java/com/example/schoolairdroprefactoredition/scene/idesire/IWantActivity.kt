package com.example.schoolairdroprefactoredition.scene.idesire

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.adapter.IWantHorizontalRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.TransitionAdapter
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import com.example.schoolairdroprefactoredition.utils.AnimUtil
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
        intent.getSerializableExtra(KEY_IWANT_ITEM) as? BaseIWantEntity?
    }

    private val windowTransition by lazy {
        TransitionInflater.from(this).inflateTransition(R.transition.share_element_post_pager)
                .also {
                    it.duration = 150L
                    it.interpolator = AccelerateInterpolator()
                }
    }

    private val adapter by lazy {
        IWantHorizontalRecyclerAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iwant_refactor)
//        setSupportActionBar(toolbar)
        initView()
    }

    private fun initView() {
        StatusBarUtil.setTranslucent(this, 0)
        // 等待调用supportStartPostponedEnterTransition
        supportPostponeEnterTransition()
//        // 初始化元素共享动画
        initTransition()
//        // 开始元素共享动画
        supportStartPostponedEnterTransition()


        val blackText = resources.getColor(R.color.black, theme)
        val whiteAlwaysText = resources.getColor(R.color.whiteAlways, theme)
        iwant_recycler.apply {
            when (iwantEntity?.color) {
                IWantRecyclerAdapter.COLOR_PURPLE -> {
                    iwant_content.setTextColor(whiteAlwaysText)
                    iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_purple, theme)
                    setBackgroundColor(resources.getColor(R.color.colorPrimaryPurple, theme))
                }
                IWantRecyclerAdapter.COLOR_THEME -> {
                    iwant_content.setTextColor(whiteAlwaysText)
                    iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded_theme, theme)
                    setBackgroundColor(resources.getColor(R.color.colorAccentDark, theme))
                }
                else -> {
                    iwant_content.setTextColor(blackText)
                    iwant_content_wrapper.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_top_rounded, theme)
                    setBackgroundColor(resources.getColor(R.color.primaryDark, theme))
                }
            }

            layoutManager = LinearLayoutManager(this@IWantActivity, LinearLayoutManager.HORIZONTAL, false)
            val padding = SizeUtils.dp2px(5f)
            addItemDecoration(HorizontalItemMarginDecoration(padding.toFloat()))
            setPadding(padding, padding * 2, padding, padding * 2)
            adapter = this@IWantActivity.adapter
        }
        adapter.setList(ArrayList<String>(2).also {
            it.add("")
            it.add("")
        })

        iwant_root.setOnClickListener {
            supportFinishAfterTransition()
        }
//        posts_pager.setOtherResourceFitFirst()
//        posts_pager.setOnFirstImageLoadedListener {
//            supportStartPostponedEnterTransition()
//        }
//        val list = ArrayList<String>()
//        list.add("http://img.chusan.com/upload/201910/20191001131750633.jpg")
//        list.add("http://img.chusan.com/upload/201910/20191001131754221.jpg")
////        ImageUtil.loadImage(posts_image_test, list[2], R.drawable.logo_placeholder) { supportStartPostponedEnterTransition() }
//        posts_pager.setData(list, true)
    }

    /**
     * 初始化进入页面时的圆角变换动画
     */
    private fun initTransition() {
        val (springAnimX, springAnimY) = iwant_card.let {
            SpringAnimation(it, DynamicAnimation.SCALE_X) to
                    SpringAnimation(it, DynamicAnimation.SCALE_Y)
        }
        springAnimX.setStartVelocity(1.3f)
        springAnimY.setStartVelocity(1.3f)
        springAnimX.spring = SpringForce(1f).also {
            it.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        }
        springAnimY.spring = SpringForce(1f).also {
            it.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
        }

        window.sharedElementEnterTransition = windowTransition
        window.sharedElementEnterTransition.apply {
            addListener(object : TransitionAdapter() {
                override fun onTransitionEnd(transition: Transition) {
//                    LogUtils.d(iwant_card.height)
//                    LogUtils.d(iwant_card.width)
                    springAnimX.start()
                    springAnimY.start()
                    removeListener(this)
                    // 下面的监听器将在退出的动画中被触发
                    addListener(object : TransitionAdapter() {
                        override fun onTransitionStart(transition: Transition) {
//                            iwant_card.visibility = View.GONE
                            iwant_tag.visibility = View.GONE
                            iwant_user_avatar.visibility = View.GONE
                            iwant_user_name.visibility = View.GONE
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            springAnimX.setStartVelocity(-1.5f)
                            springAnimY.setStartVelocity(-1.5f)
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