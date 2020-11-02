package com.example.schoolairdroprefactoredition.utils.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.example.schoolairdroprefactoredition.R

class TestShareElementFragmentB : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.share_element)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.test_fragment_b, container, false)
        return root.rootView
    }
}