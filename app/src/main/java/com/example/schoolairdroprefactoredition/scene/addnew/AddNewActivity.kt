package com.example.schoolairdroprefactoredition.scene.addnew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.KeyboardUtils
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.application.Application
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing
import com.example.schoolairdroprefactoredition.domain.DomainToken
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips
import com.example.schoolairdroprefactoredition.scene.addnew.InputSetActivity.Companion.start
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity.Companion.start
import com.example.schoolairdroprefactoredition.ui.adapter.HorizontalImageRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.HorizontalImageRecyclerAdapter.OnPicSetClickListener
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem.OnItemAddPicActionListener
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.utils.MyUtil.ImageLoader
import com.example.schoolairdroprefactoredition.utils.MyUtil.getArrayFromString
import com.example.schoolairdroprefactoredition.utils.MyUtil.pickPhotoFromAlbum
import com.example.schoolairdroprefactoredition.utils.filters.DecimalFilter
import com.example.schoolairdroprefactoredition.viewmodel.AddNewViewModel
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import kotlinx.android.synthetic.main.activity_selling_add_new.*
import java.util.*

class AddNewActivity : PermissionBaseActivity(), View.OnClickListener, AMapLocationListener, OnPicSetClickListener, Application.OnApplicationLoginListener {

    companion object {
        /**
         * 选择封面 请求码
         */
        const val REQUEST_CODE_COVER = 219

        /**
         * 选择图片集 请求码
         */
        const val REQUEST_CODE_PIC_SET = 11

        /**
         * 发布物品或新帖子
         * 添加物品或帖子使用该方法
         * 修改物品使用[AddNewActivity.start]
         *
         * @param type 页面类型
         * one of
         * [AddNewType.ADD_ITEM] 上架物品
         * [AddNewType.ADD_POST] 新增帖子
         */
        fun start(context: Context?, @AddNewType type: Int) {
            if (context == null) return

            val bundle = Bundle()
            bundle.putSerializable(ConstantUtil.KEY_ADD_NEW_TYPE, type)
            start(context, bundle)
        }

        /**
         * 修改物品信息
         * 添加物品或帖子使用[AddNewActivity.start]
         *
         * @param goodsInfo 要修改的物品的信息 类型[DomainPurchasing.DataBean]
         */
        fun start(context: Context?, goodsInfo: DomainPurchasing.DataBean?) {
            if (context == null) return

            val bundle = Bundle()
            bundle.putSerializable(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.MODIFY_ITEM)
            bundle.putSerializable(ConstantUtil.KEY_GOODS_INFO, goodsInfo)
            start(context, bundle)
        }

        /**
         * 打开页面
         * 若不知道这个bundle里需要传什么参数，请按情况使用以下打开方式
         *
         *
         * 新增物品或帖子: [AddNewActivity.start]
         * 修改物品信息: [AddNewActivity.start]
         */
        private fun start(context: Context?, bundle: Bundle) {
            if (context == null) return

            val intent = Intent(context, AddNewActivity::class.java)
            intent.putExtras(bundle)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    /**
     * 页面的类型
     */
    annotation class AddNewType {
        companion object {
            /**
             * 上传新物品 页面类型
             */
            const val ADD_ITEM = 678

            /**
             * 修改物品 页面类型
             */
            const val MODIFY_ITEM = 456

            /**
             * 上传新帖子 页面类型
             */
            const val ADD_POST = 234
        }
    }

    private val addNewViewModel by lazy {
        ViewModelProvider(this).get(AddNewViewModel::class.java)
    }

    private val goodsViewModel by lazy {
        ViewModelProvider(this).get(GoodsViewModel::class.java)
    }

    private var mClient: AMapLocationClient? = null

    private var mOption: AMapLocationClientOption? = null

    private var mAmapLocation: AMapLocation? = null

    /**
     * 图片集横向recycler的adapter
     */
    private val mPicSetHorizontalAdapter by lazy {
        HorizontalImageRecyclerAdapter()
    }

    /**
     * 当未授权时保存请求码，授权完毕后按照保存的请求码进行操作
     */
    private var request = 0

    /**
     * 图片宽高比 高：宽
     */
    private var mHWRatio = 1.0f

    /**
     * 当前页面保存的封面路径
     */
    private var mCoverPath: String = ""

    /**
     * 当前页面保存的图片集路径
     */
    private var mPicSetSelected: MutableList<LocalMedia> = ArrayList()
    private var goodsBaseInfo: DomainPurchasing.DataBean? = null

    /**
     * 草稿内容是否已被恢复
     *
     * 若手动清除则置为false，手动恢复置为true
     */
    private var isDraftRestored = true

    /**
     * 是否已经提交并且成功了
     *
     *
     * 成功之后将不会对页面内容进行缓存
     */
    private var isSubmitSuccess = false

    /**
     * 页面中是否有草稿
     */
    private var hasDraft = false

    /**
     * 页面新增的类型
     */
    @AddNewType
    private var addNewType = AddNewType.ADD_ITEM

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selling_add_new)
        setSupportActionBar(findViewById(R.id.toolbar))

        // 当app在外部登录时将会收到通知
        (application as Application).addOnApplicationLoginListener(this)

        goodsBaseInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO) as? DomainPurchasing.DataBean
        addNewType = intent.getIntExtra(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.ADD_ITEM)

