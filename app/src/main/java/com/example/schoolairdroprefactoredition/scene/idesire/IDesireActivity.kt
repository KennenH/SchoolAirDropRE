package com.example.schoolairdroprefactoredition.scene.idesire

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.Transition
import android.view.MenuItem
import android.transition.ChangeBounds
import android.view.animation.DecelerateInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.adapter.TransitionAdapter
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_idesire.*

/**
 * [com.example.schoolairdroprefactoredition.scene.main.home.IDesireFragment.onHomePostItemClicked]
 */
class IDesireActivity : ImmersionStatusBarActivity() {

    companion object {
        val startTitleSize = SizeUtils.dp2px(5f).toFloat()
        val endTitleSize = SizeUtils.dp2px(7.2f).toFloat()

        /**
         * 彩色wrapper的动画时间
         */
        const val wrapperAnimDuration = 500L

        /**
         * 文字内容的动画时间
         */
        const val contentAnimDuration = 500L

    }

    private val changeBounds by lazy {
        ChangeBounds().also {
            it.duration = 500L
//            it.interpolator = DecelerateInterpolator(0.9f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idesire)
        setSupportActionBar(toolbar)
        initView()
    }

    private fun initView() {
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar)
        // 等待调用supportStartPostponedEnterTransition
        supportPostponeEnterTransition()
        // 初始化元素共享动画
        initTransition()
        // 开始元素共享动画
        supportStartPostponedEnterTransition()


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
        window.enterTransition = null
        window.exitTransition = null
        window.sharedElementEnterTransition = changeBounds
        window.sharedElementReturnTransition = changeBounds

        posts_images.radius = SizeUtils.dp2px(11f).toFloat()
        window.sharedElementEnterTransition.apply {
            addListener(object : TransitionAdapter() {
                override fun onTransitionStart(transition: Transition) {
                    val pagerAnim: ObjectAnimator = ObjectAnimator.ofFloat(posts_images, "radius", 0f)
                    pagerAnim.duration = wrapperAnimDuration

                    val titleAnim = ValueAnimator.ofFloat(startTitleSize, endTitleSize)
                    titleAnim.duration = contentAnimDuration
                    titleAnim.addUpdateListener {
                        val animatedValue = it.animatedValue as Float
                        posts_title.textSize = animatedValue
                    }

                    pagerAnim.start()
                    titleAnim.start()
                }

                override fun onTransitionEnd(transition: Transition) {
                    removeListener(this)
                    addListener(object : TransitionAdapter() {
                        override fun onTransitionStart(transition: Transition) {
                            val animator: ObjectAnimator = ObjectAnimator.ofFloat(posts_images, "radius", SizeUtils.dp2px(15f).toFloat())
                            animator.duration = wrapperAnimDuration

                            val titleAnim = ValueAnimator.ofFloat(endTitleSize, startTitleSize)
                            titleAnim.duration = contentAnimDuration
                            titleAnim.addUpdateListener {
                                val animatedValue = it.animatedValue as Float
                                posts_title.textSize = animatedValue
                            }

                            animator.start()
                            titleAnim.start()
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            removeListener(this)
                        }
                    })
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return false
    }
}