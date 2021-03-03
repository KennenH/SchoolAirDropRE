package com.example.schoolairdroprefactoredition.cache

import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import kotlin.collections.ArrayList

class UserInfoCache {

    companion object {
        const val KEY = "infoinfoiminfo"
    }

    private val userList = ArrayList<DomainUserInfo.DataBean>()

    /**
     * 获取上次登录的账号信息
     */
    fun getLastLoggedAccount(): DomainUserInfo.DataBean? {
        return userList.firstOrNull()
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
     * @param info 传入null时删除当前账号
     */
    fun updateUserAccount(info: DomainUserInfo.DataBean?) {
        // 传入null，则将第一个账号（当前）删除
        if (info == null) {
            userList.removeFirstOrNull()
            return
        }

        // 将已存在的相同账号先删除，也是为了省略排序和重新组装数据
        for ((index, i) in userList.withIndex()) {
            if (i.userId == info.userId) {
                userList.removeAt(index)
                break
            }
        }

        // 添加当前账号至第一个
        userList.add(0, info)
    }
}