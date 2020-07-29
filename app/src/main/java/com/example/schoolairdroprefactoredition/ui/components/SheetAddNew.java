package com.example.schoolairdroprefactoredition.ui.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.utils.MyUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SheetAddNew extends BottomSheetDialogFragment {

    public SheetAddNew(/* parameters such as user id etc. */) {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(View.inflate(getContext(), R.layout.sheet_selling_add, null));
        dialog.setCanceledOnTouchOutside(false);
        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dialog.dismiss());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_submit)
                dialog.dismiss();
            return true;
        });

        View view = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view);
        MyUtil.setupFullHeight(getContext(), dialog);
        bottomSheetBehavior.setDraggable(false);
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
        return dialog;
    }
}
