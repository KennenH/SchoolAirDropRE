package com.example.schoolairdroprefactoredition.scene.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * app所有activity页面中的父类
 *
 * 用于后续添加通用方法
 */
open class BaseActivity : AppCompatActivity() {

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