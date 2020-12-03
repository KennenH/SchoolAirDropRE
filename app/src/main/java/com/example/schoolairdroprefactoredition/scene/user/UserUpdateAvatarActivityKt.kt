package com.example.schoolairdroprefactoredition.scene.user

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ImageUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.SheetAvatarBinding
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.base.DomainBaseUserInfo
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel
import com.example.schoolairdroprefactoredition.scene.user.fragment.UserAvatarViewModel
import com.example.schoolairdroprefactoredition.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import javadz.beanutils.BeanUtils
import kotlinx.android.synthetic.main.activity_user_update_avatar.*
import java.util.*

class UserUpdateAvatarActivityKt : AppCompatActivity(), BaseStateViewModel.OnRequestListener, View.OnLongClickListener, View.OnClickListener {

    companion object {
        fun start(context: Context, token: DomainToken, info: Any) {
            val my = DomainBaseUserInfo()

            try {
                BeanUtils.copyProperties(my, info)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(context, UserUpdateAvatarActivityKt::class.java)
            intent.putExtra(ConstantUtil.KEY_USER_INFO, my)
            intent.putExtra(ConstantUtil.KEY_TOKEN, token)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, UserActivity.REQUEST_UPDATE)
            }
        }

        const val REQUEST_ALBUM = 99
        const val REQUEST_CAMERA = 88
    }

    private val mLoading by lazy {
        MyUtil.loading(this@UserUpdateAvatarActivityKt)
    }

    private var info: DomainBaseUserInfo? = null

    private var token: DomainToken? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(UserAvatarViewModel::class.java)
    }

    private val mDialog by lazy {
        BottomSheetDialog(this)
    }

    private val binding by lazy {
        SheetAvatarBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update_avatar)
        setSupportActionBar(toolbar)
        StatusBarUtil.setTranslucentForImageView(this, toolbar)

        token = intent.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken?
        info = intent.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainBaseUserInfo?

        viewModel.setOnRequestListener(this)
        avatar.setOnLongClickListener(this)

        ImageUtil.loadImage(avatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info?.user_img_path, R.drawable.ic_logo_alpha_white)

        mDialog.setContentView(binding.root)
        binding.apply {
            takePhoto.setOnClickListener(this@UserUpdateAvatarActivityKt)
            selectFromAlbum.setOnClickListener(this@UserUpdateAvatarActivityKt)
            saveToAlbum.setOnClickListener(this@UserUpdateAvatarActivityKt)
            cancel.setOnClickListener(this@UserUpdateAvatarActivityKt)
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

    private fun showAvatarDialog() {
        val view: View? = mDialog.delegate.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        view?.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent, theme)
        val bottomSheetBehavior: BottomSheetBehavior<View>
        if (view != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(view)
            bottomSheetBehavior.skipCollapsed = true
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
                REQUEST_ALBUM,
                REQUEST_CAMERA -> { // 相机修改头像回调
                    val photo = PictureSelector.obtainMultipleResult(data)[0]

                    mLoading.show()
                    viewModel.updateAvatar(token?.access_token, photo.androidQToPath ?: photo.path)
                            .observe(this, {
                                updateAvatar(it.user_img_path)
                            })
                }
            }
        }
    }

    private fun updateAvatar(avatarUrl: String) {
        mLoading.dismissWith { DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.SUCCESS, R.string.successAvatar) }
        ImageUtil.loadRoundImage(avatar, ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + avatarUrl)
        info?.user_img_path = avatarUrl

        intent.putExtra(ConstantUtil.KEY_USER_INFO, info)
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onError() {
        runOnUiThread {
            mLoading.dismissWith {
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
            }
        }
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
                val bitmap = FileUtil.urlToBitmap(this, info?.user_img_path)

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