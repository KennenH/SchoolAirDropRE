package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.cache.util.UserSettingsCacheUtil
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.ui.components.PageItem
import kotlinx.android.synthetic.main.fragment_settings_general.*

class SettingsGeneralFragment : TransitionBaseFragment(),
        View.OnClickListener, PageItem.OnPageItemSelectedListener {

    /**
     * 暗黑模式按钮是否已经初始化
     */
    private var isDarkThemeInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_settings_general, container, false).rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (UserSettingsCacheUtil.getInstance().isDarkTheme()) {
            settingsGeneralDarkTheme.select()
        }
        isDarkThemeInitialized = true

        settingsGeneralStorage.setOnClickListener(this@SettingsGeneralFragment)
        settingsGeneralDarkTheme.setOnClickListener(this@SettingsGeneralFragment)
        settingsGeneralDarkTheme.setOnPageItemSelectedListener(this@SettingsGeneralFragment)
    }

    override fun onClick(v: View?) {
        when (v) {
            settingsGeneralStorage -> {
            }

            settingsGeneralDarkTheme -> {
                settingsGeneralDarkTheme.toggle()
            }
        }
    }

    override fun onPageItemToggled(pageItem: PageItem?) {
        when (pageItem) {
            settingsGeneralDarkTheme -> {
                if (isDarkThemeInitialized) {
                    if (activity?.application is SAApplication) {
                        (activity?.application as SAApplication).setAppTheme(settingsGeneralDarkTheme.isItemSelected)
                    }
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }
    }
}