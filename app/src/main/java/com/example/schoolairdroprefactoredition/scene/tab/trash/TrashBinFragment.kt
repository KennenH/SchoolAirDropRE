package com.example.schoolairdroprefactoredition.scene.tab.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.viewmodel.TrashBinViewModel

class TrashBinFragment : StatePlaceholderFragment() {

    private var binding: FragmentRecyclerBinding? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(TrashBinViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecyclerBinding.inflate(inflater)

        viewModel.trashBinLoadState.observe(viewLifecycleOwner, {
            when (it) {
                LoadState.LOADING -> {
                    showPlaceholder(StatePlaceHolder.TYPE_LOADING)
                }
                LoadState.SUCCESS -> {
                    showContentContainer()
                }
                LoadState.ERROR -> {
                    showPlaceholder(StatePlaceHolder.TYPE_ERROR)
                }
            }
        })

        return binding?.root
    }

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return binding?.placeholder
    }

    override fun getContentContainer(): View? {
        return binding?.recycler
    }
}