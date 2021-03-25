package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.cache.util.UserSettingsCacheUtil
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.ui.components.PageItem

/**
 * 通知页面
 */
class SettingsNotificationFragment : TransitionBaseFragment(), View.OnClickListener, PageItem.OnPageItemSelectedListener {

    private lateinit var displayInAppFloat: PageItem
    private lateinit var playNotifyInApp: PageItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = LayoutInflater.from(context).inflate(R.layout.fragment_settings_notification, container, false)

        displayInAppFloat = root.findViewById(R.id.settings_notification_in_app_float)
        playNotifyInApp = root.findViewById(R.id.settings_notification_play_notify)

        if (UserSettingsCacheUtil.getInstance().isShouldShowFloat()
                && !displayInAppFloat.isItemSelected) {
            displayInAppFloat.toggle()
        }

        if (UserSettingsCacheUtil.getInstance().isShouldPlaySystemNotification()
                && !playNotifyInApp.isItemSelected) {
            playNotifyInApp.toggle()
        }

        displayInAppFloat.setOnClickListener(this)
        playNotifyInApp.setOnClickListener(this)

        displayInAppFloat.setOnPageItemSelectedListener(this)
        playNotifyInApp.setOnPageItemSelectedListener(this)
        return root
    }

    override fun onClick(v: View) {
        when (v.id) {
            // 是否显示app内弹窗提示
            R.id.settings_notification_in_app_float -> {
                displayInAppFloat.toggle()
            }

            // 是否播放app内声音提示
            R.id.settings_notification_play_notify -> {
                playNotifyInApp.toggle()
            }
        }
    }

    override fun onPageItemToggled(pageItem: PageItem?) {
        when (pageItem) {
            displayInAppFloat -> {
                UserSettingsCacheUtil.getInstance().apply {
                    saveShouldShowFloat(!isShouldShowFloat())
                }
            }

            playNotifyInApp -> {
                UserSettingsCacheUtil.getInstance().apply {
                    saveShouldPlaySystemNotification(!isShouldPlaySystemNotification())
                }
            }
        }
    }
}