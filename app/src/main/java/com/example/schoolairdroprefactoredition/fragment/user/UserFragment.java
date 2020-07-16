package com.example.schoolairdroprefactoredition.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

// 用户信息主页面
public class UserFragment extends Fragment implements View.OnClickListener {
    private String userAvatar;
    private String userName;
    private String userSex;
    private String female;
    private String male;
    private String hermaphrodite;

    private PageItem avatar;
    private PageItem name;
    private PageItem sex;

    private BottomSheetDialog dialog;

    private FragmentManager manager;

    public UserFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userAvatar = getResources().getString(R.string.avatar);
        userName = getResources().getString(R.string.setName);
        userSex = getResources().getString(R.string.sex);
        female = getResources().getString(R.string.female);
        male = getResources().getString(R.string.male);
        hermaphrodite = getResources().getString(R.string.hermaphrodite);

        if (getActivity() != null)
            manager = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user, container, false);

        avatar = root.findViewById(R.id.user_avatar);
        name = root.findViewById(R.id.user_name);
        sex = root.findViewById(R.id.user_sex);

        avatar.setOnClickListener(this);
        name.setOnClickListener(this);
        sex.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_avatar) {
            manager.beginTransaction()
                    // 这四个参数的意思分别是
                    // 1 新fragment进入动画
                    // 2 旧fragment退出动画
                    // 3 在新fragment回退时旧fragment的进入动画
                    // 4 在新fragment回退时新fragment的退出动画
                    .setCustomAnimations(R.anim.enter_x_fragment, R.anim.exit_x_fragment, R.anim.popenter_x_fragment, R.anim.popexit_x_fragment)
                    .replace(((ViewGroup) getView().getParent()).getId(), new UserAvatarFragment(), userAvatar)
                    .addToBackStack(userAvatar)
                    .commit();
        } else if (id == R.id.user_name) {
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_y_fragment, R.anim.exit_y_fragment, R.anim.popenter_y_fragment, R.anim.popexit_y_fragment)
                    .replace(((ViewGroup) getView().getParent()).getId(), new UserNameFragment(), userName)
                    .addToBackStack(userName)
                    .commit();
        } else if (id == R.id.user_sex) {
            showSexDialog();
        }
    }

    private void showSexDialog() {
        if (dialog == null  && getContext() != null) {
            dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.sheet_sex, null));

            dialog.findViewById(R.id.female).setOnClickListener(v -> {
                sex.setDescription(female);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.male).setOnClickListener(v -> {
                sex.setDescription(male);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.hermaphrodite).setOnClickListener(v -> {
                sex.setDescription(hermaphrodite);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.cancel).setOnClickListener(v -> {
                dialog.dismiss();
            });


            View view1 = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            view1.setBackground(getResources().getDrawable(R.drawable.transparent, getContext().getTheme()));
            final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view1);
            bottomSheetBehavior.setSkipCollapsed(true);
            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dialog.dismiss();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
            dialog.show();
        } else
            dialog.show();
    }
}