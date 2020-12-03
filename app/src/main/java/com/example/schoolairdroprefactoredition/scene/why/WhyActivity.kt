package com.example.schoolairdroprefactoredition.scene.why

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schoolairdroprefactoredition.R

class WhyActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, WhyActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_why)
    }
}