package com.example.schoolairdroprefactoredition.scene.ssb.fragment

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.core.content.res.ResourcesCompat
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding
import com.example.schoolairdroprefactoredition.databinding.SheetSsbItemMoreBinding
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity
import com.example.schoolairdroprefactoredition.scene.ssb.SSBActivity.OnLoginStateChangeListener
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog

class SellingFragment : SSBBaseFragment(), OnLoginStateChangeListener {

    companion object {
        fun newInstance(isMine: Boolean): SellingFragment {
            return SellingFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ConstantUtil.KEY_IS_MINE, isMine)
                }
            }
        }
    }

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

    /**
     * 初始化
     */
    override fun init(binding: FragmentSsbBinding) {
        setHasOptionsMenu(isMine)
        getSelling()
    }

    /**
     * 重试
     */
    override fun retryGrabOnlineData() {
        getSelling()
    }

    /**
     * 网络请求在售物品
     * 1、若为自己的物品则使用token获取物品
     * 2、若为他人的物品则使用用户id获取
     */
    private fun getSelling() {
        userInfo.let { info ->
            if (info != null) {
                showPlaceholder(StatePlaceHolder.TYPE_LOADING)
                viewModel.getSelling(info.userId).observeOnce(viewLifecycleOwner) {
                    it?.let {
                        loadData(it)
                        dataLenOnChange(SELLING_POS)
                    }
                }
            } else showPlaceholder(StatePlaceHolder.TYPE_ERROR)
        }
    }

    /**
     * 在售物品更多设置
     * 修改物品信息 下架物品等
     */
    public override fun onItemAction(view: View, bean: DomainPurchasing.DataBean) {
        if (isMine) return

        context?.let { c ->
            dialog = BottomSheetDialog(c)
            dialog?.setContentView(binding.root)

            val binding = SheetSsbItemMoreBinding.inflate(LayoutInflater.from(context))
            binding.apply {
                modify.setOnClickListener {
                    AddNewActivity.start(context, bean.goods_id)
                    dialog?.dismiss()
                }

                offShelf.setOnClickListener {
                    DialogUtil.showConfirm(context, getString(R.string.attention), getString(R.string.unListItem)) {
                        if (token != null) {
                            viewModel.deleteGoods(token.access_token, bean.goods_id.toString())
                                    .observeOnce(viewLifecycleOwner, {
                                        if (it) {
                                            getSelling()
                                            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successUnlist)
                                        } else {
                                            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                                        }
                                    })
                        } else {
                            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                        }
                    }
                    dialog?.dismiss()
                }

                cancel.setOnClickListener { dialog?.dismiss() }

                val bottomSheetView = dialog?.delegate?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheetView?.let {
                    it.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent, context?.theme)
                    BottomSheetBehavior.from(it).run {
                        skipCollapsed = true
                        isDraggable = false
                        addBottomSheetCallback(object : BottomSheetCallback() {
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

            R.id.selling_posts_add -> {
                AddNewActivity.start(context, AddNewActivity.AddNewType.ADD_ITEM)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLoginSSB() {
        getSelling()
    }
}