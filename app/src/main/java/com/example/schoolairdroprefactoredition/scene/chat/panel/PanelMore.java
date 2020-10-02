package com.example.schoolairdroprefactoredition.scene.chat.panel;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.adapterbean.MorePanelBean;

import java.util.ArrayList;
import java.util.List;

public class PanelMore {

    private static List<MorePanelBean> mData = new ArrayList<>();

    static {
        MorePanelBean album = new MorePanelBean(R.drawable.ic_album, MorePanelBean.ALBUM); // 相册
        MorePanelBean camera = new MorePanelBean(R.drawable.ic_camera, MorePanelBean.CAMERA); // 相机
        mData.add(album);
        mData.add(camera);
    }

    public static List<MorePanelBean> getPanelMore() {
        return mData;
    }
}
