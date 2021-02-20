package com.example.schoolairdroprefactoredition.scene.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * app中所有fragment页面的父类
 *
 * 用于后续添加一些通用方法
 */
open class BaseFragment : Fragment() {

    /**
     * 仅观察一次的观察者
     */
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }
}