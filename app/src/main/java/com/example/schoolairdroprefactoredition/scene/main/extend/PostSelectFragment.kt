package com.example.schoolairdroprefactoredition.scene.main.extend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.BaseFragment

class PostSelectFragment private constructor() : BaseFragment() {

    companion object {
        private var INSTANCE: PostSelectFragment? = null
        @JvmStatic
        fun getInstance(): PostSelectFragment {
            return INSTANCE
                    ?: PostSelectFragment().also {
                        INSTANCE = it
                    }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_post_select, container, false)
    }

}