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
class SettingsNotificationFragment : TransitionBaseFragment(), View.OnClickListener {

    private var displayInAppFloat: PageItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = LayoutInflater.from(context).inflate(R.layout.fragment_settings_notification, container, false)
        displayInAppFloat = root.findViewById(R.id.settings_notification_in_app_float)

        if (UserSettingsCacheUtil.getInstance().isShouldShowFloat()
                && displayInAppFloat?.isItemSelected == false) {
            displayInAppFloat?.toggle()
        }

        displayInAppFloat?.setOnClickListener(this)
        return root
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.settings_notification_in_app_float) {
            displayInAppFloat?.toggle()
            UserSettingsCacheUtil.getInstance().apply {
                saveShouldShowFloat(!isShouldShowFloat())
            }
        }
    }
}