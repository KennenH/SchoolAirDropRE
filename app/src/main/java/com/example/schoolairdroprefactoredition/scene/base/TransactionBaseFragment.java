package com.example.schoolairdroprefactoredition.scene.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.schoolairdroprefactoredition.R;

/**
 * 重写切换动画，将所有子页面都以fragment打开
 */
public class TransactionBaseFragment extends Fragment {
    protected void transact(FragmentManager manager, @NonNull Fragment fragment, @Nullable String tag) {
        manager.beginTransaction()
                // 这四个参数的意思分别是
                // 1 新fragment进入动画
                // 2 旧fragment退出动画
                // 3 在新fragment回退时旧fragment的进入动画
                // 4 在新fragment回退时新fragment的退出动画
                .setCustomAnimations(R.anim.enter_x_fragment, R.anim.exit_x_fragment, R.anim.popenter_x_fragment, R.anim.popexit_x_fragment)
                .replace(((ViewGroup) getView().getParent()).getId(), fragment, tag)
                .addToBackStack(tag)
                .commit();
    }
}
