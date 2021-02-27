package com.example.schoolairdroprefactoredition.scene.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ImageUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.databinding.SheetAvatarBinding
import com.example.schoolairdroprefactoredition.scene.base.BaseActivity
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.viewmodel.UserAvatarViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import kotlinx.android.synthetic.main.activity_user_update_avatar.*
import java.util.*

class UserUpdateAvatarActivity : BaseActivity(), View.OnLongClickListener, View.OnClickListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserUpdateAvatarActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, UserActivity.REQUEST_UPDATE_MY)
            }
        }

        const val REQUEST_ALBUM = 4734
        const val REQUEST_CAMERA = 7423
    }

    private val mLoading by lazy {
        MyUtil.loading(this@UserUpdateAvatarActivity)
    }

    private val info by lazy {
        (application as SAApplication).getCachedMyInfo()
    }

    private val token by lazy {
        (application as SAApplication).getCachedToken()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(UserAvatarViewModel::class.java)
    }

    private val mDialog by lazy {
        BottomSheetDialog(this)
    }

    private val bottomSheetBinding by lazy {
        SheetAvatarBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update_avatar)
        setSupportActionBar(toolbar)
        StatusBarUtil.setTranslucentForImageView(this, toolbar)

        avatar.setOnLongClickListener(this)

        ImageUtil.loadImage(avatar, ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(info?.userAvatar), R.drawable.ic_logo_alpha_white)

        mDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.apply {
            takePhoto.setOnClickListener(this@UserUpdateAvatarActivity)
            selectFromAlbum.setOnClickListener(this@UserUpdateAvatarActivity)
            saveToAlbum.setOnClickListener(this@UserUpdateAvatarActivity)
            cancel.setOnClickListener(this@UserUpdateAvatarActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.more, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.more -> {
                showAvatarDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 显示上传头像的选项，可以相册和拍照
     */
    private fun showAvatarDialog() {
        val view = mDialog.delegate.findViewById<View?>(com.google.android.material.R.id.design_bottom_sheet)
        if (view != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from(view)
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isDraggable = false
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    mDialog.dismiss()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            mDialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ALBUM, // 相册选择头像回调
                REQUEST_CAMERA -> { // 相机修改头像回调
                    val photo = PictureSelector.obtainMultipleResult(data)[0]

                    mLoading.show()
                    viewModel.updateAvatar(token?.access_token, photo.androidQToPath
                            ?: photo.path)
                            .observeOnce(this, {
                                if (it != null) {
                                    updateAvatar(it)
                                } else {
                                    mLoading.dismissWith {
                                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.systemBusy)
                                    }
                                }
                            })
                }
            }
        }
    }

    private fun updateAvatar(avatarUrl: String) {
        mLoading.dismissWith { DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successAvatar) }
        ImageUtil.loadImage(avatar,
                ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(avatarUrl),
                R.drawable.ic_logo_alpha_white)
        info?.userAvatar = avatarUrl

        intent.putExtra(ConstantUtil.KEY_USER_INFO, info)
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onLongClick(v: View?): Boolean {
        if (v?.id == R.id.avatar) {
            showAvatarDialog()
            return true
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.take_photo -> {
                MyUtil.takePhoto(this, REQUEST_CAMERA, isCircle = true)
                mDialog.dismiss()
            }
            R.id.select_from_album -> {
                MyUtil.pickPhotoFromAlbum(this,
                        REQUEST_ALBUM,
                        1,
                        isSquare = true,
                        isCircle = true)
                mDialog.dismiss()
            }
            R.id.save_to_album -> {
                val bitmap = FileUtil.urlToBitmap(this, info?.userAvatar)

                if (bitmap != null) {
                    ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG)
                    DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successSave)
                } else {
                    DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.errorUnknown)
                }
                mDialog.dismiss()
            }
            R.id.cancel -> {
                mDialog.dismiss()
            }
        }
    }
}