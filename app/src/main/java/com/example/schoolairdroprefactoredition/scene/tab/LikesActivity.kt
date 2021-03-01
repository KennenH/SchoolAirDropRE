package com.example.schoolairdroprefactoredition.scene.tab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_likes.*

class LikesActivity : ImmersionStatusBarActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LikesActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val favoriteViewModel by lazy {
        FavoriteViewModel.FavoriteViewModelFactory((application as SAApplication).databaseRepository).create(FavoriteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        setSupportActionBar(toolbar)
        favorite_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        favorite_recycler.addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(8f), false))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}