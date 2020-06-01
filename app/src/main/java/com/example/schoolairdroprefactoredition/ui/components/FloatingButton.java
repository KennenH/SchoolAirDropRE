package com.example.schoolairdroprefactoredition.ui.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.NumberUtil;
import com.lihang.ShadowLayout;

public class FloatingButton extends ConstraintLayout implements View.OnTouchListener {

    private Handler mHandler = new Handler();

    private static final float CLICK_DRAG_TOLERANCE = 10f;

    private static final int RECOVER_DELAY = 3000;
    private static final int DURATION_TOUCH = 100;
    private static final int DURATION_RECOVER = 400;

    private ShadowLayout mShadow;
    private ImageView mImage;

    private float normalAlpha = 0.7f;
    private float touchedAlpha = 1f;

    private float dX;
    private float dY;
    private boolean isCorrectingPosition;

    private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

    private Runnable recoverAlpha = () -> setAlphaWithAnim(getAlpha(), normalAlpha, DURATION_RECOVER);

    public FloatingButton(Context context) {
        this(context, null);
    }

    public FloatingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_goods_button_left, this, true);

        mImage = findViewById(R.id.button_one);
        mShadow = findViewById(R.id.shadow_layout);

        mImage.setImageDrawable(getResources().getDrawable(R.drawable.floating_button));
        mShadow.setmShadowLimit(SizeUtils.dp2px(10));

        setAlpha(normalAlpha);

        mImage.setOnTouchListener(this);
    }

    /**
     * 按下后的alpha
     *
     * @param alpha
     */
    public void setTouchedAlpha(float alpha) {
        touchedAlpha = alpha;
    }

    /**
     * 一般显示alpha
     *
     * @param alpha
     */
    public void setNormalAlpha(float alpha) {
        normalAlpha = alpha;
    }

    private void setAlphaWithAnim(float start, float end, int time) {
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(this, "Alpha", start, end);
        alphaAnim.setDuration(time).start();
    }

    /**
     * 每次被移动后调用该方法以矫正浮动球的位置
     */
    private void correctPosition() {
        ViewGroup parent = (ViewGroup) getParent();

        float dTop = Math.abs(getY());
        float dLeft = Math.abs(getX());
        float dBottom = Math.abs(parent.getBottom() - (getY() + getHeight()));
        float dRight = Math.abs(parent.getRight() - (getX() + getWidth()));
        float min = NumberUtil.min(dTop, dBottom, dLeft, dRight);

        float x = dLeft;
        float y = dTop;

        if (min == dRight) {
            x = parent.getRight() - getWidth();
        } else if (min == dLeft) {
            x = 0;
        } else if (min == dBottom) {
            y = parent.getBottom() - getHeight();
        } else {
            y = 0;
        }

        this.animate()
                .x(x)
                .y(y)
                .setDuration(288)
                .setInterpolator(interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isCorrectingPosition = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        isCorrectingPosition = true;
                    }
                })
                .start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int e = event.getAction();

        if (isCorrectingPosition) return false;

        switch (e) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacks(recoverAlpha);
                setAlphaWithAnim(getAlpha(), touchedAlpha, DURATION_TOUCH);
                float downX = event.getRawX();
                float downY = event.getRawY();
                dX = getX() - downX;
                dY = getY() - downY;

                return true;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                setX(newX);
                setY(newY);

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.postDelayed(recoverAlpha, RECOVER_DELAY);

                float x = event.getRawX();
                float y = event.getRawY();

                // 条件满足为点击，否则为拖拽
                if (Math.abs(x) < CLICK_DRAG_TOLERANCE && Math.abs(y) < CLICK_DRAG_TOLERANCE)
                    return performClick();

                correctPosition();

                return true;

        }
        return super.onTouchEvent(event);
    }
}
