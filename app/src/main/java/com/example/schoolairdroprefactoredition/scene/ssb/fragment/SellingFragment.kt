package com.example.schoolairdroprefactoredition.scene.ssb.fragment

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.core.content.res.ResourcesCompat
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.FragmentSsbBinding
import com.example.schoolairdroprefactoredition.databinding.SheetSsbItemMoreBinding
import com.example.schoolairdroprefactoredition.domain.DomainSelling
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
            return SellingFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putBoolean(ConstantUtil.KEY_IS_MINE, isMine)
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
    override fun init(binding: FragmentSsbBinding?) {
        setHasOptionsMenu(isMine)
        getSelling()
    }

    /**
     * 重试
     */
    override fun retryGrabOnlineData() {
//        getSelling()
    }

    /**
     * 网络请求在售物品
     * 1、若为自己的物品则使用token获取物品
     * 2、若为他人的物品则使用用户id获取
     */
    private fun getSelling() {
        val userID = activity?.intent?.getIntExtra(ConstantUtil.KEY_USER_ID, -1)
        userID?.let { id ->
            if (id != -1) {
                showPlaceholder(StatePlaceHolder.TYPE_LOADING)
                viewModel.getSelling(id).observeOnce(viewLifecycleOwner) {
                    it?.let {
                        loadData(it)
                    }
                }
            } else {
                showPlaceholder(StatePlaceHolder.TYPE_ERROR, getString(R.string.errorLoading))
            }
        }
    }

    /**
     * 在售物品更多设置
     * 修改物品信息 下架物品等
     */
    override fun onItemAction(view: View, bean: DomainSelling.Data?) {
        if (!isMine) return

        context?.let { c ->
            val binding = SheetSsbItemMoreBinding.inflate(LayoutInflater.from(context))
            val token = (activity?.application as SAApplication).getCachedToken()

            dialog = BottomSheetDialog(c)
            dialog?.setContentView(binding.root)
            binding.apply {
                // 修改物品信息按钮
                modify.setOnClickListener {
                    AddNewActivity.startModifyGoods(context, bean?.goods_id)
                    dialog?.dismiss()
                }

                // 下架物品按钮
                offShelf.setOnClickListener {
                    DialogUtil.showConfirm(context, getString(R.string.attention), getString(R.string.unListItem)) {
                        if (token != null) {
                            mLoading?.show()
                            viewModel.deleteGoods(token.access_token, bean?.goods_id.toString())
                                    .observeOnce(viewLifecycleOwner, {
                                        mLoading?.dismissWith {
                                            if (it) {
                                                (activity?.application as SAApplication).getCachedMyInfo()?.userGoodsOnSaleCount?.minus(1)
                                                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successUnlist)
                                            } else {
                                                DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                                            }
                                        }
                                    })
                        } else {
                            DialogUtil.showCenterDialog(context, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorUnknown)
                        }
                    }
                    dialog?.dismiss()
                }

                // 取消按钮
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
                AddNewActivity.startAddNew(context, AddNewActivity.AddNewType.ADD_ITEM)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLoginSSB() {
        getSelling()
    }
}