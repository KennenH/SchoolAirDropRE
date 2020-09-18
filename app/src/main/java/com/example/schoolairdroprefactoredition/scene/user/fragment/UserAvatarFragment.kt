package com.example.schoolairdroprefactoredition.scene.user.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize
import com.example.schoolairdroprefactoredition.domain.DomainAvatarUpdateResult
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel.OnRequestListener
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import com.lxj.xpopup.impl.LoadingPopupView
import java.util.*

@Deprecated("")
class UserAvatarFragment : Fragment(), OnLongClickListener, View.OnClickListener, OnRequestListener {
    private var mLoading: LoadingPopupView? = null
    private var mDialog: BottomSheetDialog? = null
    private var mAvatar: SimpleDraweeView? = null
    private var viewModel: UserAvatarViewModel? = null
    private var bundle: Bundle? = null
    private var info: DomainUserInfo.DataBean? = null
    private var token: DomainAuthorize? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = arguments
        if (bundle == null) bundle = Bundle()
        token = bundle!!.getSerializable(ConstantUtil.KEY_AUTHORIZE) as DomainAuthorize?
        info = bundle!!.getSerializable(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = LayoutInflater.from(context).inflate(R.layout.fragment_avatar, container, false)
        viewModel = ViewModelProvider(this).get(UserAvatarViewModel::class.java)
        viewModel!!.setOnRequestListener(this)
        mAvatar = root.findViewById(R.id.avatar)
        mAvatar!!.setOnLongClickListener(this)
        mLoading = MyUtil.loading(context)
        init()
        return root
    }

    private fun init() {
        try {
            mAvatar!!.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info!!.user_img_path)
        } catch (e: NullPointerException) {
            LogUtils.d("info null")
        }
    }

    override fun onLongClick(v: View): Boolean {
        if (v.id == R.id.avatar) {
            showAvatarDialog()
            return true
        }
        return false
    }

    private fun showAvatarDialog() {
        if (mDialog == null) {
            try {
                mDialog = BottomSheetDialog(requireContext())
                mDialog!!.setContentView(LayoutInflater.from(context).inflate(R.layout.sheet_avatar, null))
                mDialog!!.findViewById<View>(R.id.take_photo)!!.setOnClickListener(this)
                mDialog!!.findViewById<View>(R.id.select_from_album)!!.setOnClickListener(this)
                mDialog!!.findViewById<View>(R.id.save_to_album)!!.setOnClickListener(this)
                mDialog!!.findViewById<View>(R.id.cancel)!!.setOnClickListener(this)
                val view1 = mDialog!!.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                view1!!.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent, requireContext().theme)
                val bottomSheetBehavior = BottomSheetBehavior.from<View?>(view1)
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            mDialog!!.dismiss()
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            } catch (e: NullPointerException) {
                LogUtils.d("show dialog null")
            }
        }
        mDialog!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) { // 使用相机修改头像回调
                if (data != null) {
                    val photo = PictureSelector.obtainMultipleResult(data)[0]
                    showLoading()
                    viewModel!!.updateAvatar(token!!.access_token, photo.path).observe(viewLifecycleOwner, Observer { bean: DomainAvatarUpdateResult ->
                        dismissLoading()
                        updateAvatar(bean.user_img_path)
                    })
                }
            } else if (requestCode == REQUEST_ALBUM) { // 使用相册选择图片修改头像回调
                if (data != null) {
                    val photo = PictureSelector.obtainMultipleResult(data)[0]
                    showLoading()
                    viewModel!!.updateAvatar(token!!.access_token, photo.path).observe(viewLifecycleOwner, Observer { bean: DomainAvatarUpdateResult ->
                        dismissLoading()
                        updateAvatar(bean.user_img_path)
                    })
                }
            }
        }
    }

    private fun updateAvatar(avatarUrl: String) {
        info!!.user_img_path = avatarUrl
        if (activity != null) {
            requireActivity().intent.putExtra(ConstantUtil.KEY_USER_INFO, info)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.take_photo -> {
                MyUtil.takePhoto(this, REQUEST_CAMERA)
                mDialog!!.dismiss()
            }
            R.id.select_from_album -> {
                MyUtil.pickPhotoFromAlbum(this, REQUEST_ALBUM, ArrayList(), 1, false)
                mDialog!!.dismiss()
            }
            R.id.save_to_album -> {
                // todo 保存图片至相册
                Toast.makeText(context, R.string.imageSaved, Toast.LENGTH_SHORT).show()
                mDialog!!.dismiss()
            }
            R.id.cancel -> mDialog!!.dismiss()
            else -> {
            }
        }
    }

    private fun showLoading() {
        if (mLoading == null) mLoading = MyUtil.loading(context)
        mLoading!!.show()
    }

    private fun dismissLoading() {
        if (mLoading != null) mLoading!!.dismiss()
    }

    override fun onError() {
        requireActivity().runOnUiThread { mLoading!!.dismiss() }
        Toast.makeText(context, R.string.errorUpdateAvatar, Toast.LENGTH_SHORT).show()
    }

    override fun onLoading() {}

    companion object {
        const val REQUEST_ALBUM = 99
        const val REQUEST_CAMERA = 88
        fun newInstance(bundle: Bundle?): UserAvatarFragment {
            val fragment = UserAvatarFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}