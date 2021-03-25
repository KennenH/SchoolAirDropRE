package com.example.schoolairdroprefactoredition.scene.protocol

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import kotlinx.android.synthetic.main.activity_protocol.*

class ProtocolActivity : ImmersionStatusBarActivity() {

    companion object {

        /**
         * 隐私政策
         */
        const val PROTOCOL_PRIVACY = 191

        /**
         * 用户协议条款
         */
        const val PROTOCOL_USER = 528

        @JvmStatic
        fun start(context: Context?, type: Int) {
            if (context == null) return

            val intent = Intent(context, ProtocolActivity::class.java)
            intent.getIntExtra(ConstantUtil.KEY_PROTOCOL_TYPE, type)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol)
        setSupportActionBar(toolbar)
        when (intent.getIntExtra(ConstantUtil.KEY_PROTOCOL_TYPE, PROTOCOL_USER)) {
            PROTOCOL_PRIVACY -> {
                webview.loadUrl(ConstantUtil.SCHOOL_AIRDROP_PRIVACY)
            }
            PROTOCOL_USER -> {
                webview.loadUrl(ConstantUtil.SCHOOL_AIRDROP_USER)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}