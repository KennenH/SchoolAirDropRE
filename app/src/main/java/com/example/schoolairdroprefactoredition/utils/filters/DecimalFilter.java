package com.example.schoolairdroprefactoredition.utils.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pattern0: 匹配非零开头的输入，小数点前最多5个数字，除第一个数字非零外其余都可为零
 * pattern1: 匹配以零开头的输入，小数点前不可有其他数字
 * pattern2: 匹配直接以小数点开头的输入，将输入转换为 0.
 */
public class DecimalFilter implements InputFilter {

    private static final int DIGITS_BEFORE_ZERO_DEFAULT = 5; // 小数点前最高位数
    private static final int DIGITS_AFTER_ZERO_DEFAULT = 2; // 小数点后最高位数

    private final Pattern mPattern0;
    private final Pattern mPattern1;
    private final Pattern mPattern2;

    public DecimalFilter() {
        this(null, null);
    }

    public DecimalFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
        int mDigitsBeforeZero = (digitsBeforeZero != null ? digitsBeforeZero : DIGITS_BEFORE_ZERO_DEFAULT);
        int mDigitsAfterZero = (digitsAfterZero != null ? digitsAfterZero : DIGITS_AFTER_ZERO_DEFAULT);

        mPattern0 = Pattern.compile("[1-9][0-9]{0," + (mDigitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (mDigitsAfterZero)
                + "})?)||(\\.)?");
        mPattern1 = Pattern.compile("0((\\.[0-9]{0,2})?)||(\\.)?");
        mPattern2 = Pattern.compile("^\\.");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String replacement = source.subSequence(start, end).toString();
        String newVal = dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length()).toString();
        Matcher matcher = mPattern0.matcher(newVal);
        Matcher matcher1 = mPattern1.matcher(newVal);
        Matcher matcher2 = mPattern2.matcher(newVal);

        if (matcher2.matches()) {
            return "0.";
        }

        if (matcher.matches() || matcher1.matches()) {
            return null;
        }

        if (TextUtils.isEmpty(source)) {
            return dest.subSequence(dstart, dend);
        } else {
            return "";
        }
    }
}