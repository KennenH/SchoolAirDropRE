package com.example.schoolairdroprefactoredition.utils;

/**
 * 有关数字的工具类
 */
public class NumberUtil {

    public static float min(float... ints) {
        float min = Float.MAX_VALUE;
        for (float i : ints)
            if (i < min) min = i;
        return min;
    }

    /**
     * 将数字转换为带 M k 单位的字符串
     *
     * @param num 数字
     * @return 带单位的字符串
     */
    public static String num2StringWithUnit(int num) {
        int integer;
        int fraction;
        if (num >= 1000000) {
            integer = num / 1000000;
            fraction = num / 100000 % 10;
            return fraction != 0 ? integer + "." + fraction + "M" : integer + "M";
        } else if (num >= 1000) {
            integer = num / 1000;
            fraction = num / 100 % 10;
            return fraction != 0 ? integer + "." + fraction + "k" : integer + "k";
        } else
            return String.valueOf(num);
    }

    /**
     * 转换为货币
     *
     * @param num
     * @return 最大以k为单位，最大为99k
     */
    public static String num2Money(float num) {
        int integer;
        int fraction;
        if (num >= 1000) {
            integer = (int) num / 1000;
            fraction = (int) num % 1000 / 10;
            return integer + "." + fraction + "k";
        } else
            return String.valueOf(num);
    }

    /**
     * 以bound为最大值将数字转换为带 + 的字符串
     *
     * @param num   数组
     * @param bound 最大值
     * @return 带+的字符串
     */
    public static String num2StringWithPlus(int num, int bound) {
        if (num > bound) {
            return bound + "+";
        } else
            return String.valueOf(num);
    }
}
