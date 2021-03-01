package com.example.schoolairdroprefactoredition.custom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnFloatAnimator
import kotlin.math.min

class InAppFloatAnimator : OnFloatAnimator {

    override fun enterAnim(
            view: View,
            parentView: ViewGroup,
            sidePattern: SidePattern
    ): Animator {
        return ObjectAnimator.ofFloat(
                view,
                "translationY",
                (-view.height).toFloat(),
                view.translationY).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    override fun exitAnim(
            view: View,
            parentView: ViewGroup,
            sidePattern: SidePattern
    ): Animator {
        return ObjectAnimator.ofFloat(
                view,
                "translationY",
                view.translationY,
                (-view.height).toFloat()).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}