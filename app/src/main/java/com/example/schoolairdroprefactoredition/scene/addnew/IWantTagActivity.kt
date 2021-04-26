package com.example.schoolairdroprefactoredition.scene.addnew

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.ui.components.IWantTag
import com.example.schoolairdroprefactoredition.utils.AnimUtil
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.viewmodel.IWantTagViewModel
import kotlinx.android.synthetic.main.activity_select_tag.*

class IWantTagActivity : ImmersionStatusBarActivity(), IWantTag.OnIWantTagActionListener {

    companion object {

        /**
         * 需要从本页面传递出去tag id 请求码
         */
        const val REQUEST_CODE_IWANT_TAG = 90

        /**
         * 本页面传递已选择的tag时使用的键
         */
        const val KEY_SELECTED_TAG = "selected_tag"

        @JvmStatic
        fun start(context: Context?, selectedTag: DomainIWantTags.Data?) {
            if (context == null) return

            val intent = Intent(context, IWantTagActivity::class.java)
            intent.putExtra(KEY_SELECTED_TAG, selectedTag)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, REQUEST_CODE_IWANT_TAG)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    private val iwantTagViewModel by lazy {
        ViewModelProvider(this).get(IWantTagViewModel::class.java)
    }

    /**
     * 当前选中的tag
     */
    private var nowTag: DomainIWantTags.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tag)
        init()
    }

    fun init() {
        nowTag = intent.getSerializableExtra(KEY_SELECTED_TAG) as DomainIWantTags.Data?

        confirm.isEnabled = false
        confirm.setOnClickListener {
            if (nowTag != null) {
                setResult(Activity.RESULT_OK, Intent().also {
                    it.putExtra(KEY_SELECTED_TAG, nowTag)
                })
                finish()
                AnimUtil.activityExitAnimDown(this)
            } else {
                Toast.makeText(this, R.string.selectTagFirst, Toast.LENGTH_SHORT).show()
            }
        }
        cancel.setOnClickListener {
            finish()
            AnimUtil.activityExitAnimDown(this)
        }

        showLoading()
        iwantTagViewModel.getIWantTags().observeOnce(this) {
            if (it == null) {
                DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.dialogFailed)
            } else {
                select_tag_flex_box.removeAllViews()
                for (tag in it.data) {
                    IWantTag(this, tag).also { iwantTag ->
                        select_tag_flex_box.addView(iwantTag)
                        if (tag.tags_id == nowTag?.tags_id
                                && tag.tags_content == nowTag?.tags_content) {
                            confirm.isEnabled = true
                            iwantTag.isSelected = true
                            IWantTag.setselectedTagPos(select_tag_flex_box.indexOfChild(iwantTag))
                        }
                        iwantTag.setOnIWantTagActionListener(this)
                    }
                }
            }
            dismissLoading()
        }
    }

    override fun onIWantTagClick(tag: DomainIWantTags.Data) {
        if (!confirm.isEnabled) {
            confirm.isEnabled = true
        }
        nowTag = tag
    }


}