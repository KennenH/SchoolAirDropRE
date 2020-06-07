package com.example.schoolairdroprefactoredition.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.ui.components.PageItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private String lastName;

    private TextView mName1;
    private TextView mName2;
    private int lastStack = 0;

    private boolean flag = true;// 用于标识需要进行变换的标题
    private boolean actionLock = true;// false 时为锁定,true 为释放

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        String userInfoName = getResources().getString(R.string.userInfo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mName1 = findViewById(R.id.old);
        mName2 = findViewById(R.id.newcomer);
        mName1.setText(userInfoName);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new UserFragment(), userInfoName)
                .commit();


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (actionLock) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (actionLock && id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                finish();
            else
                getSupportFragmentManager().popBackStack();
            return true;
        } else
            return false;
    }

    @Override
    public void onBackStackChanged() {
        actionLock = false;

        TextView alpha = flag ? mName1 : mName2;
        TextView translation = mName2 == alpha ? mName1 : mName2;

        int nowStack = getSupportFragmentManager().getBackStackEntryCount();

        String now = getSupportFragmentManager().findFragmentById(R.id.container).getTag();

        if (lastStack < nowStack) { // 开启新的fragment
            translation.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.enter_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);
        } else { // 关闭fragment
            flag = !flag;
            translation.setText(lastName);
            alpha.setText(now);

            Animation animA = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
            animA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    alpha.setVisibility(View.VISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animT = AnimationUtils.loadAnimation(this, R.anim.exit_text);
            animT.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    translation.setVisibility(View.INVISIBLE);
                    actionLock = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alpha.startAnimation(animA);
            translation.startAnimation(animT);

        }

        flag = !flag;// 交换职责
        lastStack = nowStack;
        lastName = now;
    }

    // 用户信息主页面
    public static class UserFragment extends Fragment implements View.OnClickListener {
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
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user, container, false);

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
                        // 这四个参数的意思分别是
                        // 1 新fragment进入动画
                        // 2 旧fragment退出动画
                        // 3 在新fragment回退时旧fragment的进入动画
                        // 4 在新fragment回退时新fragment的退出动画
                        .setCustomAnimations(R.anim.enter_y_fragment, R.anim.exit_y_fragment, R.anim.popenter_y_fragment, R.anim.popexit_y_fragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), new UserNameFragment(), userName)
                        .addToBackStack(userName)
                        .commit();
            } else if (id == R.id.user_sex) {
                showSexDialog();
            }
        }

        private void showSexDialog() {
            if (dialog == null) {
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

    public static class UserNameFragment extends Fragment implements View.OnClickListener, TextWatcher {

        private String name = "空小投";

        private ImageView mClear;
        private EditText mEditor;
        private TextView mDone;

        public UserNameFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_name, container, false);

            mClear = root.findViewById(R.id.user_name_clear);
            mEditor = root.findViewById(R.id.user_name_editor);
            mDone = root.findViewById(R.id.user_name_done);

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
            } else if (id == R.id.user_name_done) {
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

    public static class UserAvatarFragment extends Fragment implements View.OnLongClickListener {

        private BottomSheetDialog dialog;
        private ImageView mAvatar;

        public UserAvatarFragment() {
        }

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

}
