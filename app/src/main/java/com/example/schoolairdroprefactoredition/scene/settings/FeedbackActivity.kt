package com.example.schoolairdroprefactoredition.scene.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.MyUtil
import kotlinx.android.synthetic.main.activity_feed_back.*


class FeedbackActivity : ImmersionStatusBarActivity(), AdapterView.OnItemSelectedListener {

    companion object {

        @JvmStatic
        fun start(context: Context?) {
            if (context == null) return

            val intent = Intent(context, FeedbackActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var currentAddress: String? = "1800303220@cjlu.edu.cn"

    private var currentType: String? = "发现bug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        setSupportActionBar(toolbar)

        feedback_send_to.setOnLongClickListener {
            MyUtil.copyTpClipboard(this, currentAddress.toString())
            true
        }
        feedback_send_to.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
                this,
                R.array.feedback_address,
                R.layout.item_spinner).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            feedback_send_to.adapter = adapter
        }

        feedback_type.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
                this,
                R.array.feedback_type,
                R.layout.item_spinner).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            feedback_type.adapter = adapter
        }

        feedback_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                feedback_done.isEnabled = feedback_content.text.length > 9
            }
        })

        feedback_done.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).let {
                it.type = "message/rfc822"
                it.data = Uri.parse("mailto:$currentAddress")
                it.putExtra(Intent.EXTRA_SUBJECT, currentType)
                it.putExtra(Intent.EXTRA_TEXT, feedback_content.text)
                try {
                    startActivity(Intent.createChooser(it, getString(R.string.ChooseEmailApp)))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(this@FeedbackActivity, getString(R.string.NoEmailApp), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            feedback_send_to -> {
                currentAddress = parent?.getItemAtPosition(position) as String?
            }
            feedback_type -> {
                currentType = parent?.getItemAtPosition(position) as String?
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}