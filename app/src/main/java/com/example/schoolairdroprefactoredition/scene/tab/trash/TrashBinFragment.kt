package com.example.schoolairdroprefactoredition.scene.tab.trash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.databinding.FragmentRecyclerBinding
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainTrashBin
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.tab.TabBaseFragment
import com.example.schoolairdroprefactoredition.ui.adapter.TrashBinRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil.KEY_ARG_SECTION_NUMBER
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.TrashBinViewModel

class TrashBinFragment : TabBaseFragment() {

    companion object {
        fun newInstance(index: Int): TrashBinFragment {
            val fragment = TrashBinFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mAdapter by lazy {
        TrashBinRecyclerAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(TrashBinViewModel::class.java)
    }

    override fun init(binding: FragmentRecyclerBinding?, index: Int?) {
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

        mAdapter.setIndex(index)
        val manager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.recycler?.addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(8f), false))
        binding?.recycler?.layoutManager = manager
        binding?.recycler?.adapter = mAdapter
    }

    override fun loadFirstTabData(binding: FragmentRecyclerBinding?, token: DomainAuthorize) {
        binding?.placeholder?.setPlaceholderType(StatePlaceHolder.TYPE_LOADING)
        viewModel.getCorrupted(token.access_token).observe(this, {
            if (it.data.isEmpty()) {
                binding?.placeholder?.setPlaceholderType(StatePlaceHolder.TYPE_EMPTY)
            } else {
                mAdapter.setList(it.data)
            }
        })

    }

    override fun loadSecondTabData(binding: FragmentRecyclerBinding?, token: DomainAuthorize) {
        binding?.placeholder?.setPlaceholderType(StatePlaceHolder.TYPE_LOADING)
        viewModel.getAccomplished(token.access_token).observe(this, {
            if (it.data.isEmpty()) {
                binding?.placeholder?.setPlaceholderType(StatePlaceHolder.TYPE_EMPTY)
            } else {
                mAdapter.setList(it.data)
            }
        })

    }
}