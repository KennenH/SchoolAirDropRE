package com.example.schoolairdroprefactoredition.scene.goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestGoodsDetailBean;

import java.util.ArrayList;
import java.util.List;

public class GoodsViewModel extends ViewModel {

    private MutableLiveData<TestGoodsDetailBean> mGoodsData;

    public GoodsViewModel() {
        getOnlineGoodsData();
    }

    public LiveData<TestGoodsDetailBean> getGoodsData() {
        return mGoodsData;
    }

    private void getOnlineGoodsData() {
        mGoodsData = new MutableLiveData<>();

        //////////////////////test/////////////////////////
        TestGoodsDetailBean data = new TestGoodsDetailBean();
        List<String> list = new ArrayList<>();
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2171494511,24063625&fm=26&gp=0.jpg");
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3902106409,3042380057&fm=26&gp=0.jpg");
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2300009576,917412282&fm=26&gp=0.jpg");
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3131779180,674888813&fm=26&gp=0.jpg");
        data.setImageData(list);
        data.setGoodsName("杂七杂八");
        data.setTags(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
                add(2);
            }
        });
        data.setAvatar("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2290595455,3975937937&fm=26&gp=0.jpg");
        data.setComments(12332);
        data.setLikes(5463);
        data.setWatches(345889);
        data.setPrice(15923.5f);
        data.setUserName("校园空投官方");
        data.setDescription("哈哈哈哈哈哈哈哈哈哈哈哈奥奥奥哈哈哈哈哈哈或红红火火恍恍惚惚");
        //////////////////////test/////////////////////////
        mGoodsData.setValue(data);
    }

}
