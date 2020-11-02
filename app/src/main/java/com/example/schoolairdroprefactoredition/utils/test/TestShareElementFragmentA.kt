package com.example.schoolairdroprefactoredition.utils.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.example.schoolairdroprefactoredition.R

class TestShareElementFragmentA : Fragment() {

    private var listener: OnIconClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.test_fragment_a, container, false)
        root.findViewById<View>(R.id.icon).setOnClickListener{
            listener?.onIconClick()
        }

        return root.rootView
    }

    interface OnIconClickListener {
        fun onIconClick()
    }

    fun setOnIconClickListener(listener: OnIconClickListener) {
        this.listener = listener
    }

}