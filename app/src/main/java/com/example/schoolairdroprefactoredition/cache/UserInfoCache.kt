package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import java.util.*
import kotlin.collections.ArrayList

class UserInfoCache {

    companion object {
        const val USER_INFO = "infoinfoiminfo"
    }

    private val infoList = ArrayList<DomainUserInfo.DataBean>()

    /**
     * 获取上次登录的账号信息
     */
    fun getLastLoggedAccount(): DomainUserInfo.DataBean? {
        val info: DomainUserInfo.DataBean? = infoList.firstOrNull()
        return if (info != null && info.ualipay != null) {
            info
        } else {
            null
        }
    }

    /**
     * 获取本设备上所有保存的用户
     */
    fun getAllUsersOnThisDevice(): ArrayList<DomainUserInfo.DataBean> {
        return infoList
    }

    /**
     * 更新设备上已有的账号信息
     */
    fun updateUserAccount(info: DomainUserInfo.DataBean?) {
        if (info == null) {
            infoList.removeFirstOrNull()
            return
        }

        for ((index, i) in infoList.withIndex()) {
            if (i.ualipay == info.ualipay) {
                infoList.removeAt(index)
                break
            }
        }

        val first = infoList.firstOrNull()
        if (first != null && first.ualipay != null) {
            infoList.add(0, info)
        } else if (first != null) {
            infoList[0] = info
        }

    }

    /**
     * 退出账号
     */
    fun quitCurrentAccount() {
        updateUserAccount(DomainUserInfo.DataBean())
    }

    /**
     * 删除账号
     */
    fun deleteCurrentAccount() {
        updateUserAccount(null)
    }

    override fun toString(): String {
        return "UserInfoCache{" +
                "info=" + infoList +
                '}'
    }

}