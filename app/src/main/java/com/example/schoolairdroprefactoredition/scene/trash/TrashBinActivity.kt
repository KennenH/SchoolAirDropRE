package com.example.schoolairdroprefactoredition.scene.trash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.TransitionBaseActivity
import com.example.schoolairdroprefactoredition.utils.test.TestShareElementFragmentA
import com.example.schoolairdroprefactoredition.utils.test.TestShareElementFragmentB
import kotlinx.android.synthetic.main.test_fragment_a.*

class TrashBinActivity : TransitionBaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TrashBinActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentA = TestShareElementFragmentA()
        val fragmentB = TestShareElementFragmentB()
        fragmentA.setOnIconClickListener(object : TestShareElementFragmentA.OnIconClickListener {
            override fun onIconClick() {
                supportFragmentManager
                        .beginTransaction()
                        .addSharedElement(fragmentA.icon, "alipay")
                        .addToBackStack(null)
                        .replace(R.id.container, fragmentB)
                        .commit()
            }
        })

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragmentA)
                .commit()
    }
}