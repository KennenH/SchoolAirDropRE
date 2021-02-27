package com.example.schoolairdroprefactoredition.scene.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import kotlinx.android.synthetic.main.activity_user_info_modify.*

class UserModifyInfoActivity : ImmersionStatusBarActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, UserModifyInfoActivity::class.java)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, UserActivity.REQUEST_UPDATE_MY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_modify)
        setSupportActionBar(findViewById(R.id.toolbar))

        user_avatar.setOnClickListener(this)
        user_name.setOnClickListener(this)
        setUserInfo((application as SAApplication).getCachedMyInfo())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == UserActivity.REQUEST_UPDATE_MY) {
                if (data != null) {
                    val newInfo = data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean?
                    intent.putExtra(ConstantUtil.KEY_USER_INFO, newInfo)
                    setUserInfo(newInfo)
                    setResult(RESULT_OK, data)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 使用info填充ui，在用户第一次打开页面时和用户修改信息后回到页面时调用
     */
    private fun setUserInfo(info: DomainUserInfo.DataBean?) {
        info?.let {
            user_avatar.setIconImage(ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(info.userAvatar))
            user_name.setDescription(info.userName)
            user_gender.setDescription(info.userGender)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.user_avatar -> {
                UserUpdateAvatarActivity.start(this)
            }
            R.id.user_name -> {
                UserUpdateNameActivity.start(this)
            }
            R.id.user_gender -> {

            }
            else -> {
            }
        }
    }
}