        saved_draft.visibility = View.GONE
        server_tip.visibility = View.GONE
        illegal_warning.visibility = View.GONE
        draft_tip_toggle.setOnClickListener(this)
        draft_action.setOnClickListener(this)
        saved_close.setOnClickListener(this)
        pic_set.setOnClickListener(this)
        option_title.setOnClickListener(this)
        price_confirm.setOnClickListener(this)
        option_location.setOnClickListener(this)
        option_negotiable.setOnClickListener(this)
        option_secondHand.setOnClickListener(this)
        option_description.setOnClickListener(this)
        price_input.setOnEditorActionListener { _: TextView?, _: Int, _: KeyEvent? ->
            price_input.clearFocus()
            KeyboardUtils.hideSoftInput(this)
            true
        }
        price_input.filters = arrayOf<InputFilter>(DecimalFilter())
        pic_set.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        //封面
        cover.setOnItemAddPicActionListener(object : OnItemAddPicActionListener {
            override fun onClose() {
                mCoverPath = ""
                cover.clearImage(true)
            }

            override fun onItemClick() {
                if (cover.imagePath != null && "" != cover.imagePath) {
                    XPopup.Builder(this@AddNewActivity)
                            .isDarkTheme(true)
                            .asImageViewer(cover.findViewById(R.id.image), mCoverPath, false, -1, -1, -1, true, ImageLoader())
                            .show()
                } else {
                    request = REQUEST_CODE_COVER
                    requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL)
                }
            }
        })
        // 这是footer 不显示图片，仅作为相册选择图片的按钮
        val add = AddPicItem(this)
        add.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        add.setOnItemAddPicActionListener(object : OnItemAddPicActionListener {
            override fun onClose() {
                // do nothing
            }

            override fun onItemClick() {
                request = REQUEST_CODE_PIC_SET
                requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL)
            }
        })
        mPicSetHorizontalAdapter.setOnPicSetClickListener(this)
        mPicSetHorizontalAdapter.addFooterView(add)
        pic_set.adapter = mPicSetHorizontalAdapter
        requestPermission(PermissionConstants.LOCATION, RequestType.AUTO)
        initPageAccordingToType()
    }

    /**
     * 根据页面类型初始化页面数据
     */
    private fun initPageAccordingToType() {
        when (addNewType) {
            AddNewType.ADD_ITEM -> {
                draft_tip_toggle.setText(R.string.addNewSelling)
                tag_title.visibility = View.GONE
                option_tag_wrapper.visibility = View.GONE
                option_anonymous.visibility = View.GONE
                restoreItemDraft()
            }
            AddNewType.ADD_POST -> {
                price_title.visibility = View.GONE
                option_price.visibility = View.GONE
                option_negotiable.visibility = View.GONE
                option_secondHand.visibility = View.GONE
                draft_tip_toggle.setText(R.string.addNewPost)
                detail_title.setText(R.string.postTitleSaySth)
                restorePostDraft()
            }
            AddNewType.MODIFY_ITEM -> {
                draft_tip_toggle.setText(R.string.modifyInfo)
                tag_title.visibility = View.GONE
                option_tag_wrapper.visibility = View.GONE
                option_anonymous.visibility = View.GONE
                initGoodsInfo()
            }
        }
    }

    override fun locationGranted() {
        super.locationGranted()
        startLocation()
    }

    override fun locationDenied() {
        super.locationDenied()
        option_location.description = getString(R.string.errorRetry)
    }

    override fun albumGranted() {
        super.albumGranted()
        if (request == REQUEST_CODE_COVER) {
            if (addNewType == AddNewType.ADD_ITEM) {
                pickPhotoFromAlbum(
                        this,
                        REQUEST_CODE_COVER,
                        1,
                        isSquare = true,
                        isCircle = false,
                        isCropWithoutSpecificShape = false
                )
            } else if (addNewType == AddNewType.ADD_POST) {
                pickPhotoFromAlbum(
                        this,
                        REQUEST_CODE_COVER,
                        1,
                        isSquare = false,
                        isCircle = false,
                        isCropWithoutSpecificShape = true
                )
            }
        } else if (request == REQUEST_CODE_PIC_SET) {
            pickPhotoFromAlbum(
                    this,
                    REQUEST_CODE_PIC_SET,
                    8,
                    isSquare = false,
                    isCircle = false,
                    isCropWithoutSpecificShape = false
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == InputSetActivity.REQUEST_CODE) { // 输入页面内容返回
                if (data != null) {
                    val type = data.getIntExtra(InputSetActivity.TYPE, InputSetActivity.TYPE_TITLE)
                    if (type == InputSetActivity.TYPE_TITLE) {
                        option_title.text = data.getStringExtra(InputSetActivity.RESULT)
                    } else {
                        option_description.text = data.getStringExtra(InputSetActivity.RESULT)
                    }
                }
            } else if (requestCode == REQUEST_CODE_COVER) { // 封面选择返回
                if (data != null) {
                    val coverMedia = PictureSelector.obtainMultipleResult(data)[0]
                    mCoverPath = coverMedia.path ?: coverMedia.cutPath
                    mHWRatio = coverMedia.height.toFloat() / coverMedia.width.toFloat()
                    cover.setImageLocalPath(mCoverPath)
                }
            } else if (requestCode == REQUEST_CODE_PIC_SET) { // 图片集选择返回
                if (data != null) {
                    mPicSetSelected.addAll(PictureSelector.obtainMultipleResult(data))
                    mPicSetHorizontalAdapter.setList(mPicSetSelected)
                }
            } else if (requestCode == LoginActivity.LOGIN) { // 在本页面打开登录页面登录并返回
                if (data != null) {
                    setResult(RESULT_OK, data)
                    (application as Application).cacheMyInfoAndToken(
                            data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean,
                            data.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken)
                    try {
                        submit()
                    } catch (ignored: Exception) {
                    }
                }
            }
        }
    }

    /**
     * 定位
     */
    private fun startLocation() {
        if (mClient == null) {
            mClient = AMapLocationClient(this)
            mClient?.setLocationListener(this)
        }
        if (mOption == null) {
            mOption = AMapLocationClientOption()
        }
        option_location.description = getString(R.string.locating)
        mOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mOption?.isOnceLocation = true
        mOption?.isLocationCacheEnable = true
        mClient?.setLocationOption(mOption)
        mClient?.startLocation()
    }

    /**
     * 提交表单
     */
    private fun submit() {
        if (checkFormIsLegal()) {
            if (addNewType == AddNewType.ADD_POST) {
                submitPost()
            } else {
                submitItem()
            }
        }
    }

    /**
     * 提交物品表单
     */
    private fun submitItem() {
        if (addNewType == AddNewType.ADD_ITEM) { // 新增物品
            val token = (application as Application).getCachedToken()
            if (token != null) {
                if (mAmapLocation == null) {
//                            dismissLoading(() ->
                    AddNewResultActivity.start(this, false, AddNewResultTips.LOCATION_FAILED_NEW_ITEM)
                } else {
                    showLoading {
                        val mPicSetPaths = ArrayList<String>()
                        for (localMedia in mPicSetSelected) {
                            mPicSetPaths.add(localMedia.path)
                        }

                        isSubmitSuccess = false
                        addNewViewModel.submitItem(token.access_token, mCoverPath, mPicSetPaths,
                                option_title.text.toString(), option_description.text.toString(),
                                mAmapLocation!!.longitude, mAmapLocation!!.latitude,
                                !option_secondHand.isChecked, option_negotiable.isChecked, price_input.text.toString().toFloat())
                                .observeOnce(this) { result: Boolean ->
                                    if (!isSubmitSuccess) {
                                        dismissLoading {
                                            AddNewResultActivity.start(this, result, if (result) AddNewResultTips.SUCCESS_NEW_ITEM else AddNewResultTips.FAILED_ADD)
                                            if (result) {
                                                isSubmitSuccess = true // 发送已完毕标志
                                                finish()
                                                AnimUtil.activityExitAnimDown(this)
                                            }
                                        }
                                    }
                                }
                    }
                }
            } else {
                start(this)
            }
        } else if (addNewType == AddNewType.MODIFY_ITEM) { // 修改物品
            showLoading {

                // TODO: 2021/2/19 修改物品 还没写完 或许不提供修改接口了
                val mPicSetPaths: MutableList<String> = ArrayList()
                for (localMedia in mPicSetSelected) {
                    mPicSetPaths.add(localMedia.path)
                }
            }
        }
    }

    /**
     * 提交新帖表单
     */
    private fun submitPost() {
        val token = (application as Application).getCachedToken()
        if (token != null) {
            if (mAmapLocation == null) {
                AddNewResultActivity.start(this, false, AddNewResultTips.LOCATION_FAILED_NEW_ITEM)
            } else {
                showLoading {
                    val mPicSetPaths: MutableList<String> = ArrayList()
                    for (localMedia in mPicSetSelected) {
                        mPicSetPaths.add(localMedia.path)
                    }
                    addNewViewModel.submitPost(token.access_token, mCoverPath, mHWRatio, mPicSetPaths,
                            option_title.text.toString(), option_description.text.toString(),
                            mAmapLocation!!.longitude, mAmapLocation!!.latitude)
                            .observeOnce(this, { result: Boolean ->
                                // 这里有一个bug 当提交响应失败后 再在同一个页面重试之后成功 将会多次弹出成功提示页面 但实际只提交成功了一次
                                // 所以这里加一个标志变量 打开一次页面最多只能成功一次 因此成功后即不再弹出
                                if (!isSubmitSuccess) {
                                    dismissLoading {
                                        AddNewResultActivity.start(this, result, if (result) AddNewResultTips.SUCCESS_NEW_POST else AddNewResultTips.FAILED_ADD)
                                        if (result) {
                                            isSubmitSuccess = true // 发送已完毕标志
                                            finish()
                                            AnimUtil.activityExitAnimDown(this)
                                        }
                                    }
                                }
                            })
                }
            }
        } else {
            start(this)
        }
    }

    /**
     * 检查表单填写是否完整
     * 若有必填项未填，则将页面跳至未填项目并高亮显示提示用户填写
     */
    private fun checkFormIsLegal(): Boolean {
        var pass = true
        var focusView: View? = null

        if (option_description.text.isEmpty()) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_description_wrapper)
            focusView = option_description_wrapper
            pass = false
        }
        if (addNewType != AddNewType.ADD_POST && price_input.text.toString().trim() == "") {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_price)
            focusView = price_title
            pass = false
        }
        if (option_title.text.isEmpty()) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_title_wrapper)
            focusView = title_title
            pass = false
        }
        if (mPicSetHorizontalAdapter.data.size < 1) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, pic_set)
            focusView = pic_set_title
            pass = false
        }
        if (mCoverPath.trim() == "") {
            AnimUtil.primaryBackgroundViewBlinkRed(this, cover_wrapper)
            focusView = cover_title
            pass = false
        }
        if (!pass) {
            focusView?.requestFocus()
            focusView?.clearFocus()
        }
        return pass
    }

    /**
     * 保存用户本次输入
     * 若未提交成功且输入不为空则保存草稿
     * 若为修改物品则无需保存草稿
     */
    override fun onPause() {
        super.onPause()
        saveDraft()
    }

    /**
     * 保存页面草稿
     *
     * 当用户将之前的草稿清除之后，保存草稿操作将会覆盖之前的草稿，因此应该将撤销清除的横幅去掉以表面草稿已不可恢复
     */
    private fun saveDraft() {
        // 若草稿被清除，走到这一步时表明草稿将不可被恢复了，因为会被接下来的保存操作所覆盖
        if (hasDraft && !isDraftRestored) { // 存在草稿且没有被恢复，说明是被用户手动清除了
            // 恢复草稿的横幅还在，那就把它关闭
            if (saved_draft.visibility == View.VISIBLE) {
                saved_draft.visibility = View.GONE
            }
            // 把标题颜色设置为当前没有草稿
            draft_tip_toggle.setTextColor(resources.getColor(R.color.primaryText, theme))
            hasDraft = false
        }

        // 保存草稿
        if (addNewType == AddNewType.ADD_ITEM) { // 保存物品表单
            if (!isSubmitSuccess && (mCoverPath.trim() != ""
                            || mPicSetSelected.size > 0 || option_title.text.toString().trim() != ""
                            || option_description.text.toString().trim() != ""
                            || price_input.text.toString() != ""
                            || option_negotiable.isChecked
                            || option_secondHand.isChecked)) {
                addNewViewModel.saveItemDraft(mCoverPath,
                        mPicSetSelected,
                        option_title.text.toString(),
                        option_description.text.toString(),
                        price_input.text.toString(),
                        option_negotiable.isChecked,
                        option_secondHand.isChecked)
            } else {
                addNewViewModel.deleteItemDraft()
            }
        } else if (addNewType == AddNewType.ADD_POST) { // 保存帖子表单
            if (!isSubmitSuccess && (mCoverPath.trim() != ""
                            || mPicSetSelected.size > 0 || option_title.text.toString().trim() != ""
                            || option_description.text.toString().trim() != "")) {
                addNewViewModel.savePostDraft(mCoverPath,
                        mHWRatio,
                        mPicSetSelected,
                        option_tag.text.toString(),
                        option_anonymous.isChecked,
                        option_title.text.toString(),
                        option_description.text.toString())
            } else {
                addNewViewModel.deletePostDraft()
            }
        }
    }

    /**
     * 恢复物品草稿
     * 在用户清除草稿之后再次恢复
     */
    private fun restoreItemDraft() {
        addNewViewModel.restoreItemDraft().observeOnce(this, { draftCache: NewItemDraftCache? ->
            if (draftCache != null) {
                // 将页面当前有草稿置为true
                hasDraft = true
                // 显示草稿已经恢复的提示
                saved_draft.visibility = View.VISIBLE

                mCoverPath = draftCache.cover
                mPicSetSelected = draftCache.picSet
                if (mCoverPath != "") {
                    cover.setImageLocalPath(mCoverPath)
                }
                mPicSetHorizontalAdapter.setList(mPicSetSelected)
                option_title.text = draftCache.title
                option_description.text = draftCache.description
                price_input.setText(draftCache.price)
                if (draftCache.isNegotiable) {
                    if (!option_negotiable.isChecked) {
                        option_negotiable.toggle()
                    }
                }
                if (draftCache.isSecondHand) {
                    if (!option_secondHand.isChecked) {
                        option_secondHand.toggle()
                    }
                }
            }
            draft_tip.text = getString(R.string.draftRecovered)
            draft_action.text = getString(R.string.clearInput)
            isDraftRestored = true
        })
    }

    /**
     * 恢复帖子草稿
     * 清除后在关闭之前仍可恢复
     */
    private fun restorePostDraft() {
        addNewViewModel.restorePostDraft().observeOnce(this, { draftCache: NewPostDraftCache? ->
            if (draftCache != null) {
                hasDraft = true
                saved_draft.visibility = View.VISIBLE // 显示草稿恢复提示
                mCoverPath = draftCache.cover
                mHWRatio = draftCache.hwRatio
                mPicSetSelected = draftCache.picSet
                if (mCoverPath != "") {
                    cover.setImageLocalPath(mCoverPath)
                }
                mPicSetHorizontalAdapter.setList(mPicSetSelected)
                option_title.text = draftCache.title
                option_description.text = draftCache.content
            }
            draft_tip.text = getString(R.string.draftRecovered)
            draft_action.text = getString(R.string.clearInput)
            isDraftRestored = true
        })
    }

    /**
     * 用给进来的物品id获取物品信息
     * 在使用物品信息填充页面
     */
    private fun initGoodsInfo() {
        goodsBaseInfo?.let { baseInfo ->
            goodsViewModel.getGoodsDetailByID(baseInfo.goods_id).observeOnce(this) { goodsDetailInfo ->
                goodsDetailInfo.let { detailInfo ->
                    if (detailInfo != null) {
                        val goodsInfo = intent.getSerializableExtra(ConstantUtil.KEY_GOODS_INFO) as DomainPurchasing.DataBean
                        mCoverPath = ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(goodsInfo.goods_cover_image)
                        cover.setImageRemotePath(mCoverPath)

                        val picSet =
                                if (detailInfo.data.goods_images == null || detailInfo.data.goods_images.trim() == "") ArrayList()
                                else getArrayFromString(detailInfo.data.goods_images)

                        for (i in picSet.indices) {
                            val media = LocalMedia()
                            media.path = ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(picSet[i])
                            mPicSetSelected.add(media)
                        }

                        mPicSetHorizontalAdapter.setList(mPicSetSelected)
                        option_title.text = goodsInfo.goods_name
                        price_input.setText(goodsInfo.goods_price)
                        if (goodsInfo.isGoods_is_secondHande) {
                            option_secondHand.toggle()
                        }
                        if (goodsInfo.isGoods_is_bargain) {
                            option_negotiable.toggle()
                        }
                        option_description.text = detailInfo.data.goods_content
                    } else {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorLoadItemInfo)
                    }
                }
            }
        }
    }

    /**
     * 清除输入
     */
    private fun clearDraft() {
        mCoverPath = ""
        mPicSetSelected.clear()
        mPicSetHorizontalAdapter.setList(mPicSetSelected)
        cover.clearImage(true)
        option_title.text = ""
        price_input.setText("")
        if (option_negotiable.isChecked) {
            option_negotiable.toggle()
        }
        if (option_secondHand.isChecked) {
            option_secondHand.toggle()
        }
        option_description.text = ""
        draft_tip.text = getString(R.string.draftCleared)
        draft_action.text = getString(R.string.restoreDraft)
        isDraftRestored = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.selling_add_done, menu)
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            AnimUtil.activityExitAnimDown(this)
            return true
        } else if (id == R.id.add_submit) {
            item.isEnabled = false
            try {
                submit()
            } catch (ignored: Exception) {
            }
            item.isEnabled = true
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AnimUtil.activityExitAnimDown(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.option_title) {
            start(this, InputSetActivity.TYPE_TITLE, option_title.text.toString(), getString(R.string.title))
        } else if (id == R.id.price_confirm) {
            KeyboardUtils.hideSoftInput(v)
            price_input.clearFocus()
        } else if (id == R.id.option_location) {
            option_location.description = getString(R.string.locating)
            requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL)
        } else if (id == R.id.option_negotiable) {
            option_negotiable.toggle()
        } else if (id == R.id.option_secondHand) {
            option_secondHand.toggle()
        } else if (id == R.id.option_description) {
            start(this, InputSetActivity.TYPE_DESCRIPTION, option_description.text.toString(), getString(R.string.goods_description))
        } else if (id == R.id.saved_close) {
            // 点击横幅关闭按钮，将页面标题颜色置为主题色以提示用户页面中存在可恢复的草稿
            AnimUtil.collapse(saved_draft)
            AnimUtil.textColorAnim(this, draft_tip_toggle, R.color.primaryText, R.color.colorAccent)
        } else if (id == R.id.draft_action) {
            if (addNewType != AddNewType.MODIFY_ITEM) {
                if (isDraftRestored) {
                    clearDraft()
                } else {
                    if (addNewType == AddNewType.ADD_ITEM) {
                        restoreItemDraft()
                    } else {
                        restorePostDraft()
                    }
                }
            }
        } else if (id == R.id.draft_tip_toggle) {
            // 页面中有草稿且横幅已被关闭时点击页面标题将可以展示横幅
            if (hasDraft && saved_draft.visibility != View.VISIBLE) {
                AnimUtil.expand(saved_draft)
                AnimUtil.textColorAnim(this, draft_tip_toggle, R.color.colorAccent, R.color.primaryText)
            }
        }
    }

    override fun onLocationChanged(aMapLocation: AMapLocation) {
        mAmapLocation = aMapLocation
        if (aMapLocation.errorCode == 0) {
            val location = StringBuilder()
            option_location.description = location
                    .append(aMapLocation.province)
                    .append(aMapLocation.city)
                    .append(aMapLocation.district)
                    .append(aMapLocation.street)
                    .append(aMapLocation.streetNum)
                    .toString()
        } else {
            option_location.description = getString(R.string.errorLocation)
        }
    }

    /**
     * 页面被销毁，将定位对象都置为null
     */
    public override fun onDestroy() {
        super.onDestroy()
        if (mClient != null) {
            mClient?.stopLocation()
            mClient?.unRegisterLocationListener(this)
            mClient?.onDestroy()
            mClient = null
            mOption = null
        }
    }

    /**
     * 在已选的图片集中删除一张图片
     *
     * @param pos 删除的位置
     */
    override fun onPicSetDeleteAt(pos: Int) {
        mPicSetSelected.removeAt(pos)
    }

    /**
     * 点击已选的图片集
     * 查看图片
     *
     * @param pos 点击的位置
     */
    override fun onPicSetClick(source: ImageView, pos: Int) {
        val data: MutableList<Any> = ArrayList()
        for (pic in mPicSetHorizontalAdapter.data) {
            data.add(pic.path)
        }
        XPopup.Builder(this)
                .isDarkTheme(true)
                .asImageViewer(
                        source,
                        pos,
                        data,
                        false,
                        false,
                        -1,
                        -1,
                        -1,
                        true,
                        { popupView: ImageViewerPopupView, position1: Int ->
                            popupView.updateSrcView(pic_set.getChildAt(position1).findViewById(R.id.image))
                        },
                        ImageLoader()
                )
                .show()
    }

    override fun onApplicationLoginStateChange(isLogged: Boolean) {
        // todo app 登录回调
    }
}