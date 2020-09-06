package com.example.schoolairdroprefactoredition.scene.chat.panel;

import com.example.schoolairdroprefactoredition.R;
import com.example.schoolairdroprefactoredition.model.adapterbean.MorePanelBean;

import java.util.ArrayList;
import java.util.List;

public class PanelMore {

    private static List<MorePanelBean> mData = new ArrayList<>();

    static {
        MorePanelBean quote = new MorePanelBean(R.drawable.ic_quote, MorePanelBean.QUOTE); // 报价
        MorePanelBean album = new MorePanelBean(R.drawable.ic_album, MorePanelBean.ALBUM); // 相册
        MorePanelBean camera = new MorePanelBean(R.drawable.ic_camera, MorePanelBean.CAMERA); // 相机
        MorePanelBean position = new MorePanelBean(R.drawable.ic_map, MorePanelBean.POSITION); // 约定地点
        mData.add(quote);
        mData.add(album);
        mData.add(camera);
        mData.add(position);
    }

    public static List<MorePanelBean> getPanelMore() {
        return mData;
    }
}
