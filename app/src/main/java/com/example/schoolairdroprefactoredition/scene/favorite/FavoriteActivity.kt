package com.example.schoolairdroprefactoredition.scene.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.database.pojo.Favorite
import com.example.schoolairdroprefactoredition.scene.base.ImmersionStatusBarActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.ui.adapter.FavoriteRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.StatePlaceHolder
import com.example.schoolairdroprefactoredition.utils.DialogUtil
import com.example.schoolairdroprefactoredition.utils.decoration.MarginItemDecoration
import com.example.schoolairdroprefactoredition.viewmodel.FavoriteViewModel
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import kotlinx.android.synthetic.main.activity_likes.*

class FavoriteActivity : ImmersionStatusBarActivity(), FavoriteRecyclerAdapter.OnFavoriteItemActionListener, View.OnClickListener {

    companion object {
        /**
         * 收藏页面在未登录时仍旧可以进入，但是执行取消收藏和收藏操作必须在登录状态才可进行
         */
        fun start(context: Context?) {
            Intent(context, FavoriteActivity::class.java).let {
                if (context is AppCompatActivity) {
                    context.startActivityForResult(it, LoginActivity.LOGIN)
                } else {
                    context?.startActivity(it)
                }
            }
        }
    }

    private val favoriteViewModel by lazy {
        FavoriteViewModel.FavoriteViewModelFactory((application as SAApplication).databaseRepository).create(FavoriteViewModel::class.java)
    }

    private val goodsViewModel by lazy {
        GoodsViewModel.GoodsViewModelFactory((application as SAApplication).databaseRepository).create(GoodsViewModel::class.java)
    }

    private val favoriteRecyclerAdapter by lazy {
        FavoriteRecyclerAdapter().also {
            it.setOnFavoriteItemActionListener(this@FavoriteActivity)
        }
    }

    private val dismissRunnable by lazy {
        Runnable {
            favoriteViewModel.getFavorites(favorite_search_bar.text.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        setSupportActionBar(toolbar)
        initRecycler()
    }

    private fun initRecycler() {
        showPlaceholder(StatePlaceHolder.TYPE_LOADING)
        favorite_search_bar.setOnClickListener(this)
        toolbar.setOnClickListener(this)
        likes_title.setOnClickListener(this)
        favorite_recycler.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(SizeUtils.dp2px(8f), false))
            adapter = favoriteRecyclerAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        KeyboardUtils.hideSoftInput(this@FavoriteActivity)
                        favorite_search_bar.clearFocus()
                    }
                }
            })
        }

        favorite_search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                resetPostDelay(favorite_search_bar)
            }
        })

        /**
         * 获取本地收藏的物品
         */
        favoriteViewModel.getFavorites().observe(this) {
            if (it.isNotEmpty()) {
                showContentContainer()
                favoriteRecyclerAdapter.setList(it)
            } else {
                showPlaceholder(StatePlaceHolder.TYPE_EMPTY)
            }
        }
    }

    /**
     * 重置view的post delay时间
     */
    private fun resetPostDelay(view: View?) {
        view?.removeCallbacks(dismissRunnable)
        view?.postDelayed(dismissRunnable, 1000L)
    }

    /**
     * 显示占位符
     */
    private fun showPlaceholder(type: Int) {
        favorite_placeholder.setPlaceholderType(type)
        favorite_placeholder.setPlaceholderActionTip(getString(R.string.canFavorItemInGoodsPage))
        favorite_placeholder?.visibility = View.VISIBLE
        favorite_recycler?.visibility = View.INVISIBLE
    }

    private fun showContentContainer() {
        favorite_placeholder?.visibility = View.INVISIBLE
        favorite_recycler?.visibility = View.VISIBLE
    }

    /**
     * 登录页面和主页面的中间页，本页面中点击item的联系卖家将引导用户登录
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LoginActivity.LOGIN) {
                // 无需处理，直接将登录信息转交给主页面
                setResult(Activity.RESULT_OK, data)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onToggleFavorite(button: View, item: Favorite) {
        val token = (application as SAApplication).getCachedToken()
        if (token != null) {
            showLoading()
            goodsViewModel.toggleGoodsFavorite(this, token.access_token, item).observeOnce(this) {
                dismissLoading {
                    button.isEnabled = true
                    when (it) {
                        true -> {
                            favoriteRecyclerAdapter.updateFavor(item.goods_id, true)
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAVOR, R.string.favorDone)
                        }
                        false -> {
                            favoriteRecyclerAdapter.updateFavor(item.goods_id, false)
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAVOR, R.string.unfavorDone)
                        }
                        else -> {
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.actionTooFrequent)
                        }
                    }
                }
            }
        } else {
            LoginActivity.start(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.favorite_search_bar -> {
                favorite_search_bar.requestFocus()
            }
            R.id.toolbar -> {
                KeyboardUtils.hideSoftInput(this)
                favorite_search_bar.clearFocus()
            }
            R.id.likes_title -> {
                KeyboardUtils.hideSoftInput(this)
                favorite_search_bar.clearFocus()
            }
        }
    }
}