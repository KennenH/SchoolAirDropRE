package com.example.schoolairdroprefactoredition.scene.addnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ActivityAddNewSuccessBinding
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.AnimUtil

class AddNewResultActivity : ImmersionStatusBarActivity() {

    companion object {
        private const val IS_SUCCESS = "?isSuccess"
        private const val PAGE_TIP = "?pageTip"

        private const val ICON_SUCCESS = R.drawable.ic_success
        private const val ICON_FAILED = R.drawable.ic_failed

        private const val TITLE_ADD_NEW_SUCCESS = R.string.submitSuccess
        private const val TITLE_ADD_NEW_FAILED = R.string.submitFailed

        /**
         * @param isSuccess 是否显示为成功页面
         * @param tip       页面提示 one of [AddNewResultActivity.AddNewResultTips]
         */
        fun start(context: Context, isSuccess: Boolean, @AddNewResultTips tip: Int) {
            val intent = Intent(context, AddNewResultActivity::class.java)
            intent.putExtra(IS_SUCCESS, isSuccess)
            intent.putExtra(PAGE_TIP, tip)
            context.startActivity(intent)
            if (context is AppCompatActivity) {
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    /**
     * 页面提示标语
     */
    annotation class AddNewResultTips {
        companion object {
            var SUCCESS_NEW_ITEM = R.string.submitSuccessTip
            var SUCCESS_MODIFY_ITEM = R.string.modifySuccessTip
            var FAILED_ADD = R.string.submitFailedTip
            var FAILED_MODIFY = R.string.modifyFailedTip
            var FAILED_PREPARE = R.string.picturePrepareFailed
            var LOCATION_FAILED_NEW_ITEM = R.string.submitItemLocationFailedTip
            var LOCATION_FAILED_MODIFY_ITEM = R.string.modifyItemLocationFailedTip
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddNewSuccessBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val intent = intent
        var isSuccess = false
        var pageTip = AddNewResultTips.FAILED_ADD
        if (intent != null) {
            pageTip = intent.getIntExtra(PAGE_TIP, pageTip)
            isSuccess = intent.getBooleanExtra(IS_SUCCESS, false)
        }
        if (isSuccess) {
            binding.resultWhy.visibility = View.GONE
            binding.resultTitle.text = getString(TITLE_ADD_NEW_SUCCESS)
            binding.resultIcon.setImageResource(ICON_SUCCESS)
        } else {
            // TODO: 2020/11/27 为什么出错页面
            binding.resultWhy.visibility = View.GONE
            binding.resultTitle.text = getString(TITLE_ADD_NEW_FAILED)
            binding.resultIcon.setImageResource(ICON_FAILED)
        }
        binding.resultTip.text = getString(pageTip)
        binding.resultConfirm.setOnClickListener { v: View? ->
            finish()
            AnimUtil.activityExitAnimDown(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AnimUtil.activityExitAnimDown(this)
    }
}