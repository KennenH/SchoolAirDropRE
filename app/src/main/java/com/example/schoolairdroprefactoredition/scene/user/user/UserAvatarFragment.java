package com.example.schoolairdroprefactoredition.scene.user.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ImageUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainGetUserInfo;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseChildFragmentViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class UserAvatarFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener, BaseChildFragmentViewModel.OnRequestListener {

    public static final int REQUEST_ALBUM = 99;
    public static final int REQUEST_CAMERA = 88;

    private LoadingPopupView mLoading;

    private BottomSheetDialog mDialog;
    private SimpleDraweeView mAvatar;

    private UserAvatarViewModel viewModel;

    private Bundle bundle;

    private DomainGetUserInfo.DataBean info;

    public static UserAvatarFragment newInstance(Bundle bundle) {
        UserAvatarFragment fragment = new UserAvatarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        info = (DomainGetUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_avatar, container, false);
        viewModel = new ViewModelProvider(this).get(UserAvatarViewModel.class);
        viewModel.setOnRequestListener(this);

        mAvatar = root.findViewById(R.id.avatar);
        mAvatar.setOnLongClickListener(this);
        mLoading = MyUtil.loading(getContext());

        init();

        return root;
    }

    private void init() {
        try {
            mAvatar.setImageURI(ConstantUtil.SCHOOL_AIR_DROP_BASE_URL_NEW + info.getUser_img_path());
        } catch (NullPointerException e) {
            Log.d("UserAvatarFragment", e.toString());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.avatar) {
            showAvatarDialog();
            return true;
        }
        return false;
    }

    private void showAvatarDialog() {
        if (mDialog == null) {
            mDialog = new BottomSheetDialog(getContext());
            mDialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.sheet_avatar, null));

            mDialog.findViewById(R.id.take_photo).setOnClickListener(this);
            mDialog.findViewById(R.id.select_from_album).setOnClickListener(this);
            mDialog.findViewById(R.id.save_to_album).setOnClickListener(this);
            mDialog.findViewById(R.id.cancel).setOnClickListener(this);

            View view1 = mDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            view1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, getContext().getTheme()));
            final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        mDialog.dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
        mDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (data != null) {
                    String uri;
                    LocalMedia photo = PictureSelector.obtainMultipleResult(data).get(0);
                    uri = photo.getPath();
                    if (mLoading == null)
                        mLoading = MyUtil.loading(getContext());
                    mLoading.show();

                    viewModel.updateAvatar(uri, "100001").observe(getViewLifecycleOwner(), bean -> {
                        mLoading.dismiss();

                    });
                }
            } else if (requestCode == REQUEST_ALBUM) {
                if (data != null) {
                    LocalMedia photo = PictureSelector.obtainMultipleResult(data).get(0);
                    mLoading = MyUtil.loading(getContext());
                    mLoading.show();

                    viewModel.updateAvatar(photo.getPath(), "100001").observe(getViewLifecycleOwner(), bean -> {
                        mLoading.dismiss();
                        if (bean.isSuccess())
                            Log.d("UserAvatar", "========Avatar upload success========");
                        else
                            Log.d("UserAvatar", "========Avatar upload failed========");
                    });
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.take_photo:
                MyUtil.takePhoto(this, REQUEST_CAMERA);
                mDialog.dismiss();
                break;
            case R.id.select_from_album:
                MyUtil.pickPhotoFromAlbum(this, REQUEST_ALBUM, new ArrayList<>(), 1);
                mDialog.dismiss();
                break;
            case R.id.save_to_album:
                // todo 保存图片至相册

                Toast.makeText(getContext(), "图片已保存至相册", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onError() {
        requireActivity().runOnUiThread(() -> mLoading.dismiss());
    }

    @Override
    public void onLoading() {

    }
}