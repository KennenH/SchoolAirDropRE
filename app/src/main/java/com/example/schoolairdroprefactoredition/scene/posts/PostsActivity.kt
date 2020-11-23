package com.example.schoolairdroprefactoredition.scene.posts

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.Transition
import android.view.MenuItem
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.adapter.TransitionAdapter
import kotlinx.android.synthetic.main.activity_posts.*

class PostsActivity : ImmersionStatusBarActivity() {

    companion object {
        val startTitleSize = SizeUtils.dp2px(5f).toFloat()
        val endTitleSize = SizeUtils.dp2px(7f).toFloat()
        const val pagerAnimDuration = 500L
        const val titleAnimDuration = 250L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        setSupportActionBar(toolbar)
        startTransition()
    }

    private fun startTransition() {
        posts_images.radius = SizeUtils.dp2px(12f).toFloat()
        window.sharedElementEnterTransition.apply {
            addListener(object : TransitionAdapter() {
                override fun onTransitionStart(transition: Transition) {
                    val pagerAnim: ObjectAnimator = ObjectAnimator.ofFloat(posts_images, "radius", 0f)
                    pagerAnim.duration = pagerAnimDuration

                    val titleAnim = ValueAnimator.ofFloat(startTitleSize, endTitleSize)
                    titleAnim.duration = titleAnimDuration
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
                            animator.duration = pagerAnimDuration

                            val titleAnim = ValueAnimator.ofFloat(endTitleSize, startTitleSize)
                            titleAnim.duration = titleAnimDuration
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