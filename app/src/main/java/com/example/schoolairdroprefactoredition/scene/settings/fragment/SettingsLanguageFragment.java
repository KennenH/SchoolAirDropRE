package com.example.schoolairdroprefactoredition.scene.settings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;

/**
 * 语言
 */
public class SettingsLanguageFragment extends Fragment implements View.OnClickListener {
    private ViewGroup mParent;
    private PageItem mSimplifiedCN;
    private PageItem mEN;

    public SettingsLanguageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_language, container, false);

        mParent = root.findViewById(R.id.settings_language_parent);
        mSimplifiedCN = root.findViewById(R.id.settings_language_cn);
        mEN = root.findViewById(R.id.settings_language_en);

        mSimplifiedCN.setOnClickListener(this);
        mEN.setOnClickListener(this);

        return root;
    }

    private void deSelectAll() {
        for (int i = 0; i < mParent.getChildCount(); i++) {
            PageItem item = (PageItem) mParent.getChildAt(i);
            item.deSelect();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_language_cn) {
            if (!((PageItem) v).isItemSelected()) {
                deSelectAll();
            }
            mSimplifiedCN.toggle();
        } else if (id == R.id.settings_language_en) {
            if (!((PageItem) v).isItemSelected()) {
                deSelectAll();
            }
            mEN.toggle();
        }
    }
}