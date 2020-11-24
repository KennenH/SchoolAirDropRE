package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import kotlin.collections.ArrayList

class UserInfoCache {

    companion object {
        const val USER_INFO = "infoinfoiminfo"
    }

    private val userList = ArrayList<DomainUserInfo.DataBean>()

    /**
     * 获取上次登录的账号信息
     */
    fun getLastLoggedAccount(): DomainUserInfo.DataBean? {
        val info: DomainUserInfo.DataBean? = userList.firstOrNull()
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
        return userList
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

    /**
     * 更新设备上已有的账号信息
     *
     * 传入null时删除当前账号
     *
     */
    fun updateUserAccount(info: DomainUserInfo.DataBean?) {
        if (info == null) {
            userList.removeFirstOrNull()
            return
        }

        for ((index, i) in userList.withIndex()) {
            if (i.ualipay == info.ualipay) {
                userList.removeAt(index)
                break
            }
        }

        // 当没有缓存账号或者第一个账号不为空白替代账号时直接在头部添加账号信息
        // 否则，第一个账号应该是空白替代账号，直接将其替代
        val first = userList.firstOrNull()
        if (first == null || first.ualipay != null) {
            userList.add(0, info)
        } else {
            userList[0] = info
        }
    }

    override fun toString(): String {
        return "UserInfoCache{" +
                "info=" + userList +
                '}'
    }

}