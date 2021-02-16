package com.example.schoolairdroprefactoredition.scene.chat.panel;

import com.example.schoolairdroprefactoredition.R;

import java.util.ArrayList;
import java.util.List;

public class PanelMore {

    /**
     * 更多面板中的按钮列表
     */
    private static final List<MorePanelBean> mData = new ArrayList<>();

    static {
        MorePanelBean album = new MorePanelBean(R.drawable.ic_album, MorePanelBean.ALBUM); // 相册 按钮
        MorePanelBean camera = new MorePanelBean(R.drawable.ic_camera, MorePanelBean.CAMERA); // 相机 按钮

        // 添加所有的按钮
        mData.add(album);
        mData.add(camera);
    }

    public static List<MorePanelBean> getPanelMore() {
        return mData;
    }
}
