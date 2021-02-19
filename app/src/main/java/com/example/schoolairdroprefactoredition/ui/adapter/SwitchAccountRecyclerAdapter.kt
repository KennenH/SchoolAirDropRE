package com.example.schoolairdroprefactoredition.ui.adapter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.utils.AnimUtil
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil

class SwitchAccountRecyclerAdapter(val info: DomainUserInfo.DataBean?) : BaseQuickAdapter<DomainUserInfo.DataBean, BaseViewHolder>(R.layout.item_accounts) {

    override fun convert(holder: BaseViewHolder, item: DomainUserInfo.DataBean) {
        holder.itemView.setOnClickListener {
            if (context is AppCompatActivity) {
                if (info != null && info.userId == item.userId) {
                    (context as AppCompatActivity).finish()
                    AnimUtil.activityExitAnimDown(context as AppCompatActivity)
                } else {
                    val intent = Intent()
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, item)
                    (context as AppCompatActivity).setResult(Activity.RESULT_OK, intent)
                    (context as AppCompatActivity).finish()
                    AnimUtil.activityExitAnimDown(context as AppCompatActivity)
                }
            }
        }

        if (info != null && info.userId == item.userId) {
            holder.setGone(R.id.current_account, false)
        } else {
            holder.setGone(R.id.current_account, true)
        }
        ImageUtil.loadRoundedImage(holder.getView(R.id.account_avatar), ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(item.userAvatar))
        holder.setText(R.id.account_name, item.userName)
    }

}