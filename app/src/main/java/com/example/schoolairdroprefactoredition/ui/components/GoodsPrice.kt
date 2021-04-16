package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.utils.NumberUtil

class GoodsPrice : ConstraintLayout {

    private val mCurrency: TextView

    private val mPriceInt: TextView

    private val mPriceFraction: TextView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.component_price_light, this, true)
        mCurrency = findViewById(R.id.price_currency)
        mPriceInt = findViewById(R.id.price_int)
        mPriceFraction = findViewById(R.id.price_fraction)
    }

    /**
     * @param isUnitMode 是否需要转换为以k为单位的形式
     */
    fun setPrice(price: Float, isUnitMode: Boolean) {
        val p = if (isUnitMode) {
            NumberUtil.num2Money(price)
        } else {
            price.toString()
        }
        val dot = p.indexOf(".")
        mCurrency.setText(R.string.currency_RMB)
        if (dot != -1) {
            mPriceInt.text = p.substring(0, dot)
            mPriceFraction.text = p.substring(dot)
        } else {
            mPriceInt.text = p
            mPriceFraction.setText(R.string.fraction0)
        }
    }

    /**
     * @param isUnitMode 是否需要转换为以k为单位的形式
     */
    fun setPrice(price: String?, isUnitMode: Boolean) {
        if (price == null) return
        val p = if (isUnitMode) {
            val priceF = price.toFloat()
            NumberUtil.num2Money(priceF)
        } else {
            price
        }
        val dot = p.indexOf(".")
        mCurrency.setText(R.string.currency_RMB)
        if (dot != -1) {
            mPriceInt.text = p.substring(0, dot)
            mPriceFraction.text = p.substring(dot)
        } else {
            mPriceInt.text = p
            mPriceFraction.setText(R.string.fraction0)
        }
    }
}