package com.example.schoolairdroprefactoredition.fragment.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserAvatarFragment extends Fragment implements View.OnLongClickListener {
    private BottomSheetDialog dialog;
    private ImageView mAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_avatar, container, false);

        mAvatar = root.findViewById(R.id.avatar);

        mAvatar.setOnLongClickListener(this);

        return root;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.avatar) {
            showAvatarDialog();
        }
        return false;
    }

    private void showAvatarDialog() {
        if (dialog == null) {
            dialog = new BottomSheetDialog(getContext());
            dialog.setContentView(LayoutInflater.from(getContext()).inflate(R.layout.sheet_avatar, null));

            dialog.findViewById(R.id.take_photo).setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.select_from_album).setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.save_to_album).setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.cancel).setOnClickListener(v -> {
                dialog.dismiss();
            });

            View view1 = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            view1.setBackground(getResources().getDrawable(R.drawable.transparent, getContext().getTheme()));
            final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
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