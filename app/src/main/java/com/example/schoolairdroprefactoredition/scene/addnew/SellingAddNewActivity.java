package com.example.schoolairdroprefactoredition.scene.addnew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.schoolairdroprefactoredition.databinding.ActivitySellingAddNewBinding;
import com.example.schoolairdroprefactoredition.domain.DomainAuthorize;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.scene.base.PermissionBaseActivity;
import com.example.schoolairdroprefactoredition.scene.settings.LoginActivity;
import com.example.schoolairdroprefactoredition.ui.adapter.HorizontalImageRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.AddPicItem;
import com.example.schoolairdroprefactoredition.utils.AnimUtil;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.DecimalFilter;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.TIP_ADD_NEW_FAILED;
import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.TIP_ADD_NEW_SUCCESS;
import static com.example.schoolairdroprefactoredition.scene.addnew.AddNewResultActivity.TIP_MODIFY_FAILED;

public class SellingAddNewActivity extends PermissionBaseActivity implements View.OnClickListener, AMapLocationListener, HorizontalImageRecyclerAdapter.OnPicSetClickListener, SellingAddNewViewModel.OnRequestListener {

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SellingAddNewActivity.class);
        if (bundle.getSerializable(ConstantUtil.KEY_AUTHORIZE) == null) {
            // 若未登录则带着登录请求打开页面
            startForLogin(context);
        } else {
            // 若已登录则直接打开页面
            intent.putExtras(bundle);
            context.startActivity(intent);
            if (context instanceof AppCompatActivity)
                AnimUtil.activityStartAnimUp((AppCompatActivity) context);
        }
    }

    public static final String PAGE_CONVERT_TO_MODIFY = "?NOTAddNewBUTModify";
    public static final int REQUEST_PAGE_CONVERT_TO_MODIFY = 666;// 请求码 修改信息而非新增

    public static final int REQUEST_CODE_COVER = 219;// 请求码 封面选择
    public static final int REQUEST_CODE_PIC_SET = 11;// 请求码 图片集选择

    private SellingAddNewViewModel viewModel;

    private ActivitySellingAddNewBinding binding;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;
    private AMapLocation mAmapLocation;

    private HorizontalImageRecyclerAdapter mAdapter;

    private int request;

    private String mCoverPath = "";
    private List<LocalMedia> mPicSetSelected = new ArrayList<>();

    private DomainAuthorize token;

    private boolean isDraftRestored = true;
    private boolean isSubmit = false;
    private boolean hasDraft = false;

    private boolean isAddNewMode; // 是否是新增而不是修改物品

    private static void startForLogin(Context context) {
        Intent intent = new Intent(context, SellingAddNewActivity.class);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.startActivityForResult(intent, LoginActivity.LOGIN);
            AnimUtil.activityStartAnimUp((AppCompatActivity) context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellingAddNewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.toolbar));
        viewModel = new ViewModelProvider(this).get(SellingAddNewViewModel.class);
        viewModel.setOnRequestListener(this);

        token = (DomainAuthorize) getIntent().getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
        isAddNewMode = getIntent().getIntExtra(SellingAddNewActivity.PAGE_CONVERT_TO_MODIFY, -1) != REQUEST_PAGE_CONVERT_TO_MODIFY;

        binding.draftTipToggle.setText(isAddNewMode ? R.string.addNewSelling : R.string.modifyInfo);
        binding.savedDraft.setVisibility(View.GONE);
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
                if (binding.cover.getImagePath() != null && !binding.cover.getImagePath().equals("")) {
                    new XPopup.Builder(SellingAddNewActivity.this)
                            .isDarkTheme(true)
                            .isDestroyOnDismiss(true)
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

        if (isAddNewMode) // 若为新增物品则恢复上次草稿，否则初始化给进来的物品信息
            restoreDraft();
        else
            initGoodsInfo();
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
    protected void cameraGranted() {
        super.cameraGranted();
    }

    @Override
    protected void cameraDenied() {
        super.cameraDenied();
    }

    @Override
    protected void albumGranted() {
        super.albumGranted();
        if (request == REQUEST_CODE_COVER)
            MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_COVER,  1, true, false);
        else if (request == REQUEST_CODE_PIC_SET)
            MyUtil.pickPhotoFromAlbum(this, REQUEST_CODE_PIC_SET, 8, false, false);
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
                    if (type == InputSetActivity.TYPE_TITLE)
                        binding.optionTitle.setText(data.getStringExtra(InputSetActivity.RESULT));
                    else
                        binding.optionDescription.setText(data.getStringExtra(InputSetActivity.RESULT));
                }
            } else if (requestCode == REQUEST_CODE_COVER) { // 封面选择返回
                if (data != null) {
                    LocalMedia cover = PictureSelector.obtainMultipleResult(data).get(0);
                    String qPath = cover.getAndroidQToPath();
                    mCoverPath = qPath == null ? cover.getPath() : qPath;
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
                    token = (DomainAuthorize) data.getSerializableExtra(ConstantUtil.KEY_AUTHORIZE);
                    submit();
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
        if (mOption == null)
            mOption = new AMapLocationClientOption();

        binding.optionLocation.setDescription(getString(R.string.locating));
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(true);
        mOption.setLocationCacheEnable(true);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
    }

    /**
     * 提交物品表单
     */
    private void submit() {
        if (checkFormIsLegal()) {
            try {
                if (isAddNewMode) { // 新增物品
                    List<String> mPicSetPaths = new ArrayList<>();
                    for (LocalMedia localMedia : mPicSetSelected) {
                        String qPath = localMedia.getAndroidQToPath();
                        mPicSetPaths.add(qPath == null ? localMedia.getPath() : qPath);
                    }

                    if (token != null) {
                        if (mAmapLocation == null) {
//                            dismissLoading(() ->
                            AddNewResultActivity.start(this, false, AddNewResultActivity.TIP_ADD_NEW_LOCATION_FAILED);
                        } else {
                            showLoading();
                            viewModel.submit(token.getAccess_token(), mCoverPath, mPicSetPaths,
                                    binding.optionTitle.getText().toString(), binding.optionDescription.getText().toString(),
                                    mAmapLocation.getLongitude(), mAmapLocation.getLatitude(),
                                    binding.optionSecondHand.getIsSelected(), !binding.optionNegotiable.getIsSelected(),
                                    Float.parseFloat(binding.priceInput.getText().toString()))
                                    .observe(this, result -> {
                                        // 这里有一个bug 当提交响应失败后 再在同一个页面重试之后成功 将会多次弹出成功提示页面 但实际只提交成功了一次
                                        // 所以这里加一个标志变量 打开一次页面最多只能成功一次 因此成功后即不再弹出
                                        if (!isSubmit) {
                                            dismissLoading(() -> {
                                                AddNewResultActivity.start(this, result.isSuccess(), result.isSuccess() ? TIP_ADD_NEW_SUCCESS : TIP_ADD_NEW_FAILED);
                                                if (result.isSuccess()) {
                                                    isSubmit = true;// 发送已完毕标志

                                                    finish();
                                                    AnimUtil.activityExitAnimDown(this);
                                                }
                                            });
                                        }
                                    });
                        }
                    } else {
                        LoginActivity.startForLogin(this);
                    }
                } else // 修改物品
                    AddNewResultActivity.start(this, true, TIP_MODIFY_FAILED);
            } catch (NullPointerException ignored) {
            }
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
            AnimUtil.whiteBackgroundViewBlinkRed(this, binding.optionDescriptionWrapper);
            focusView = binding.optionDescriptionWrapper;
            pass = false;
        }

        if (binding.priceInput.getText().toString().trim().equals("")) {
            AnimUtil.whiteBackgroundViewBlinkRed(this, binding.optionPrice);
            focusView = binding.priceTitle;
            pass = false;
        }

        if (binding.optionTitle.getText().length() < 1) {
            AnimUtil.whiteBackgroundViewBlinkRed(this, binding.optionTitleWrapper);
            focusView = binding.titleTitle;
            pass = false;
        }

        if (mAdapter.getData().size() < 1) {
            AnimUtil.whiteBackgroundViewBlinkRed(this, binding.picSet);
            focusView = binding.picSetTitle;
            pass = false;
        }

        if (mCoverPath == null || mCoverPath.trim().equals("")) {
            AnimUtil.whiteBackgroundViewBlinkRed(this, binding.coverWrapper);
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

        if (isAddNewMode)
            if (!isSubmit && (!mCoverPath.trim().equals("")
                    || mPicSetSelected.size() > 0
                    || !binding.optionTitle.getText().toString().trim().equals("")
                    || !binding.optionDescription.getText().toString().trim().equals("")
                    || !binding.priceInput.getText().toString().equals("")
                    || binding.optionNegotiable.getIsSelected()
                    || binding.optionSecondHand.getIsSelected())) {
                viewModel.save(mCoverPath,
                        mPicSetSelected,
                        binding.optionTitle.getText().toString(),
                        binding.optionDescription.getText().toString(),
                        binding.priceInput.getText().toString(),
                        binding.optionNegotiable.getIsSelected(),
                        binding.optionSecondHand.getIsSelected());
            } else viewModel.deleteDraft();
    }

    /**
     * 恢复草稿
     * 在用户清除草稿之后再次恢复
     */
    private void restoreDraft() {
        viewModel.recoverDraft().observe(this, draftCache -> {
            if (draftCache != null) {
                hasDraft = true;
                binding.savedDraft.setVisibility(View.VISIBLE);// 显示草稿恢复提示

                mCoverPath = draftCache.getCover();
                mPicSetSelected = draftCache.getPicSet();
                if (mCoverPath != null && !mCoverPath.equals(""))
                    binding.cover.setImageLocalPath(mCoverPath);
                mAdapter.setList(mPicSetSelected);
                binding.optionTitle.setText(draftCache.getTitle());
                binding.optionDescription.setText(draftCache.getDescription());
                binding.priceInput.setText(draftCache.getPrice());
                if (draftCache.isNegotiable())
                    if (!binding.optionNegotiable.getIsSelected())
                        binding.optionNegotiable.toggle();
                if (draftCache.isSecondHand())
                    if (!binding.optionSecondHand.getIsSelected())
                        binding.optionSecondHand.toggle();
            }

            binding.draftTip.setText(getString(R.string.draftRecovered));
            binding.draftAction.setText(getString(R.string.clearInput));
            isDraftRestored = true;
        });
    }

    /**
     * 用给进来的物品信息填充页面
     */
    private void initGoodsInfo() {
        DomainGoodsInfo.DataBean goodsInfo = (DomainGoodsInfo.DataBean) getIntent().getSerializableExtra(ConstantUtil.KEY_GOODS_INFO);
        try {
            mCoverPath = ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + goodsInfo.getGoods_img_cover();
            binding.cover.setImageRemotePath(mCoverPath);
            List<String> picSet = goodsInfo.getGoods_img_set() == null || goodsInfo.getGoods_img_set().trim().equals("") ?
                    new ArrayList<>()
                    : MyUtil.getArrayFromString(goodsInfo.getGoods_img_set());

            for (int i = 0; i < picSet.size(); i++) {
                LocalMedia media = new LocalMedia();
                media.setPath(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + picSet.get(i));
                mPicSetSelected.add(media);
            }
            mAdapter.setList(mPicSetSelected);
            binding.optionTitle.setText(goodsInfo.getGoods_name());
            binding.priceInput.setText(goodsInfo.getGoods_price());
            if (goodsInfo.getGoods_is_brandNew() == 0)
                binding.optionSecondHand.toggle();
            if (goodsInfo.getGoods_is_quotable() == 1)
                binding.optionNegotiable.toggle();
            binding.optionDescription.setText(goodsInfo.getGoods_description());
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * 清除输入
     */
    private void clearDraft() {
        mCoverPath = "";
        mPicSetSelected = new ArrayList<>();
        mAdapter.setList(new ArrayList<>());
        binding.cover.clearImage(true);
        binding.optionTitle.setText("");
        binding.priceInput.setText("");
        if (binding.optionNegotiable.getIsSelected())
            binding.optionNegotiable.toggle();
        if (binding.optionSecondHand.getIsSelected())
            binding.optionSecondHand.toggle();
        binding.optionDescription.setText("");

        binding.draftTip.setText(getString(R.string.draftCleared));
        binding.draftAction.setText(getString(R.string.restoreDraft));
        isDraftRestored = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.selling_add_done, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            AnimUtil.activityExitAnimDown(this);
        } else if (id == R.id.add_submit) {
            submit();
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
        switch (id) {
            case R.id.option_title:
                InputSetActivity.start(this, InputSetActivity.TYPE_TITLE, binding.optionTitle.getText().toString(), getString(R.string.title));
                break;
            case R.id.price_confirm:
                KeyboardUtils.hideSoftInput(v);
                binding.priceInput.clearFocus();
                break;
            case R.id.option_location:
                binding.optionLocation.setDescription(getString(R.string.locating));
                requestPermission(PermissionConstants.LOCATION, RequestType.MANUAL);
                break;
            case R.id.option_negotiable:
                binding.optionNegotiable.toggle();
                break;
            case R.id.option_secondHand:
                binding.optionSecondHand.toggle();
                break;
            case R.id.option_description:
                InputSetActivity.start(this, InputSetActivity.TYPE_DESCRIPTION, binding.optionDescription.getText().toString(), getString(R.string.goods_description));
                break;
            case R.id.saved_close:
                AnimUtil.collapse(binding.savedDraft);
                break;
            case R.id.draft_action:
                if (isAddNewMode)
                    if (isDraftRestored)
                        clearDraft();
                    else
                        restoreDraft();
                break;
            case R.id.draft_tip_toggle:
                if (hasDraft && binding.savedDraft.getVisibility() != View.VISIBLE)
                    AnimUtil.expand(binding.savedDraft);
                break;
            default:
                break;
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
            mClient.stopAssistantLocation();
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
                .isDestroyOnDismiss(true)
                .asImageViewer(source, pos, data, false, false, -1, -1, -1, true, (popupView, position1) -> {
                    popupView.updateSrcView(binding.picSet.getChildAt(position1).findViewById(R.id.image));
                }, new MyUtil.ImageLoader())
                .show();
    }

    @Override
    public void onAddNewItemError() {
        dismissLoading(() -> AddNewResultActivity.start(this, false, TIP_ADD_NEW_FAILED));
    }

    @Override
    public void onModifyInfoError() {
        dismissLoading(() -> AddNewResultActivity.start(this, false, TIP_MODIFY_FAILED));
    }
}
