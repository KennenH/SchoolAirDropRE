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
import androidx.lifecycle.Observer
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
import com.example.schoolairdroprefactoredition.application.SAApplication
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewIWantDraftCache
import com.example.schoolairdroprefactoredition.databinding.SheetIwantColorSelectorBinding
import com.example.schoolairdroprefactoredition.domain.*
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity
import com.example.schoolairdroprefactoredition.ui.adapter.AddNewHorizontalImageRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.adapter.AddNewHorizontalImageRecyclerAdapter.OnPicSetClickListener
import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem.OnItemAddPicActionListener
import com.example.schoolairdroprefactoredition.utils.*
import com.example.schoolairdroprefactoredition.utils.MyUtil.ImageLoader
import com.example.schoolairdroprefactoredition.utils.MyUtil.pickPhotoFromAlbum
import com.example.schoolairdroprefactoredition.utils.filters.DecimalFilter
import com.example.schoolairdroprefactoredition.viewmodel.AddNewViewModel
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import kotlinx.android.synthetic.main.activity_iwant_refactor.*
import kotlinx.android.synthetic.main.activity_selling_add_new.*
import kotlinx.android.synthetic.main.sheet_iwant_color_selector.*
import java.util.*
import kotlin.collections.ArrayList

class AddNewActivity : PermissionBaseActivity(), View.OnClickListener, AMapLocationListener,
        OnPicSetClickListener, SAApplication.OnApplicationLoginListener {

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
         * 发布物品或求购，仅新增物品或新增求购
         *
         * @param type 页面类型 [AddNewType]
         */
        @JvmStatic
        fun startAddNew(context: Context?, @AddNewType type: Int) {
            if (context == null) return

            if (type != AddNewType.ADD_ITEM && type != AddNewType.ADD_IWANT) {
                throw IllegalArgumentException("startAddNew can only accept add new type, use startModifyGoods or startModifyIWant instead")
            }

            val intent = Intent(context, AddNewActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_ADD_NEW_TYPE, type)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }

        /**
         * 修改物品信息
         *
         * @param goodsID 要修改的物品的id
         */
        @JvmStatic
        fun startModifyGoods(context: Context?, goodsID: Int?) {
            if (context == null || goodsID == null) return

            val intent = Intent(context, AddNewActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.MODIFY_ITEM)
            intent.putExtra(ConstantUtil.KEY_GOODS_ID, goodsID)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }

        /**
         * 修改求购信息
         *
         * @param iWantID 要修改的求购信息
         */
        @JvmStatic
        fun startModifyIWant(context: Context?, iWantID: Int?) {
            if (context == null || iWantID == null) return

            val intent = Intent(context, AddNewActivity::class.java)
            intent.putExtra(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.MODIFY_IWANT)
            intent.putExtra(ConstantUtil.KEY_IWANT_ID, iWantID)
            if (context is AppCompatActivity) {
                context.startActivityForResult(intent, LoginActivity.LOGIN)
                AnimUtil.activityStartAnimUp(context)
            }
        }
    }

    /**
     * 页面的类型
     *
     * 因为把这个页面命名为AddNewActivity，所以把页面类型叫做AddNewType
     * 就是页面的类型，并不是只有新增时使用这个页面，所有新增修改类型的标志码都在这个类中列出
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
             * 发布新求购 页面类型
             */
            const val ADD_IWANT = 234

            /**
             * 修改求购 页面类型
             */
            const val MODIFY_IWANT = 861
        }
    }

    private val addNewViewModel by lazy {
        ViewModelProvider(this).get(AddNewViewModel::class.java)
    }

    private val goodsViewModel by lazy {
        GoodsViewModel.GoodsViewModelFactory((application as SAApplication).databaseRepository).create(GoodsViewModel::class.java)
    }

    /**
     * Amap 定位所需对象
     */
    private var mClient: AMapLocationClient? = null

    /**
     * Amap 定位所需对象
     */
    private var mOption: AMapLocationClientOption? = null

    /**
     * Amap 定位所需对象
     */
    private var mAmapLocation: AMapLocation? = null

    /**
     * 图片集横向recycler的adapter
     */
    private val mPicSetHorizontalAdapter by lazy {
        AddNewHorizontalImageRecyclerAdapter()
    }

    /**
     * 当未授权时保存请求码，授权完毕后按照保存的请求码自动进行操作
     */
    private var request = 0

    /**
     * 类型为[AddNewType.ADD_ITEM]时当前选择的封面本地路径
     */
    private var mCoverPath: String = ""

    /**
     * 所有类型适用，保存的将要添加的图片集本地路径
     */
    private var mPicSetSelected: MutableList<LocalMedia> = ArrayList()

    /**
     * 类型为[AddNewType.MODIFY_IWANT] 或 [AddNewType.MODIFY_ITEM]时被删除的服务器图片路径
     */
    private val mImagesToDelete = ArrayList<String>()

    /**
     * 类型为[AddNewType.ADD_IWANT] 或 [AddNewType.MODIFY_IWANT]时当前选中的tag id
     */
    private var mNowTag: DomainIWantTags.Data? = null

    /**
     * 类型为[AddNewType.ADD_IWANT] 或 [AddNewType.MODIFY_IWANT]时当前选中的卡片颜色
     *
     * one of
     * [com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.COLOR_DEFAULT]
     * [com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.COLOR_PURPLE]
     * [com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.COLOR_THEME]
     * [com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.COLOR_HEART]
     * [com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter.COLOR_WARNING]
     */
    private var mNowCardColor = -1

    /**
     * 草稿内容是否已被恢复
     *
     * 若手动清除则置为false，手动恢复置为true
     */
    private var isDraftRestored = true

    /**
     * 是否已经提交并且成功了
     *
     * 该标志位是为了防止提交成功之后出错仍留在本页面，防止用户重复提交
     * 成功之后将不会对页面内容进行缓存
     */
    private var isSubmitSuccess = false

    /**
     * 页面中是否有草稿
     *
     * 在[AddNewType]为[AddNewType.ADD_IWANT] [AddNewType.ADD_ITEM]时标志是否本地有
     * 对应页面类型的草稿
     */
    private var hasDraft = false

    /**
     * 当前页面的类型 [AddNewType]
     *
     * 从所有start页面方法中传递页面类型
     * [startAddNew] [startModifyIWant] [startModifyGoods]
     */
    @AddNewType
    private val pageType by lazy {
        intent.getIntExtra(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.ADD_ITEM)
    }

    /**
     * 在[AddNewType]为[AddNewType.MODIFY_ITEM]时传入的要修改的物品id
     */
    private val goodsID by lazy {
        intent.getIntExtra(ConstantUtil.KEY_GOODS_ID, -1)
    }

    /**
     * 在[AddNewType]为[AddNewType.MODIFY_ITEM]时获取的旧的物品信息
     */
    private var goodsInfo: DomainGoodsAllDetailInfo.Data? = null

    /**
     * 在[AddNewType]为[AddNewType.MODIFY_IWANT]时传入的要修改的求购id
     */
    private val iWantID by lazy {
        intent.getIntExtra(ConstantUtil.KEY_IWANT_ID, -1)
    }

    /**
     * 在[AddNewType]为[AddNewType.MODIFY_IWANT]时获取的旧求购信息
     */
    private var iWantInfo: DomainIWant.Data? = null

    /**
     * 卡片颜色选择弹窗
     */
    private val cardColorSelectorDialog by lazy {
        BottomSheetDialog(this)
    }

    /**
     * 卡片颜色选择弹窗的布局绑定器
     */
    private val cardColorSelectorBinding by lazy {
        SheetIwantColorSelectorBinding.inflate(LayoutInflater.from(this))
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selling_add_new)
        setSupportActionBar(findViewById(R.id.toolbar))

        // 当app在外部登录时将会收到通知
        (application as? SAApplication)?.addOnApplicationLoginListener(this)

        // 请求定位权限，权限授予后父类调用locationGranted
        requestPermission(PermissionConstants.LOCATION, RequestType.AUTO)

        // 初始化页面
        initPageAccordingToType()
    }

    /**
     * 初始化全局数据以及根据页面类型[AddNewType]初始化页面数据
     *
     * 新增类型将初始化表单条目，只显示对应的选项
     * 修改类型将请求原始表单数据并使用其填充页面选项
     *
     * onClick回调传送门[onClick]
     */
    private fun initPageAccordingToType() {
        saved_draft.visibility = View.GONE
        server_tip.visibility = View.GONE
        illegal_warning.visibility = View.GONE

        // 这是footer 不显示图片，仅作为打开相册或相机来选择图片的按钮
        val add = AddPicItem(this)
        add.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        add.setOnItemAddPicActionListener(object : OnItemAddPicActionListener {
            override fun onClose() {
                // do nothing
            }

            override fun onItemClick() {
                request = REQUEST_CODE_PIC_SET
                // 请求权限，权限通过后自动打开相册
                requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL)
            }
        })
        mPicSetHorizontalAdapter.addFooterView(add)
        mPicSetHorizontalAdapter.setOnPicSetClickListener(this)
        pic_set.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        pic_set.adapter = mPicSetHorizontalAdapter

        draft_tip_toggle.setOnClickListener(this)
        draft_action.setOnClickListener(this)
        saved_close.setOnClickListener(this)
        pic_set.setOnClickListener(this)
        option_location.setOnClickListener(this)
        option_description.setOnClickListener(this)

        // 判断页面类型
        when (pageType) {
            // 新增物品类型
            AddNewType.ADD_ITEM -> {
                draft_tip_toggle.setText(R.string.addNewSelling)

                option_iwant_color.visibility = View.GONE // 求购卡片颜色
                tag_title.visibility = View.GONE // 求购tag
                option_tag_wrapper.visibility = View.GONE // 求购tag父

                option_title.setOnClickListener(this)
                price_confirm.setOnClickListener(this)
                option_negotiable.setOnClickListener(this)
                option_secondHand.setOnClickListener(this)

                // 初始化封面选项
                cover.setOnItemAddPicActionListener(object : OnItemAddPicActionListener {
                    override fun onClose() {
                        mCoverPath = ""
                        cover.clearImage(true)
                    }

                    override fun onItemClick() {
                        if (cover.imagePath != null && cover.imagePath.isNotBlank()) {
                            XPopup.Builder(this@AddNewActivity)
                                    .isDarkTheme(true)
                                    .asImageViewer(cover.findViewById(R.id.image), mCoverPath, false, -1, -1, -1, true, getColor(R.color.blackAlways), ImageLoader())
                                    .show()
                        } else {
                            request = REQUEST_CODE_COVER
                            requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL)
                        }
                    }
                })
                // 初始化价格选项
                price_input.setOnEditorActionListener { _: TextView?, _: Int, _: KeyEvent? ->
                    price_input.clearFocus()
                    KeyboardUtils.hideSoftInput(this)
                    true
                }
                price_input.filters = arrayOf<InputFilter>(DecimalFilter())

                // 恢复物品草稿
                restoreItemDraft()
            }


            // 修改物品信息类型
            AddNewType.MODIFY_ITEM -> {
                draft_tip_toggle.setText(R.string.modifyInfo)

                option_iwant_color.visibility = View.GONE // 求购卡片颜色
                tag_title.visibility = View.GONE // 求购tag标题
                option_tag_wrapper.visibility = View.GONE // 求购tag父
                cover_title.visibility = View.GONE // 修改物品信息禁止修改封面图片
                cover_wrapper.visibility = View.GONE // 修改物品信息禁止修改封面图片

                option_title.setOnClickListener(this)
                price_confirm.setOnClickListener(this)
                option_negotiable.setOnClickListener(this)
                option_secondHand.setOnClickListener(this)
                price_input.setOnEditorActionListener { _: TextView?, _: Int, _: KeyEvent? ->
                    price_input.clearFocus()
                    KeyboardUtils.hideSoftInput(this)
                    true
                }
                price_input.filters = arrayOf<InputFilter>(DecimalFilter())

                // 使用物品id获取物品信息
                initGoodsInfo()
            }

            // 新增求购类型
            AddNewType.ADD_IWANT -> {
                draft_tip_toggle.setText(R.string.addNewIWant)
                detail_title.setText(R.string.postTitleSaySth)

                pic_set_title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                // 都是新增物品里的选项，求购用不到
                price_title.visibility = View.GONE
                option_title_wrapper.visibility = View.GONE
                title_title.visibility = View.GONE
                option_price.visibility = View.GONE
                option_negotiable.visibility = View.GONE
                option_secondHand.visibility = View.GONE
                cover_title.visibility = View.GONE
                cover_wrapper.visibility = View.GONE

                option_tag.setOnClickListener(this)
                option_iwant_color.setOnClickListener(this)
                cardColorSelectorDialog.setContentView(cardColorSelectorBinding.root)
                cardColorSelectorBinding.apply {
                    iwantColorSelectorDefault.setOnClickListener(this@AddNewActivity)
                    iwantColorSelectorPurple.setOnClickListener(this@AddNewActivity)
                    iwantColorSelectorTheme.setOnClickListener(this@AddNewActivity)
                    iwantColorSelectorHeart.setOnClickListener(this@AddNewActivity)
                    iwantColorSelectorWarning.setOnClickListener(this@AddNewActivity)
                    iwantColorSelectorCancel.setOnClickListener(this@AddNewActivity)
                }

                // 恢复求购草稿
                restoreIWantDraft()
            }

            // 修改求购信息类型
            AddNewType.MODIFY_IWANT -> {
                draft_tip_toggle.setText(R.string.modifyInfo)
                detail_title.setText(R.string.postTitleSaySth)

                pic_set_title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                option_title_wrapper.visibility = View.GONE
                title_title.visibility = View.GONE
                price_title.visibility = View.GONE
                option_price.visibility = View.GONE
                option_negotiable.visibility = View.GONE
                option_secondHand.visibility = View.GONE
                cover_title.visibility = View.GONE
                cover_wrapper.visibility = View.GONE

                option_tag.setOnClickListener(this)
                option_iwant_color.setOnClickListener(this)
                cardColorSelectorDialog.setContentView(cardColorSelectorBinding.root)
                cardColorSelectorBinding.apply {
                    iwant_color_selector_default.setOnClickListener(this@AddNewActivity)
                    iwant_color_selector_purple.setOnClickListener(this@AddNewActivity)
                    iwant_color_selector_theme.setOnClickListener(this@AddNewActivity)
                    iwant_color_selector_heart.setOnClickListener(this@AddNewActivity)
                    iwant_color_selector_warning.setOnClickListener(this@AddNewActivity)
                }

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
            if (pageType == AddNewType.ADD_ITEM) {
                pickPhotoFromAlbum(
                        this,
                        REQUEST_CODE_COVER,
                        1,
                        isSquare = true,
                        isCircle = false,
                        isCropWithoutSpecificShape = false
                )
            } else if (pageType == AddNewType.ADD_IWANT) {
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
                    12,
                    isSquare = false,
                    isCircle = false,
                    isCropWithoutSpecificShape = false
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                InputSetActivity.REQUEST_CODE -> { // 输入页面内容返回
                    val type = data.getIntExtra(InputSetActivity.TYPE, InputSetActivity.TYPE_TITLE)
                    if (type == InputSetActivity.TYPE_TITLE) {
                        option_title.text = data.getStringExtra(InputSetActivity.RESULT)
                    } else {
                        // 此处获得的直接是input set页面传回来的spannable，可以直接显示
                        option_description.text = data.getStringExtra(InputSetActivity.RESULT)
                    }
                }
                REQUEST_CODE_COVER -> { // 封面选择返回
                    val coverMedia = PictureSelector.obtainMultipleResult(data)[0]
                    mCoverPath = coverMedia.cutPath ?: coverMedia.path
                    cover.setImageLocalPath(mCoverPath)
                }
                REQUEST_CODE_PIC_SET -> { // 图片集选择返回
                    mPicSetSelected.addAll(PictureSelector.obtainMultipleResult(data))
                    mPicSetHorizontalAdapter.setList(mPicSetSelected)
                }
                LoginActivity.LOGIN -> { // 在本页面打开登录页面登录并返回
                    setResult(RESULT_OK, data)
                    (application as SAApplication).cacheMyInfoAndToken(
                            data.getSerializableExtra(ConstantUtil.KEY_USER_INFO) as DomainUserInfo.DataBean,
                            data.getSerializableExtra(ConstantUtil.KEY_TOKEN) as DomainToken)
                    JsonCacheUtil.runWithFrequentCheck(this, {
                        checkBeforeSubmit()
                    })
                }
                IWantTagActivity.REQUEST_CODE_IWANT_TAG -> {
                    val tag = data.getSerializableExtra(IWantTagActivity.KEY_SELECTED_TAG)
                            as DomainIWantTags.Data?
                    if (tag != null) {
                        option_tag.text = tag.tag
                        this.mNowTag = tag
                    }
                }
            }
        }
    }

    /**
     * 开始定位
     *
     * 进入页面、点击重新定位时调用
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
     * 提交表单前的检查
     *
     * 1、检查是否频繁提交
     * 2、检查表单必填项是否填写完整
     * 3、检查当前是否已登录
     */
    private fun checkBeforeSubmit() {
        if (checkFormIsLegal()) {
            // 若已经提交完成，则直接返回
            if (isSubmitSuccess) {
                return
            }

            val token = (application as SAApplication).getCachedToken()
            if (token != null) {
                if (mAmapLocation == null) {
                    // 定位异常，拦截上传操作
                    AddNewResultActivity.start(
                            this, false,
                            if (pageType == AddNewType.ADD_ITEM) AddNewResultTips.LOCATION_FAILED_NEW
                            else AddNewResultTips.LOCATION_FAILED_MODIFY)
                } else {
                    when (pageType) {
                        AddNewType.ADD_ITEM, AddNewType.MODIFY_ITEM -> {
                            submitItem(token)
                        }
                        AddNewType.ADD_IWANT, AddNewType.MODIFY_IWANT -> {
                            submitIWant(token)
                        }
                        else -> {
                            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.featureNotSupport)
                        }
                    }
                }
            } else {
                // 未登录则先登录
                LoginActivity.start(this)
            }
        }
    }

    /**
     * 提交物品表单
     */
    @Synchronized
    private fun submitItem(token: DomainToken) {
        showLoading {
            if (pageType == AddNewType.ADD_ITEM) { // 新增物品
//              val spannableJson =
//                      if (option_description.text is Spannable)
//                          SadSpannable.generateJson(this, option_description.text as Spanned)
//                      else option_description.text.toString()
                val mPicSetPaths = ArrayList<String>()
                for (localMedia in mPicSetSelected) {
                    mPicSetPaths.add(localMedia.cutPath
                            ?: localMedia.path)
                }
                addNewViewModel.submitItem(token.access_token, mCoverPath, mPicSetPaths,
                        option_title.text.toString(), option_description.text.toString(),
                        mAmapLocation!!.longitude, mAmapLocation!!.latitude,
                        !option_secondHand.isChecked, option_negotiable.isChecked, price_input.text.toString().toFloat())
                        .apply {
                            observe(this@AddNewActivity, object : Observer<Triple<Boolean, Pair<Int, Boolean>, Boolean>> {
                                override fun onChanged(response: Triple<Boolean, Pair<Int, Boolean>, Boolean>) {
                                    if (!response.first) {
                                        if (response.second.second) {
                                            updateLoadingTip(getString(response.second.first))
                                            showUploadResult(result = false)
                                        } else if (response.second.first != -1) {
                                            updateLoadingTip(getString(R.string.uploadFailedAtIndex, response.second.first))
                                            showUploadResult(result = false, isUploadImageError = true)
                                        }
                                        removeObserver(this) // 上传失败，中断流程，注销观察者
                                    } else if (response.third) {
                                        // 在售物品数量加一
                                        (application as SAApplication).getCachedMyInfo()?.userGoodsOnSaleCount?.plus(1)
                                        showUploadResult(true)
                                        removeObserver(this) // 完成上传，注销观察者
                                    } else {
                                        if (response.second.second) {
                                            // pair中第二个Boolean为true则代表它是res id，可以直接显示
                                            updateLoadingTip(getString(response.second.first))
                                        } else {
                                            // pair中第二个Boolean为false则代表是纯数字，是正在上传的图片的index+1，需要用占位字符
                                            updateLoadingTip(getString(R.string.uploadingIndexPicture, response.second.first))
                                        }
                                    }
                                }
                            })
                        }
            } else if (pageType == AddNewType.MODIFY_ITEM) { // 修改物品信息
                // 将不是原本物品信息中的图片集拿出来
                val mPicSetPaths = ArrayList<String>()
                goodsInfo?.goods_cover_image?.split(",")?.let {
                    for (localMedia in mPicSetSelected) {
                        val path = localMedia.cutPath ?: localMedia.path
                        if (!path.startsWith(ConstantUtil.QINIU_BASE_URL)) {
                            mPicSetPaths.add(path)
                        }
                    }
                }
                addNewViewModel.modifyGoodsInfo(token.access_token, mImagesToDelete, mPicSetPaths, goodsID,
                        option_title.text.toString(), option_description.text.toString(),
                        mAmapLocation!!.longitude, mAmapLocation!!.latitude,
                        !option_secondHand.isChecked, option_negotiable.isChecked, price_input.text.toString().toFloat())
                        .apply {
                            observe(this@AddNewActivity, object : Observer<Triple<Boolean, Pair<Int, Boolean>, Boolean>> {
                                override fun onChanged(response: Triple<Boolean, Pair<Int, Boolean>, Boolean>) {
                                    if (!response.first) {
                                        if (response.second.second) {
                                            updateLoadingTip(getString(response.second.first))
                                            showUploadResult(result = false)
                                        } else if (response.second.first != -1) {
                                            updateLoadingTip(getString(R.string.uploadFailedAtIndex, response.second.first))
                                            showUploadResult(result = false, isUploadImageError = true)
                                        }
                                        removeObserver(this) // 上传失败，中断流程，注销观察者
                                    } else if (response.third) {
                                        showUploadResult(true)
                                        removeObserver(this) // 完成上传，注销观察者
                                    } else {
                                        if (response.second.second) {
                                            // pair中第二个Boolean为true则代表它是res id，可以直接显示
                                            updateLoadingTip(getString(response.second.first))
                                        } else {
                                            // pair中第二个Boolean为false则代表是纯数字，是正在上传的图片的index+1，需要用占位字符
                                            updateLoadingTip(getString(R.string.uploadingIndexPicture, response.second.first))
                                        }
                                    }
                                }
                            })
                        }
            }
        }
    }

    /**
     * 提交求购表单
     */
    @Synchronized
    private fun submitIWant(token: DomainToken) {
        showLoading {
            if (pageType == AddNewType.ADD_IWANT) {
                val mPicSetPaths = ArrayList<String>()
                for (localMedia in mPicSetSelected) {
                    mPicSetPaths.add(localMedia.cutPath
                            ?: localMedia.path)
                }
                addNewViewModel.submitIWant(
                        token.access_token, mNowTag!!.tag_id, mNowCardColor,
                        mPicSetPaths, option_description.text.toString(),
                        mAmapLocation!!.longitude, mAmapLocation!!.latitude)
                        .apply {
                            observe(this@AddNewActivity, object : Observer<Triple<Boolean, Pair<Int, Boolean>, Boolean>> {
                                override fun onChanged(response: Triple<Boolean, Pair<Int, Boolean>, Boolean>) {
                                    if (!response.first) {
                                        if (response.second.second) {
                                            updateLoadingTip(getString(response.second.first))
                                            showUploadResult(result = false)
                                        } else if (response.second.first != -1) {
                                            updateLoadingTip(getString(R.string.uploadFailedAtIndex, response.second.first))
                                            showUploadResult(result = false, isUploadImageError = true)
                                        }
                                        removeObserver(this) // 上传失败，中断流程，注销观察者
                                    } else if (response.third) {
                                        // 求购数量加一
                                        (application as SAApplication).getCachedMyInfo()?.userContactCount?.plus(1)
                                        showUploadResult(true)
                                        removeObserver(this) // 完成上传，注销观察者
                                    } else {
                                        if (response.second.second) {
                                            // pair中第二个Boolean为true则代表它是res id，可以直接显示
                                            updateLoadingTip(getString(response.second.first))
                                        } else {
                                            // pair中第二个Boolean为false则代表是纯数字，是正在上传的图片的index+1，需要用占位字符
                                            updateLoadingTip(getString(R.string.uploadingIndexPicture, response.second.first))
                                        }
                                    }
                                }
                            })
                        }
            } else if (pageType == AddNewType.MODIFY_IWANT) {
                // TODO: 2021/4/18  修改求购信息
            }
        }
    }

    /**
     * 显示表单提交结果
     *
     * @param result 是否成功
     * @param isUploadImageError 是否是上传图片时出的错，如果是，则提示用户换一张图片再试试
     */
    private fun showUploadResult(result: Boolean, isUploadImageError: Boolean = false) {
        dismissLoading {
            AddNewResultActivity.start(
                    this@AddNewActivity,
                    result,
                    when {
                        result
                                && pageType == AddNewType.ADD_ITEM
                                && pageType == AddNewType.ADD_IWANT
                        -> AddNewResultTips.SUCCESS_NEW

                        result
                                && pageType == AddNewType.MODIFY_ITEM
                                && pageType == AddNewType.MODIFY_IWANT
                        -> AddNewResultTips.SUCCESS_MODIFY

                        isUploadImageError -> AddNewResultTips.FAILED_PREPARE

                        !result
                                && pageType == AddNewType.ADD_ITEM
                                && pageType == AddNewType.ADD_IWANT
                        -> AddNewResultTips.FAILED_ADD

                        !result
                                && pageType == AddNewType.MODIFY_ITEM
                                && pageType == AddNewType.MODIFY_IWANT
                        -> AddNewResultTips.FAILED_MODIFY

                        else -> AddNewResultTips.FAILED_ADD
                    })
            if (result) {
                isSubmitSuccess = true // 发送已完毕标志
                finish()
                AnimUtil.activityExitAnimDown(this@AddNewActivity)
            }
        }
    }

    /**
     * 检查表单必填项填写是否完整
     *
     * 若有必填项未填，则将页面跳至未填项目并高亮显示提示用户填写
     */
    private fun checkFormIsLegal(): Boolean {
        var pass = true
        var focusView: View? = null

        if (option_description.text.isBlank()) { // 详细描述
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_description_wrapper)
            focusView = option_description_wrapper
            pass = false
        }
        if (price_input.text.isNotBlank() // 价格输入
                && pageType == AddNewType.ADD_ITEM
                && pageType == AddNewType.MODIFY_ITEM) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_price)
            focusView = price_title
            pass = false
        }
        if (option_title.text.isBlank() // 标题
                && pageType == AddNewType.ADD_ITEM
                && pageType == AddNewType.MODIFY_ITEM) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_title_wrapper)
            focusView = title_title
            pass = false
        }
        if (mPicSetHorizontalAdapter.data.isEmpty() // 新增的图片
                && pageType == AddNewType.ADD_ITEM
                && pageType == AddNewType.MODIFY_ITEM) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, pic_set)
            focusView = pic_set_title
            pass = false
        }
        if (mCoverPath.isBlank() // 封面
                && pageType == AddNewType.ADD_ITEM) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, cover_wrapper)
            focusView = cover_title
            pass = false
        }
        if (mNowTag == null // 求购标签
                || mNowTag?.tag_id == -1
                || mNowTag?.tag?.isBlank() == true
                && pageType == AddNewType.ADD_IWANT) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, option_tag_wrapper)
            focusView = tag_title
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
     * 当用户将之前的草稿清除之后，在页面pause之后当前输入将会覆盖草稿，因此应该将撤销清除的横幅去掉以表面草稿已不可恢复
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
        if (pageType == AddNewType.ADD_ITEM) { // 保存物品表单
            if (!isSubmitSuccess && (mCoverPath.isNotBlank()
                            || mPicSetSelected.size > 0
                            || option_title.text.isNotBlank()
                            || option_description.text.isNotBlank()
                            || price_input.text.isNotBlank()
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
        } else if (pageType == AddNewType.ADD_IWANT) { // 保存帖子表单
            if (!isSubmitSuccess && (mCoverPath.isNotBlank()
                            || mNowTag != null
                            || mPicSetSelected.size > 0 || option_title.text.isNotBlank()
                            || option_description.text.isNotBlank())) {
                addNewViewModel.savePostDraft(
                        mPicSetSelected,
                        mNowTag ?: DomainIWantTags.Data(-1, ""),
                        option_description.text.toString(),
                        mNowCardColor)
            } else {
                addNewViewModel.deletePostDraft()
            }
        }
    }

    /**
     * 恢复物品草稿
     * 在用户清除草稿之后页面调用pause之前可再次恢复
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
                if (mCoverPath.isNotBlank()) {
                    cover.setImageLocalPath(mCoverPath)
                }
                mPicSetHorizontalAdapter.setList(mPicSetSelected)
                option_title.text = draftCache.title
//                option_description.text = SadSpannable(this,
//                        SadSpannable.parseJsonToSpannableJsonStyle(draftCache.description))
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
     * 恢复求购草稿
     * 清除后在页面被pause之前仍可恢复
     */
    private fun restoreIWantDraft() {
        addNewViewModel.restoreIWantDraft().observeOnce(this, { draftCache: NewIWantDraftCache? ->
            if (draftCache != null) {
                hasDraft = true
                saved_draft.visibility = View.VISIBLE // 显示草稿恢复提示
                mPicSetSelected = draftCache.picSet
                if (mCoverPath.isNotBlank()) {
                    cover.setImageLocalPath(mCoverPath)
                }
                mNowTag = draftCache.tag
                option_tag.text = mNowTag?.tag
                mNowCardColor = draftCache.cardColor
                setCardColor()
                mPicSetHorizontalAdapter.setList(mPicSetSelected)
                option_description.text = draftCache.content
            }
            draft_tip.text = getString(R.string.draftRecovered)
            draft_action.text = getString(R.string.clearInput)
            isDraftRestored = true
        })
    }

    /**
     * 类型为[AddNewType.MODIFY_ITEM]时用给进来的物品id获取物品信息，再使用物品信息填充页面
     */
    private fun initGoodsInfo() {
        showLoading()
        goodsViewModel.getGoodsAllDetailByID(goodsID).observeOnce(this) { detailInfo ->
            val goodsInfo = detailInfo?.data
            if (goodsInfo != null) {
                this.goodsInfo = goodsInfo
                when (detailInfo.code) {
                    // 物品已下架
                    ConstantUtil.HTTP_NOT_FOUND -> {
                        DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.FAILED, R.string.goodsDeleted)
                    }

                    // 物品信息获取成功，使用数据填充页面
                    ConstantUtil.HTTP_OK -> {
                        mCoverPath = ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(goodsInfo.goods_cover_image)
                        cover.setImageRemotePath(mCoverPath)

                        val picSet =
                                if (goodsInfo.goods_images.trim().isBlank()) ArrayList()
                                else MyUtil.getArrayFromString(goodsInfo.goods_images)

                        for (i in picSet.indices) {
                            val media = LocalMedia()
                            media.cutPath = ConstantUtil.QINIU_BASE_URL + ImageUtil.fixUrl(picSet[i])
                            mPicSetSelected.add(media) // 修改物品时图片集是完整路径
                        }

                        mPicSetHorizontalAdapter.setList(mPicSetSelected)
                        option_title.text = goodsInfo.goods_name
                        price_input.setText(goodsInfo.goods_price.toString())
                        if (goodsInfo.goods_is_secondHand) {
                            option_secondHand.toggle()
                        }
                        if (goodsInfo.goods_is_bargain) {
                            option_negotiable.toggle()
                        }
                        option_description.text = goodsInfo.goods_content
                    }
                }
                dismissLoading()
            } else {
                dismissLoading {
                    // 物品信息获取失败
                    DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorLoadItemInfo)
                }
            }
        }
    }

    /**
     * 类型为[AddNewType.MODIFY_IWANT]使用给进来的IWant id获取信息并填充页面
     */
    private fun initIWantInfo() {
        showLoading()

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
        mNowTag = null
        option_tag.text = ""
        option_iwant_color.setDrawable(R.drawable.bg_round_primary_dark)
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

    /**
     * 设置卡片颜色
     */
    private fun setCardColor() {
        when (mNowCardColor) {
            IWantRecyclerAdapter.COLOR_DEFAULT ->
                option_iwant_color.setDrawable(R.drawable.bg_round_primary_dark)
            IWantRecyclerAdapter.COLOR_PURPLE ->
                option_iwant_color.setDrawable(R.drawable.bg_round_purple)
            IWantRecyclerAdapter.COLOR_THEME ->
                option_iwant_color.setDrawable(R.drawable.bg_round_theme)
            IWantRecyclerAdapter.COLOR_WARNING ->
                option_iwant_color.setDrawable(R.drawable.bg_round_warning)
            IWantRecyclerAdapter.COLOR_HEART ->
                option_iwant_color.setDrawable(R.drawable.bg_round_heart)
        }
    }

    /**
     * 显示选择卡片颜色弹窗
     *
     * onClick传送门[onClick]
     */
    private fun showColorSelector() {
        val view = cardColorSelectorDialog.delegate.findViewById<View?>(com.google.android.material.R.id.design_bottom_sheet)
        if (view != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from(view)
            bottomSheetBehavior.skipCollapsed = true
            bottomSheetBehavior.isDraggable = false
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    cardColorSelectorDialog.dismiss()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            cardColorSelectorDialog.show()
        }
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
            JsonCacheUtil.runWithFrequentCheck(this, {
                checkBeforeSubmit()
            })
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
        when (v.id) {
            // 标题
            R.id.option_title ->
                InputSetActivity.start(this, InputSetActivity.TYPE_TITLE, option_title.text.toString(), getString(R.string.title))

            // 价格右边的确认按钮
            R.id.price_confirm -> {
                KeyboardUtils.hideSoftInput(v)
                price_input.clearFocus()
            }

            // 定位按钮
            R.id.option_location -> {
                option_location.description = getString(R.string.locating)
                requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL)
            }

            // 是否可议价
            R.id.option_negotiable -> option_negotiable.toggle()

            // 是否二手
            R.id.option_secondHand -> option_secondHand.toggle()

            // 详细描述
            R.id.option_description -> {
                InputSetActivity.start(
                        this,
                        InputSetActivity.TYPE_DESCRIPTION,
//                        SadSpannable.generateJson(this, option_description.text as Spanned),
                        option_description.text.toString(),
                        getString(R.string.detailDescription))
            }

            // 蓝色横幅（表单保存相关）关闭按钮
            R.id.saved_close -> {
                // 点击横幅关闭按钮，将页面标题颜色置为主题色以提示用户页面中存在可恢复的草稿
                AnimUtil.collapse(saved_draft)
                AnimUtil.textColorAnim(this, draft_tip_toggle, R.color.primaryText, R.color.colorAccent)
            }

            // 蓝色横幅（表单保存相关）动作按钮
            R.id.draft_action -> {
                if (pageType != AddNewType.MODIFY_ITEM) {
                    if (isDraftRestored) {
                        clearDraft()
                    } else {
                        if (pageType == AddNewType.ADD_ITEM) {
                            restoreItemDraft()
                        } else {
                            restoreIWantDraft()
                        }
                    }
                }
            }

            // 页面顶部标题，可打开或关闭蓝色横幅（表单保存相关）
            R.id.draft_tip_toggle -> {
                // 页面中有草稿且横幅已被关闭时点击页面标题将可以展示横幅
                if (hasDraft && saved_draft.visibility != View.VISIBLE) {
                    AnimUtil.expand(saved_draft)
                    AnimUtil.textColorAnim(this, draft_tip_toggle, R.color.colorAccent, R.color.primaryText)
                }
            }

            // 求购卡片颜色
            R.id.option_iwant_color -> showColorSelector()

            // 求购标签
            R.id.option_tag -> IWantTagActivity.start(this, mNowTag)

            // 默认颜色
            R.id.iwant_color_selector_default->{
                mNowCardColor = IWantRecyclerAdapter.COLOR_DEFAULT
                setCardColor()
                cardColorSelectorDialog.dismiss()
            }

            // 紫色
            R.id.iwant_color_selector_purple->{
                mNowCardColor = IWantRecyclerAdapter.COLOR_PURPLE
                setCardColor()
                cardColorSelectorDialog.dismiss()
            }

            // 主题色
            R.id.iwant_color_selector_theme->{
                mNowCardColor = IWantRecyclerAdapter.COLOR_THEME
                setCardColor()
                cardColorSelectorDialog.dismiss()
            }

            // 红色
            R.id.iwant_color_selector_heart->{
                mNowCardColor = IWantRecyclerAdapter.COLOR_HEART
                setCardColor()
                cardColorSelectorDialog.dismiss()
            }

            // 警告
            R.id.iwant_color_selector_warning -> {
                mNowCardColor = IWantRecyclerAdapter.COLOR_WARNING
                setCardColor()
                cardColorSelectorDialog.dismiss()
            }

            R.id.iwant_color_selector_cancel -> cardColorSelectorDialog.dismiss()
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
        (application as? SAApplication)?.removeOnApplicationLoginListener(this)
        if (mClient != null) {
            mClient?.stopLocation()
            mClient?.unRegisterLocationListener(this)
            mClient?.onDestroy()
            mClient = null
            mOption = null
        }
    }

    override fun onOriginalImageDeleted(path: String?) {
        if (path != null) {
            mImagesToDelete.add(path)
        }
    }

    /**
     * 在已选的新增图片集中删除一张图片
     *
     * @param pos 删除的位置
     */
    override fun onPicSetDeleteAt(pos: Int) {
        mPicSetSelected.removeAt(pos)
    }

    /**
     * 点击已选的新增图片集以查看图片
     *
     * @param pos 点击的位置
     */
    override fun onPicSetClick(source: ImageView, pos: Int) {
        val data: MutableList<Any> = ArrayList()
        for (pic in mPicSetHorizontalAdapter.data) {
            data.add(pic.cutPath ?: pic.path)
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
                        getColor(R.color.blackAlways),
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