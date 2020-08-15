package com.example.schoolairdroprefactoredition.fragment.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.fragment.TransactionBaseFragment;

/**
 * 通用
 */
public class SettingsGeneralFragment extends TransactionBaseFragment implements View.OnClickListener {

    private String languageName;

    private FragmentManager manager;

    public SettingsGeneralFragment() {
    }

    public static SettingsGeneralFragment newInstance(Bundle bundle) {
        SettingsGeneralFragment fragment = new SettingsGeneralFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        languageName = getResources().getString(R.string.language);

        if (getActivity() != null)
            manager = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_general, container, false);

        root.findViewById(R.id.settings_general_language).setOnClickListener(this);
        root.findViewById(R.id.settings_general_storage).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_general_language)
            transact(manager, new SettingsLanguageFragment(), languageName);
    }
}
