package com.example.schoolairdroprefactoredition.scene.transactionstate

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.arrangeplace.ArrangePositionActivity
import com.example.schoolairdroprefactoredition.scene.chat.ChatActivity
import com.example.schoolairdroprefactoredition.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_transaction_state.*

class TransactionStateActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TransactionStateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_state)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        StatusBarUtil.setTranslucentForImageView(this@TransactionStateActivity, toolbar)
        BarUtils.setNavBarLightMode(this@TransactionStateActivity, true)
        BarUtils.setNavBarColor(this@TransactionStateActivity, getColor(R.color.primary))

        location.setOnClickListener(this@TransactionStateActivity)
        incompatible.setOnClickListener(this@TransactionStateActivity)
        QRCodeBtn.setOnClickListener(this@TransactionStateActivity)
        avatar.setOnClickListener(this@TransactionStateActivity)
        userName.setOnClickListener(this@TransactionStateActivity)
        chat.setOnClickListener(this@TransactionStateActivity)
    }

    override fun onClick(v: View?) {
        when (v) {
            location -> {
                ArrangePositionActivity.startForResult(this@TransactionStateActivity)
            }

            incompatible -> {

            }

            QRCodeBtn -> {

            }

            avatar -> {

            }

            userName -> {

            }

            chat -> {
                ChatActivity.start(this@TransactionStateActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ArrangePositionActivity.SELECT_POSITION) {
                location.text = data?.getStringExtra(ArrangePositionActivity.SELECT_POSITION_KEY)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}