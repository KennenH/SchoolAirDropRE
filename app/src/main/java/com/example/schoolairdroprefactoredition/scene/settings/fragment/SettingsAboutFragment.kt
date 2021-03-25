package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSettingsAboutBinding
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.ui.components.PageItem
import com.example.schoolairdroprefactoredition.utils.MyUtil

/**
 * 关于页面
 */
class SettingsAboutFragment : TransitionBaseFragment(), View.OnClickListener {

    private var binding: FragmentSettingsAboutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsAboutBinding.inflate(inflater, container, false)

        binding?.settingsAboutVersion?.setOnClickListener(this)
        binding?.settingsAboutFeedback?.setOnClickListener(this)
        binding?.settingsAboutPrivacy?.setOnClickListener(this)
        binding?.settingsAboutService?.setOnClickListener(this)
        binding?.settingsAboutVersion?.text = getString(R.string.version, "1.0")

        return binding?.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.settings_about_version -> {

            }
            R.id.settings_about_feedback -> {
                // 发送邮箱或app内文字加图片形式
            }
            R.id.settings_about_privacy -> {
                MyUtil.openPrivacyWebsite(context)
            }
            R.id.settings_about_service -> {
                MyUtil.openServiceWebsite(context)
            }
        }
    }
}