package com.example.schoolairdroprefactoredition.fragment.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;

public class UserNameFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private String name = "空小投";

    private ImageView mClear;
    private EditText mEditor;
    private TextView mDone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_name, container, false);

        mClear = root.findViewById(R.id.user_name_clear);
        mEditor = root.findViewById(R.id.user_name_editor);
        mDone = root.findViewById(R.id.done);

        mEditor.addTextChangedListener(this);
        mEditor.setOnClickListener(this);
        mDone.setOnClickListener(this);
        mClear.setOnClickListener(this);

        mEditor.setText(name);
        mEditor.requestFocus();
        KeyboardUtils.showSoftInput();

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_name_clear) {
            mEditor.setText("");
        } else if (id == R.id.user_name_editor) {
            if (!mEditor.hasFocus())
                mEditor.requestFocus();
        } else if (id == R.id.done) {
            if (getActivity() != null) {
                KeyboardUtils.hideSoftInput(getActivity());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = mEditor.getText().toString().trim();
        if (txt.equals(""))
            mClear.setVisibility(View.GONE);
        else
            mClear.setVisibility(View.VISIBLE);

        if (txt.equals(name)) {
            mDone.setTextColor(getResources().getColor(R.color.primaryDarkest));
            mDone.setEnabled(false);
        } else {
            mDone.setTextColor(getResources().getColor(R.color.white));
            mDone.setEnabled(true);
        }
    }
}
