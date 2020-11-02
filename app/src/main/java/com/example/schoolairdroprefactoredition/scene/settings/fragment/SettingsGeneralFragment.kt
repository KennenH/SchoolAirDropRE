package com.example.schoolairdroprefactoredition.scene.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.Application
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseFragment
import com.example.schoolairdroprefactoredition.ui.components.PageItem
import com.example.schoolairdroprefactoredition.viewmodel.SettingsGeneralViewModel
import kotlinx.android.synthetic.main.fragment_settings_general.*

class SettingsGeneralFragment : TransitionBaseFragment(),
        View.OnClickListener, PageItem.OnPageItemSelectedListener {

    private val generalViewModel by lazy {
        ViewModelProvider(this).get(SettingsGeneralViewModel::class.java)
    }

    private val languageName by lazy {
        resources.getString(R.string.language)
    }

    companion object {
        fun newInstance(bundle: Bundle): SettingsGeneralFragment {
            val fragment = SettingsGeneralFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_settings_general, container, false).rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (generalViewModel.getIsDarkTheme().value == true)
            settingsGeneralDarkTheme.select()

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

    override fun onPageItemToggled() {
        if (activity?.application is Application) {
            (activity?.application as Application).setAppTheme(settingsGeneralDarkTheme.isItemSelected)
        }
        activity?.supportFragmentManager?.popBackStack()
    }
}