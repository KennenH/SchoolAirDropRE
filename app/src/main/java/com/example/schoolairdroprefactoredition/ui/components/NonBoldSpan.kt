package com.example.schoolairdroprefactoredition.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.StyleSpan


/**
 * @author kennen
 * @date 2021/4/14
 *
 * 00 normal
 * 01 bold
 * 10 italic
 * 11 bold italic
 */
class NonBoldSpan : StyleSpan(Typeface.NORMAL) {

    override fun updateDrawState(ds: TextPaint?) {
        apply(ds, Typeface.NORMAL)
    }

    private fun apply(paint: Paint?, style: Int) {
        val old = paint?.typeface
        val tf: Typeface =
//        if (old == null) {
            Typeface.defaultFromStyle(style)
//        } else {
//            Typeface.create(old, style)
//        }
        val fake = style and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint?.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint?.textSkewX = -0.25f
        }
        paint?.typeface = tf
    }
}