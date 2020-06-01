package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.schoolairdroprefactoredition.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SheetAlways extends BottomSheetDialog implements View.OnClickListener {

    private View mDialog;
    private BottomSheetBehavior mBehavior;

    private TextView mPosition;

    public SheetAlways(@NonNull Context context) {
        super(context);
        init();
    }

    public SheetAlways(@NonNull Context context, int theme) {
        super(context, theme);
        init();
    }

    protected SheetAlways(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.sheet_map);
        mDialog = this.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (mDialog != null) {
            mPosition = mDialog.findViewById(R.id.sheet_map_position);
            mDialog.setBackgroundColor(Color.parseColor("#00000000"));
            mDialog.findViewById(R.id.sheet_map_confirm).setOnClickListener(this);

            mBehavior = BottomSheetBehavior.from(mDialog);
            mBehavior.setSkipCollapsed(true);
            mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            setCanceledOnTouchOutside(false);
        }
        show();
    }

    public void setPosition(String position) {
        mPosition.setText(position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sheet_map_confirm) {

        }
    }
}