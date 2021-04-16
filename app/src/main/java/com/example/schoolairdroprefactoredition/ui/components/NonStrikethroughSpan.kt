package com.example.schoolairdroprefactoredition.ui.components

import android.text.TextPaint
import android.text.style.StrikethroughSpan


/**
 * @author kennen
 * @date 2021/4/14
 */
class NonStrikethroughSpan : StrikethroughSpan() {
    override fun updateDrawState(ds: TextPaint) {
        ds.isStrikeThruText = false
    }
}