package com.example.schoolairdroprefactoredition.utils

import android.content.Context
import com.example.schoolairdroprefactoredition.R


/**
 * @author kennen
 * @date 2021/4/26
 */
object XPopupUtil {

    /**
     * 获取主页面点击新增数组
     */
    @JvmStatic
    fun getMainAddNewStringList(context: Context): Array<String> {
        return arrayOf(
                context.getString(R.string.addNewSelling),
                context.getString(R.string.addNewIWant))
    }

    /**
     * 获取主页点击新增图标数组
     */
    @JvmStatic
    fun getMainAddNewIconArray(): IntArray {
        return IntArray(2){
            R.drawable.ic_upload_cloud
            R.drawable.ic_upload_cloud
        }
    }
}