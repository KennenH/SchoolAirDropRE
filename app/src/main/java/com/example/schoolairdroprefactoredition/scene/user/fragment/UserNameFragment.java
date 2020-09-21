package com.example.schoolairdroprefactoredition.scene.user.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.domain.DomainUserInfo;
import com.example.schoolairdroprefactoredition.scene.user.ResultViewModel;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;

@Deprecated
public class UserNameFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private ResultViewModel viewModel;

    private String name = "";

    private ImageView mClear;
    private EditText mEditor;
    private TextView mDone;

    private Bundle bundle;

    public static UserFragment newInstance(Bundle bundle) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();

        try {
            name = ((DomainUserInfo.DataBean) bundle.getSerializable(ConstantUtil.KEY_USER_INFO)).getUname();
        } catch (NullPointerException e) {
            Log.d("UserNameFragment", e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_name, container, false);
        viewModel = new ViewModelProvider(this).get(ResultViewModel.class);

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
            KeyboardUtils.hideSoftInput(v);



            if (getActivity() != null) {
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
