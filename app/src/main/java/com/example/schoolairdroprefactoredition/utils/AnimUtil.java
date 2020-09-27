package com.example.schoolairdroprefactoredition.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.example.schoolairdroprefactoredition.R;

public class AnimUtil {

    /**
     * 使白色背景的view高亮闪烁红色以引起用户注意
     */
    public static void whiteBackgroundViewBlinkRed(Context context, final View view) {
        int colorTo = ContextCompat.getColor(context, R.color.colorPrimaryRedLight);
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.WHITE, colorTo);
        anim.setDuration(180); // milliseconds
        anim.setRepeatCount(1); // repeat count must be ood to recover original color of view
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.addUpdateListener(animator -> {
            view.setBackgroundColor((int) animator.getAnimatedValue());
        });
        anim.start();
    }

    /**
     * 使view高亮闪烁以引起用户注意
     *
     * @param fromColor 起始颜色
     * @param toColor   结束颜色
     */
    public static void viewBlink(Context context, final View view, @ColorRes int fromColor, @ColorRes int toColor) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), ContextCompat.getColor(context, fromColor), ContextCompat.getColor(context, toColor));
        anim.setDuration(180);
        anim.setRepeatCount(1);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.addUpdateListener(animation -> view.setBackgroundColor((int) animation.getAnimatedValue()));
        anim.start();
    }


    /**
     * View 从上至下展开动画
     *
     * @param v
     */
    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(v.getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int now = (int) (targetHeight * interpolatedTime);
                v.getLayoutParams().height = v.getLayoutParams().height < now ? now : v.getLayoutParams().height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(250);
        v.startAnimation(a);
    }

    /**
     * View 由下至上折叠动画
     *
     * @param v
     */
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);
        v.startAnimation(a);
    }


}
