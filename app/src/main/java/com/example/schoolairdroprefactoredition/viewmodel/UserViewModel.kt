package com.example.schoolairdroprefactoredition.viewmodel

import androidx.lifecycle.*
import com.example.schoolairdroprefactoredition.database.pojo.UserCache
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.repository.DatabaseRepository
import com.example.schoolairdroprefactoredition.repository.UserRepository
import com.example.schoolairdroprefactoredition.cache.JsonCacheConstantUtil.IS_GET_USER_INFO_PRESENTLY
import com.example.schoolairdroprefactoredition.cache.JsonCacheUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {

    class UserViewModelFactory(private val repository: DatabaseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private val userRepository by lazy {
        UserRepository.getInstance()
    }

    private val jsonCacheUtil by lazy {
        JsonCacheUtil.getInstance()
    }

    /**
     * 该live data返回的数据尽量趋近于数据库中的用户数据，不建议频繁调用
     *
     * 代价是频繁调用网络请求，增加服务器负担
     */
    private val userInfoLiveData = MutableLiveData<DomainUserInfo.DataBean>()

    /**
     * 使用userID获取用户信息
     *
     * @param isActive 是否尽量保持用户信息和服务器上的一致
     * true 与服务器数据尽量保持一致，代价是可能会频繁调用，给服务器带来压力  适合在用户个人主页使用
     * false 只要本地有缓存返回即可，无需保证数据与服务器一致               适合在聊天相关的页面使用
     */
    fun getUserInfo(userID: Int, isActive: Boolean = false): LiveData<DomainUserInfo.DataBean?> {
        viewModelScope.launch {
            // 先用户信息获取缓存显示
            databaseRepository.getUserCache(userID).let { cache ->
                if (cache?.user_avatar != null && cache.user_name != null) {
                    // 缓存若存在则直接返回
                    userInfoLiveData.value = DomainUserInfo.DataBean().also {
                        it.userId = cache.user_id
                        it.userName = cache.user_name
                        it.userAvatar = cache.user_avatar
                        it.createtime = cache.user_create_time ?: -1
                        it.userGoodsOnSaleCount = cache.user_goods_count ?: 0
                        it.userContactCount = cache.user_post_count ?: 0
                    }

                    if (isActive) {
                        // 检查对于该用户是否在3分钟之内获取过个人主页，若是则无需进行请求
                        jsonCacheUtil.getCache(IS_GET_USER_INFO_PRESENTLY + userID, Boolean::class.java).let {
                            if (it == null) {
                                getUserInfoOnline(userID)
                            }
                        }
                    }
                } else {
                    getUserInfoOnline(userID)
                }
            }
        }
        return userInfoLiveData
    }

    /**
     * 网络请求用户信息
     */
    private fun getUserInfoOnline(userID: Int) {
        // 再获取网络信息
        userRepository.getUserInfoById(userID) { success, response ->
            if (success) {
                // 网络获取成功后缓存用户信息
                response?.let {
                    val userCache = UserCache(it.userId, it.userName, it.userAvatar, it.createtime, it.userGoodsOnSaleCount, it.userContactCount)
                    viewModelScope.launch {
                        databaseRepository.saveUserCache(userCache)
                    }
                }
                userInfoLiveData.postValue(response)
            } else {
                userInfoLiveData.postValue(null)
            }
        }
    }
}