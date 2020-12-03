package com.example.schoolairdroprefactoredition.scene.main.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolairdroprefactoredition.databinding.FragmentMessagesBinding
import com.example.schoolairdroprefactoredition.domain.base.LoadState
import com.example.schoolairdroprefactoredition.scene.base.StatePlaceholderFragment
import com.example.schoolairdroprefactoredition.scene.main.MainActivity
import com.example.schoolairdroprefactoredition.ui.adapter.HomeMessagesRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.viewmodel.MessagesViewModel

class MessagesFragment : StatePlaceholderFragment(), MainActivity.OnLoginStateChangedListener {

    private val homeMessagesRecyclerAdapter by lazy {
        HomeMessagesRecyclerAdapter()
    }

    private val messagesViewModel by lazy {
        ViewModelProvider(this@MessagesFragment).get(MessagesViewModel::class.java)
    }

    private var binding: FragmentMessagesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is MainActivity) {
            (activity as MainActivity).addOnLoginActivityListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)

        binding?.messagesRecycler?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = homeMessagesRecyclerAdapter
        }

        init()

        return binding?.root
    }

    private fun init() {
        val token = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        if (token == null) {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
        }

        messagesViewModel.messagesLoadState.observe(viewLifecycleOwner, {
            when (it) {
                LoadState.LOADING -> {
                    showPlaceholder(StatePlaceHolder.TYPE_LOADING)
                }
                LoadState.SUCCESS -> {
                    showContentContainer()
                }
                LoadState.ERROR -> {

                }
            }
        })

        binding?.messagesRefresh?.setOnRefreshListener {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
            it.finishRefresh()
        }
    }

    override fun getStatePlaceholder(): StatePlaceHolder? {
        return binding?.messagesPlaceholder
    }

    override fun getContentContainer(): View? {
        return binding?.messagesRecycler
    }

    override fun onLoginStateChanged() {
        val token = activity?.intent?.getSerializableExtra(ConstantUtil.KEY_TOKEN)
        if (token != null) {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
//            messagesViewModel.getMessagesList().observe(viewLifecycleOwner, {
//
//            })
        } else {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
        }
    }


}