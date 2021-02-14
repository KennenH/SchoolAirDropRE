package com.example.schoolairdroprefactoredition.scene.addnew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.application.Application;
import com.example.schoolairdroprefactoredition.databinding.ActivitySellingAddNewBinding;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainToken;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.domain.GoodsDetailInfo;
import com.example.schoolairdroprefactoredition.domain.DomainPurchasing;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HorizontalImageRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DialogUtil;
import com.example.schoolairdroprefactoredition.utils.ImageUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.example.schoolairdroprefactoredition.utils.filters.DecimalFilter;
import com.example.schoolairdroprefactoredition.viewmodel.AddNewViewModel;
import com.example.schoolairdroprefactoredition.viewmodel.GoodsViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips.FAILED_ADD;
import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips.LOCATION_FAILED_NEW_ITEM;
import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips.SUCCESS_NEW_ITEM;
import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.AddNewResultTips.SUCCESS_NEW_POST;

public class AddNewActivity extends PermissionBaseActivity implements View.OnClickListener, AMapLocationListener, HorizontalImageRecyclerAdapter.OnPicSetClickListener {

    /**
     * 发布物品或新帖子
     * 添加物品或帖子使用该方法
     * 修改物品使用{@link AddNewActivity#start(Context, DomainPurchasing.DataBean)}
     *
     * @param type 页面类型 one of {@link AddNewType#ADD_ITEM} {@link AddNewType#ADD_POST}
     */
    public static void start(Context context, @AddNewType int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantUtil.KEY_ADD_NEW_TYPE, type);
        start(context, bundle);
    }

    /**
     * 修改物品信息
     * 添加物品或帖子使用{@link AddNewActivity#start(Context, int)}
     *
     * @param goodsInfo 物品基本信息
     */
    public static void start(Context context, DomainPurchasing.DataBean goodsInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.MODIFY_ITEM);
        bundle.putSerializable(ConstantUtil.KEY_GOODS_BASE_INFO, goodsInfo);
        start(context, bundle);
    }

    /**
     * 打开页面
     * 若不知道这个bundle里需要传什么参数，请按情况使用以下打开方式
     * <p>
     * 新增物品或帖子: {@link AddNewActivity#start(Context, int)}
     * 修改物品信息: {@link AddNewActivity#start(Context, DomainPurchasing.DataBean)}
     */
    private static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddNewActivity.class);
        intent.putExtras(bundle);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).startActivityForResult(intent, LoginActivity.LOGIN);
            AnimUtil.activityStartAnimUp((AppCompatActivity) context);
        }
    }

    /**
     * 添加页面的类型
     */
    public @interface AddNewType {
        int ADD_ITEM = 678; // 上传新物品
        int MODIFY_ITEM = 456; // 修改物品
        int ADD_POST = 234; // 上传新的帖子
    }

    public static final int REQUEST_CODE_COVER = 219;// 请求码 封面选择
    public static final int REQUEST_CODE_PIC_SET = 11;// 请求码 图片集选择

    private AddNewViewModel addNewViewModel;
    private GoodsViewModel goodsViewModel;

    private ActivitySellingAddNewBinding binding;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private AMapLocation mAmapLocation;

    private HorizontalImageRecyclerAdapter mAdapter;

    private int request; // 相册访问码

    private String mCoverPath = "";
    private float mHWRatio = 1.0f;
    private List<LocalMedia> mPicSetSelected = new ArrayList<>();

    private DomainPurchasing.DataBean goodsBaseInfo;
    private GoodsDetailInfo.DataBean goodsDetailInfo;

    private boolean isDraftRestored = true;
    private boolean isSubmit = false;
    private boolean hasDraft = false;

    @AddNewType
    private int addNewType = AddNewType.ADD_ITEM; // 页面新增类型

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellingAddNewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.toolbar));

        addNewViewModel = new ViewModelProvider(this).get(AddNewViewModel.class);
        goodsViewModel = new ViewModelProvider(this).get(GoodsViewModel.class);

        goodsBaseInfo = (DomainPurchasing.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_GOODS_BASE_INFO);
        addNewType = getIntent().getIntExtra(ConstantUtil.KEY_ADD_NEW_TYPE, AddNewType.ADD_ITEM);

        binding.savedDraft.setVisibility(View.GONE);
        binding.serverTip.setVisibility(View.GONE);
        binding.illegalWarning.setVisibility(View.GONE);

        binding.draftTipToggle.setOnClickListener(this);
        binding.draftAction.setOnClickListener(this);
        binding.savedClose.setOnClickListener(this);
        binding.picSet.setOnClickListener(this);
        binding.optionTitle.setOnClickListener(this);
        binding.priceConfirm.setOnClickListener(this);
        binding.optionLocation.setOnClickListener(this);
        binding.optionNegotiable.setOnClickListener(this);
        binding.optionSecondHand.setOnClickListener(this);
        binding.optionDescription.setOnClickListener(this);
        binding.priceInput.setOnEditorActionListener((v, actionId, event) -> {
            binding.priceInput.clearFocus();
            KeyboardUtils.hideSoftInput(this);
            return true;
        });

        binding.priceInput.setFilters(new InputFilter[]{new DecimalFilter()});
        binding.picSet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        //封面
        binding.cover.setOnItemAddPicActionListener(new AddPicItem.OnItemAddPicActionListener() {
            @Override
            public void onClose() {
                mCoverPath = "";
                binding.cover.clearImage(true);
            }

            @Override
            public void onItemClick() {
                if (binding.cover.getImagePath() != null && !"".equals(binding.cover.getImagePath())) {
                    new XPopup.Builder(AddNewActivity.this)
                            .isDarkTheme(true)
                            .asImageViewer(binding.cover.findViewById(R.id.image), mCoverPath, false, -1, -1, -1, true, new MyUtil.ImageLoader())
                            .show();
                } else {
                    request = REQUEST_CODE_COVER;
                    requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL);
                }
            }
        });
        // 这是footer 不显示图片，仅作为相册选择图片的按钮
        AddPicItem add = new AddPicItem(this);
        add.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        add.setOnItemAddPicActionListener(new AddPicItem.OnItemAddPicActionListener() {
            @Override
            public void onClose() {
                // do nothing
            }

            @Override
            public void onItemClick() {
                request = REQUEST_CODE_PIC_SET;
                requestPermission(PermissionConstants.STORAGE, RequestType.MANUAL);
            }
        });

        mAdapter = new HorizontalImageRecyclerAdapter();
        mAdapter.setOnPicSetClickListener(this);
        mAdapter.addFooterView(add);
        binding.picSet.setAdapter(mAdapter);

        requestPermission(PermissionConstants.LOCATION, RequestType.AUTO);

        initPageAccordingToType();
    }

    /**
     * 根据页面类型初始化页面数据
     */
    private void initPageAccordingToType() {
        switch (addNewType) {
            case AddNewType.ADD_ITEM:
                binding.draftTipToggle.setText(R.string.addNewSelling);
                binding.tagTitle.setVisibility(View.GONE);
                binding.optionTagWrapper.setVisibility(View.GONE);
                binding.optionAnonymous.setVisibility(View.GONE);
                restoreItemDraft();
                break;
            case AddNewType.ADD_POST:
                binding.priceTitle.setVisibility(View.GONE);
                binding.optionPrice.setVisibility(View.GONE);
                binding.optionNegotiable.setVisibility(View.GONE);
                binding.optionSecondHand.setVisibility(View.GONE);
                binding.draftTipToggle.setText(R.string.addNewPost);
                binding.detailTitle.setText(R.string.postTitleSaySth);
                restorePostDraft();
                break;
            case AddNewType.MODIFY_ITEM:
                binding.draftTipToggle.setText(R.string.modifyInfo);
                binding.tagTitle.setVisibility(View.GONE);
                binding.optionTagWrapper.setVisibility(View.GONE);
                binding.optionAnonymous.setVisibility(View.GONE);
                initGoodsInfo();
                break;
        }
    }

    @Override
    protected void locationGranted() {
        super.locationGranted();
        startLocation();
    }

    @Override
    protected void locationDenied() {
        super.locationDenied();
        binding.optionLocation.setDescription(getString(R.string.errorRetry));
    }

    @Override
    protected void albumGranted() {
        super.albumGranted();
        if (request == REQUEST_CODE_COVER) {
            if (addNewType == AddNewType.ADD_ITEM) {
                MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_COVER, 1, true, false, false);
            } else if (addNewType == AddNewType.ADD_POST) {
                MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_COVER, 1, false, false, true);
            }
        } else if (request == REQUEST_CODE_PIC_SET) {
            MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_PIC_SET, 8, false, false, false);
        }
    }

    @Override
    protected void albumDenied() {
        super.albumDenied();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == InputSetActivity.RESULT_CODE) { // 输入页面返回
                if (data != null) {
                    int type = data.getIntExtra(InputSetActivity.TYPE, InputSetActivity.TYPE_TITLE);
                    if (type == InputSetActivity.TYPE_TITLE) {
                        binding.optionTitle.setText(data.getStringExtra(InputSetActivity.RESULT));
                    } else {
                        binding.optionDescription.setText(data.getStringExtra(InputSetActivity.RESULT));
                    }
                }
            } else if (requestCode == REQUEST_CODE_COVER) { // 封面选择返回
                if (data != null) {
                    LocalMedia cover = PictureSelector.obtainMultipleResult(data).get(0);
                    String qPath = cover.getAndroidQToPath();
                    mCoverPath = qPath == null ? cover.getCutPath() : qPath;
                    mHWRatio = (float) cover.getHeight() / (float) cover.getWidth();
                    binding.cover.setImageLocalPath(mCoverPath);
                }
            } else if (requestCode == REQUEST_CODE_PIC_SET) { // 图片集选择返回
                if (data != null) {
                    mPicSetSelected.addAll(PictureSelector.obtainMultipleResult(data));
                    mAdapter.setList(mPicSetSelected);
                }
            } else if (requestCode == LoginActivity.LOGIN) { // 在本页面打开登录页面登录并返回
                if (data != null) {
                    setResult(Activity.RESULT_OK, data);
                    ((Application) getApplication()).cacheMyInfoAndToken(
                            (DomainUserInfo.DataBean) data.getSerializableExtra(ConstantUtil.KEY_USER_INFO),
                            (DomainToken) data.getSerializableExtra(ConstantUtil.KEY_TOKEN));
                    try {
                        submit();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    /**
     * 定位
     */
    private void startLocation() {
        if (mClient == null) {
            mClient = new AMapLocationClient(this);
            mClient.setLocationListener(this);
        }
        if (mOption == null) {
            mOption = new AMapLocationClientOption();
        }

        binding.optionLocation.setDescription(getString(R.string.locating));
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
    }

    /**
     * 提交表单
     */
    private void submit() {
        if (checkFormIsLegal()) {
            if (addNewType == AddNewType.ADD_POST) {
                submitPost();
            } else {
                submitItem();
            }
        }
    }

    /**
     * 提交物品表单
     */
    private void submitItem() {
        if (addNewType == AddNewType.ADD_ITEM) { // 新增物品
            DomainToken token = ((Application) getApplication()).getCachedToken();
            if (token != null) {
                if (mAmapLocation == null) {
//                            dismissLoading(() ->
                    AddNewResultActivity.start(this, false, LOCATION_FAILED_NEW_ITEM);
                } else {
                    showLoading(() -> {
                                ArrayList<String> mPicSetPaths = new ArrayList<>();
                                for (LocalMedia localMedia : mPicSetSelected) {
                                    String qPath = localMedia.getAndroidQToPath();
                                    mPicSetPaths.add(qPath == null ? localMedia.getPath() : qPath);
                                }

                                addNewViewModel.submitItem(token.getAccess_token(), mCoverPath, mPicSetPaths,
                                        binding.optionTitle.getText().toString(), binding.optionDescription.getText().toString(),
                                        mAmapLocation.getLongitude(), mAmapLocation.getLatitude(),
                                        !binding.optionSecondHand.getIsChecked(), binding.optionNegotiable.getIsChecked(),
                                        Float.parseFloat(binding.priceInput.getText().toString()))
                                        .observe(this, result -> {
                                            // 这里有一个bug 当提交响应失败后 再在同一个页面重试之后成功 将会多次弹出成功提示页面 但实际只提交成功了一次
                                            // 所以这里加一个标志变量 打开一次页面最多只能成功一次 因此成功后即不再弹出
                                            if (!isSubmit) {
                                                dismissLoading(() -> {
                                                    AddNewResultActivity.start(this, result, result ? SUCCESS_NEW_ITEM : FAILED_ADD);
                                                    if (result) {
                                                        isSubmit = true;// 发送已完毕标志

                                                        finish();
                                                        AnimUtil.activityExitAnimDown(this);
                                                    }
                                                });
                                            }
                                        });
                            }
                    );
                }
            } else {
                LoginActivity.Companion.start(this);
            }
        } else if (addNewType == AddNewType.MODIFY_ITEM) { // 修改物品
            showLoading(() -> {
                List<String> mPicSetPaths = new ArrayList<>();
                for (LocalMedia localMedia : mPicSetSelected) {
                    String qPath = localMedia.getAndroidQToPath();
                    mPicSetPaths.add(qPath == null ? localMedia.getPath() : qPath);
                }
            });
        }
    }

    /**
     * 提交新帖表单
     */
    private void submitPost() {
        DomainToken token = ((Application) getApplication()).getCachedToken();
        if (token != null) {
            if (mAmapLocation == null) {
                AddNewResultActivity.start(this, false, LOCATION_FAILED_NEW_ITEM);
            } else {
                showLoading(() -> {
                            List<String> mPicSetPaths = new ArrayList<>();
                            for (LocalMedia localMedia : mPicSetSelected) {
                                String qPath = localMedia.getAndroidQToPath();
                                mPicSetPaths.add(qPath == null ? localMedia.getPath() : qPath);
                            }

                            addNewViewModel.submitPost(token.getAccess_token(), mCoverPath, mHWRatio, mPicSetPaths,
                                    binding.optionTitle.getText().toString(), binding.optionDescription.getText().toString(),
                                    mAmapLocation.getLongitude(), mAmapLocation.getLatitude())
                                    .observe(this, result -> {
                                        // 这里有一个bug 当提交响应失败后 再在同一个页面重试之后成功 将会多次弹出成功提示页面 但实际只提交成功了一次
                                        // 所以这里加一个标志变量 打开一次页面最多只能成功一次 因此成功后即不再弹出
                                        if (!isSubmit) {
                                            dismissLoading(() -> {
                                                AddNewResultActivity.start(this, result, result ? SUCCESS_NEW_POST : FAILED_ADD);
                                                if (result) {
                                                    isSubmit = true;// 发送已完毕标志

                                                    finish();
                                                    AnimUtil.activityExitAnimDown(this);
                                                }
                                            });
                                        }
                                    });
                        }
                );
            }
        } else {
            LoginActivity.Companion.start(this);
        }
    }

    /**
     * 检查表单填写是否完整
     * 若有必填项未填，则将页面跳至未填项目并高亮显示提示用户填写
     */
    private boolean checkFormIsLegal() {
        boolean pass = true;
        View focusView = null;
        if (binding.optionDescription.getText().length() < 1) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, binding.optionDescriptionWrapper);
            focusView = binding.optionDescriptionWrapper;
            pass = false;
        }

        if (addNewType != AddNewType.ADD_POST && binding.priceInput.getText().toString().trim().equals("")) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, binding.optionPrice);
            focusView = binding.priceTitle;
            pass = false;
        }

        if (binding.optionTitle.getText().length() < 1) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, binding.optionTitleWrapper);
            focusView = binding.titleTitle;
            pass = false;
        }

        if (mAdapter.getData().size() < 1) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, binding.picSet);
            focusView = binding.picSetTitle;
            pass = false;
        }

        if (mCoverPath == null || mCoverPath.trim().equals("")) {
            AnimUtil.primaryBackgroundViewBlinkRed(this, binding.coverWrapper);
            focusView = binding.coverTitle;
            pass = false;
        }

        if (!pass) {
            focusView.requestFocus();
            focusView.clearFocus();
        }

        return pass;
    }

    /**
     * 保存用户本次输入
     * 若未提交成功且输入不为空则保存草稿
     * 若为修改物品则无需保存草稿
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (addNewType == AddNewType.ADD_ITEM) { // 保存物品表单
            if (!isSubmit && (!mCoverPath.trim().equals("")
                    || mPicSetSelected.size() > 0
                    || !binding.optionTitle.getText().toString().trim().equals("")
                    || !binding.optionDescription.getText().toString().trim().equals("")
                    || !binding.priceInput.getText().toString().equals("")
                    || binding.optionNegotiable.getIsChecked()
                    || binding.optionSecondHand.getIsChecked())) {
                addNewViewModel.saveItemDraft(mCoverPath,
                        mPicSetSelected,
                        binding.optionTitle.getText().toString(),
                        binding.optionDescription.getText().toString(),
                        binding.priceInput.getText().toString(),
                        binding.optionNegotiable.getIsChecked(),
                        binding.optionSecondHand.getIsChecked());
            } else {
                addNewViewModel.deleteItemDraft();
            }
        } else if (addNewType == AddNewType.ADD_POST) { // 保存帖子表单
            if (!isSubmit && (!mCoverPath.trim().equals("")
                    || mPicSetSelected.size() > 0
                    || !binding.optionTitle.getText().toString().trim().equals("")
                    || !binding.optionDescription.getText().toString().trim().equals(""))) {
                addNewViewModel.savePostDraft(mCoverPath,
                        mHWRatio,
                        mPicSetSelected,
                        binding.optionTag.getText().toString(),
                        binding.optionAnonymous.getIsChecked(),
                        binding.optionTitle.getText().toString(),
                        binding.optionDescription.getText().toString());
            } else {
                addNewViewModel.deletePostDraft();
            }
        }
    }

    /**
     * 恢复物品草稿
     * 在用户清除草稿之后再次恢复
     */
    private void restoreItemDraft() {
        addNewViewModel.restoreItemDraft().observe(this, draftCache -> {
            if (draftCache != null) {
                hasDraft = true;
                binding.savedDraft.setVisibility(View.VISIBLE);// 显示草稿恢复提示

                mCoverPath = draftCache.getCover();
                mPicSetSelected = draftCache.getPicSet();

                if (mCoverPath != null && !mCoverPath.equals("")) {
                    binding.cover.setImageLocalPath(mCoverPath);
                }
                mAdapter.setList(mPicSetSelected);
                binding.optionTitle.setText(draftCache.getTitle());
                binding.optionDescription.setText(draftCache.getDescription());
                binding.priceInput.setText(draftCache.getPrice());
                if (draftCache.isNegotiable()) {
                    if (!binding.optionNegotiable.getIsChecked()) {
                        binding.optionNegotiable.toggle();
                    }
                }
                if (draftCache.isSecondHand()) {
                    if (!binding.optionSecondHand.getIsChecked()) {
                        binding.optionSecondHand.toggle();
                    }
                }
            }

            binding.draftTip.setText(getString(R.string.draftRecovered));
            binding.draftAction.setText(getString(R.string.clearInput));
            isDraftRestored = true;
        });
    }

    /**
     * 恢复帖子草稿
     * 清除后在关闭之前仍可恢复
     */
    private void restorePostDraft() {
        addNewViewModel.restorePostDraft().observe(this, draftCache -> {
            if (draftCache != null) {
                hasDraft = true;
                binding.savedDraft.setVisibility(View.VISIBLE);// 显示草稿恢复提示

                mCoverPath = draftCache.getCover();
                mHWRatio = draftCache.getHwRatio();
                mPicSetSelected = draftCache.getPicSet();

                if (mCoverPath != null && !mCoverPath.equals("")) {
                    binding.cover.setImageLocalPath(mCoverPath);
                }
                mAdapter.setList(mPicSetSelected);
                binding.optionTitle.setText(draftCache.getTitle());
                binding.optionDescription.setText(draftCache.getContent());
            }

            binding.draftTip.setText(getString(R.string.draftRecovered));
            binding.draftAction.setText(getString(R.string.clearInput));
            isDraftRestored = true;
        });
    }

    /**
     * 用给进来的物品id获取物品信息
     * 在使用物品信息填充页面
     */
    private void initGoodsInfo() {
        if (goodsBaseInfo == null) return;

        goodsViewModel.getGoodsDetailByID(goodsBaseInfo.getGoods_id()).observe(this, detail -> {
            goodsDetailInfo = detail.getData().get(0);
        });

        DomainGoodsInfo.DataBean goodsInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_GOODS_INFO);

        try {
            mCoverPath = ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ImageUtil.fixUrl(goodsInfo.getGoods_img_cover());
            binding.cover.setImageRemotePath(mCoverPath);
            List<String> picSet = goodsInfo.getGoods_img_set() == null || goodsInfo.getGoods_img_set().trim().equals("") ?
                    new ArrayList<>() : MyUtil.getArrayFromString(goodsInfo.getGoods_img_set());

            for (int i = 0; i < picSet.size(); i++) {
                LocalMedia media = new LocalMedia();
                media.setPath(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL + ImageUtil.fixUrl(picSet.get(i)));
                mPicSetSelected.add(media);
            }
            mAdapter.setList(mPicSetSelected);
            binding.optionTitle.setText(goodsInfo.getGoods_name());
            binding.priceInput.setText(goodsInfo.getGoods_price());
            if (goodsInfo.getGoods_is_brandNew() == 0) {
                binding.optionSecondHand.toggle();
            }
            if (goodsInfo.getGoods_is_quotable() == 1) {
                binding.optionNegotiable.toggle();
            }
            binding.optionDescription.setText(goodsInfo.getGoods_description());
        } catch (NullPointerException ignored) {
            DialogUtil.showCenterDialog(this, DialogUtil.DIALOG_TYPE.ERROR_UNKNOWN, R.string.errorLoadItemInfo);
        }
    }

    /**
     * 清除输入
     */
    private void clearDraft() {
        mCoverPath = "";
        mPicSetSelected.clear();
        mAdapter.setList(mPicSetSelected);
        binding.cover.clearImage(true);
        binding.optionTitle.setText("");
        binding.priceInput.setText("");
        if (binding.optionNegotiable.getIsChecked()) {
            binding.optionNegotiable.toggle();
        }
        if (binding.optionSecondHand.getIsChecked()) {
            binding.optionSecondHand.toggle();
        }
        binding.optionDescription.setText("");

        binding.draftTip.setText(getString(R.string.draftCleared));
        binding.draftAction.setText(getString(R.string.restoreDraft));
        isDraftRestored = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selling_add_done, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            AnimUtil.activityExitAnimDown(this);
            return true;
        } else if (id == R.id.add_submit) {
            item.setEnabled(false);
            try {
                submit();
            } catch (Exception ignored) {
            }
            item.setEnabled(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.activityExitAnimDown(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.option_title) {
            InputSetActivity.Companion.start(this, InputSetActivity.TYPE_TITLE, binding.optionTitle.getText().toString(), getString(R.string.title));
        } else if (id == R.id.price_confirm) {
            KeyboardUtils.hideSoftInput(v);
            binding.priceInput.clearFocus();
        } else if (id == R.id.option_location) {
            binding.optionLocation.setDescription(getString(R.string.locating));
            requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL);
        } else if (id == R.id.option_negotiable) {
            binding.optionNegotiable.toggle();
        } else if (id == R.id.option_secondHand) {
            binding.optionSecondHand.toggle();
        } else if (id == R.id.option_description) {
            InputSetActivity.Companion.start(this, InputSetActivity.TYPE_DESCRIPTION, binding.optionDescription.getText().toString(), getString(R.string.goods_description));
        } else if (id == R.id.saved_close) {
            AnimUtil.collapse(binding.savedDraft);
            AnimUtil.textColorAnim(this, binding.draftTipToggle, R.color.primaryText, R.color.colorAccent);
        } else if (id == R.id.draft_action) {
            if (addNewType != AddNewType.MODIFY_ITEM) {
                if (isDraftRestored) {
                    clearDraft();
                } else {
                    if (addNewType == AddNewType.ADD_ITEM) {
                        restoreItemDraft();
                    } else {
                        restorePostDraft();
                    }
                }
            }
        } else if (id == R.id.draft_tip_toggle) {
            if (hasDraft && binding.savedDraft.getVisibility() != View.VISIBLE) {
                AnimUtil.expand(binding.savedDraft);
                AnimUtil.textColorAnim(this, binding.draftTipToggle, R.color.colorAccent, R.color.primaryText);
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            mAmapLocation = aMapLocation;
            if (aMapLocation.getErrorCode() == 0) {
                StringBuilder location = new StringBuilder();
                binding.optionLocation.setDescription(location
                        .append(aMapLocation.getProvince())
                        .append(aMapLocation.getCity())
                        .append(aMapLocation.getDistrict())
                        .append(aMapLocation.getStreet())
                        .append(aMapLocation.getStreetNum())
                        .toString());
            } else binding.optionLocation.setDescription(getString(R.string.errorLocation));
        } else binding.optionLocation.setDescription(getString(R.string.errorLocation));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mClient != null) {
            mClient.stopLocation();
            mClient.unRegisterLocationListener(this);
            mClient.onDestroy();
            mClient = null;
            mOption = null;
        }
    }

    /**
     * 在已选的图片集中删除一张图片
     *
     * @param pos 删除的位置
     */
    @Override
    public void onPicSetDeleteAt(int pos) {
        mPicSetSelected.remove(pos);
    }

    /**
     * 点击已选的图片集
     * 查看图片
     *
     * @param pos 点击的位置
     */
    @Override
    public void onPicSetClick(ImageView source, int pos) {
        List<Object> data = new ArrayList<>();
        List<LocalMedia> adapterData = mAdapter.getData();
        for (LocalMedia pic : adapterData)
            data.add(pic.getPath() == null ? pic.getAndroidQToPath() : pic.getPath());

        new XPopup.Builder(this)
                .isDarkTheme(true)
                .asImageViewer(source, pos, data, false, false, -1, -1, -1, true, (popupView, position1) -> {
                    popupView.updateSrcView(binding.picSet.getChildAt(position1).findViewById(R.id.image));
                }, new MyUtil.ImageLoader())
                .show();
    }
}
