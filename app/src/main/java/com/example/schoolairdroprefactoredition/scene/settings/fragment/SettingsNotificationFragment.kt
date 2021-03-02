package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.ui.components.PageItem
import com.example.schoolairdroprefactoredition.viewmodel.SettingsViewModel

/**
 * 通知页面
 */
class SettingsNotificationFragment : TransitionBaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance(bundle: Bundle?): SettingsNotificationFragment {
            val fragment = SettingsNotificationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var displayInAppFloat: PageItem? = null

    private val settingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = LayoutInflater.from(context).inflate(R.layout.fragment_settings_notification, container, false)
        displayInAppFloat = root.findViewById(R.id.settings_notification_in_app_float)
        displayInAppFloat?.setOnClickListener(this)
        return root
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.settings_notification_in_app_float) {
            displayInAppFloat?.toggle()
            settingsViewModel.toggleDisplayFloat()
        }
    }
}