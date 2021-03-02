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

/**
 * 关于页面
 */
class SettingsAboutFragment : TransitionBaseFragment(), View.OnClickListener {

    private var mVersion: TextView? = null

    private var mFeedback: PageItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingsAboutBinding.inflate(inflater, container, false)
        mVersion = binding.settingsAboutVersion
        mFeedback = binding.settingsAboutFeedback
        mVersion?.setOnClickListener(this)
        mFeedback?.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.settings_about_version) {

        } else if (id == R.id.settings_about_feedback) {
            // 发送邮箱或app内文字加图片形式
        }
    }
}