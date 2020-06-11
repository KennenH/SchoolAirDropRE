package com.example.schoolairdroprefactoredition.fragment.settings;

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

/**
 * 通用
 */
public class SettingsGeneralFragment extends Fragment implements View.OnClickListener {

    private String languageName;

    private FragmentManager manager;

    public SettingsGeneralFragment() {
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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_general, container, false);

        root.findViewById(R.id.settings_general_language).setOnClickListener(this);
        root.findViewById(R.id.settings_general_storage).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_general_language)
            manager.beginTransaction()
                    // 这四个参数的意思分别是
                    // 1 新fragment进入动画
                    // 2 旧fragment退出动画
                    // 3 在新fragment回退时旧fragment的进入动画
                    // 4 在新fragment回退时新fragment的退出动画
                    .setCustomAnimations(R.anim.enter_y_fragment, R.anim.exit_y_fragment, R.anim.popenter_y_fragment, R.anim.popexit_y_fragment)
                    .replace(((ViewGroup) getView().getParent()).getId(), new SettingsLanguageFragment(), languageName)
                    .addToBackStack(languageName)
                    .commit();
    }
}
