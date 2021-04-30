package com.example.schoolairdroprefactoredition.scene.ssb.fragment

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding
import com.example.schoolairdroprefactoredition.databinding.SheetSsbItemMoreBinding
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.domain.DomainUserIWant
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.ui.adapter.MyIWantAdapter
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


/**
 * @author kennen
 * @date 2021/4/27
 *
 * 用户求购页面
 */
class UserIWantsFragment :
        SSBBaseFragment(),
        SSBActivity.OnLoginStateChangeListener,
        MyIWantAdapter.OnSSBIWantItemActionListener {

    companion object {
        fun newInstance(isMine: Boolean): UserIWantsFragment {
            return UserIWantsFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putBoolean(ConstantUtil.KEY_IS_MINE, isMine)
                }
            }
        }
    }

    private val mIWantAdapter by lazy {
        MyIWantAdapter(activity?.intent?.getBooleanExtra(ConstantUtil.KEY_IS_MINE, false))
    }

    private var mIWantList: ArrayList<DomainUserIWant.Data> = ArrayList()

    private var dialog: BottomSheetDialog? = null

    private val isMine by lazy {
        arguments?.getBoolean(ConstantUtil.KEY_IS_MINE, false) ?: false
    }

    private var lastClickTime: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (activity is SSBActivity) {
            (activity as SSBActivity).setOnLoginStateChangeListener(this)
        }
    }

    override fun init(binding: FragmentSsbBinding?) {
        binding?.ssbRecycler?.adapter = mIWantAdapter
        mIWantAdapter.setOnSSBIWantItemActionListener(this)

        setHasOptionsMenu(isMine)
        getIWant()
    }

    override fun retryGrabOnlineData() {
        getIWant()
    }

    private fun getIWant() {
        val userID = activity?.intent?.getIntExtra(ConstantUtil.KEY_USER_ID, -1)
        userID?.let { id ->
            if (id != -1) {
                showPlaceholder(StatePlaceHolder.TYPE_LOADING)
                viewModel.getIWant(id).observeOnce(viewLifecycleOwner) {
                    if (it != null) {
                        loadData(it)
                    } else {
                        if (mIWantAdapter.data.isEmpty()) {
                            showPlaceholder(StatePlaceHolder.TYPE_ERROR, getString(R.string.errorLoading))
                        } else {
                            Toast.makeText(context, getString(R.string.errorLoading), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                showPlaceholder(StatePlaceHolder.TYPE_ERROR, getString(R.string.errorLoading))
            }
        }
    }

    override fun onItemAction(view: View, bean: Any?) {
        if (!isMine || bean !is DomainUserIWant.Data?) return

        context?.let {
            val binding = SheetSsbItemMoreBinding.inflate(LayoutInflater.from(it))
            val token = (activity?.application as SAApplication).getCachedToken()

            dialog = BottomSheetDialog(it)
            dialog?.setContentView(binding.root)
            binding.apply {
                // 修改求购信息按钮
                modify.setOnClickListener {
                    AddNewActivity.startModifyIWant(context, bean)
                    dialog?.dismiss()
                }

                offShelf.text = getString(R.string.undoIWant)
                // 撤销求购
                offShelf.setOnClickListener {
                    DialogUtil.showConfirm(context, getString(R.string.attention), getString(R.string.sureToUndoIWant)) {
                        if (token != null) {
                            mLoading?.show()
                            viewModel.deleteIWant(token.access_token, bean?.iwant_id.toString())
                                    .observeOnce(viewLifecycleOwner) {
                                        mLoading?.dismissWith {
                                            if (it) {
                                                (activity?.application as SAApplication).getCachedMyInfo()?.userContactCount?.minus(1)
                                                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successUndoIWant)
                                            } else {
                                                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                                            }
                                        }
                                    }
                        } else {
                            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                        }
                    }
                    dialog?.dismiss()
                }

                // 取消按钮
                cancel.setOnClickListener { dialog?.dismiss() }

                val bottomSheetView = dialog?.delegate?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheetView?.let { view ->
                    view.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent, context?.theme)
                    BottomSheetBehavior.from(view).run {
                        skipCollapsed = true
                        isDraggable = false
                        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                            override fun onStateChanged(bottomSheet: View, newState: Int) {
                                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                    dialog?.dismiss()
                                    state = BottomSheetBehavior.STATE_COLLAPSED
                                }
                            }

                            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                        })
                    }
                }
            }
            dialog?.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isMine) {
            inflater.inflate(R.menu.ssb, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isMine) {
            return false
        }

        if (SystemClock.elapsedRealtime() - lastClickTime < ConstantUtil.MENU_CLICK_GAP) {
            return false
        }

        lastClickTime = SystemClock.elapsedRealtime()

        when (item.itemId) {
            R.id.toolbar -> {
                activity?.supportFragmentManager?.popBackStack()
                return true
            }

            R.id.ssb_posts_add -> {
                AddNewActivity.startAddNew(context, AddNewActivity.AddNewType.ADD_IWANT)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(data: DomainUserIWant) {
        mIWantList.addAll(data.data)
        if (mIWantList.size == 0) {
            showPlaceholder(StatePlaceHolder.TYPE_EMPTY, getString(R.string.goPostUItem))
        } else {
            mIWantAdapter.setList(mIWantList)
            showContentContainer()
        }
    }

    override fun onLoginSSB() {
        getIWant()
    }

    override fun onItemActionButtonClick(view: View, bean: DomainUserIWant.Data?) {
        onItemAction(view, bean)
    }
}