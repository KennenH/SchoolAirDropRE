package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.ui.adapter.InquiryRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity
import java.util.*
import kotlin.collections.ArrayList

class InquiryRepository {

    companion object {
        private var INSTANCE: InquiryRepository? = null
        fun getInstance() = INSTANCE
                ?: InquiryRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取求购
     */
    fun getInquiry(page: Int, onResult: (List<BaseHomeNewsEntity>) -> Unit) {
        val data: ArrayList<BaseHomeNewsEntity> = ArrayList(12)
        val list = ArrayList<String>()
        list.add("https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-12-pro-max-gold-hero?wid=940&hei=1112&fmt=png-alpha&qlt=80&.v=1604021660000")
        list.add("https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg")
        list.add("https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/6bfe4659c6f3715ad5fa3ebde90ac123_259_194.jpg")
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606042040652&di=38cf0771633e1235ad6ce15ababccf93&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201410%2F22%2F20141022073033_nBdWH.jpeg")
        list.add("https://www.cleverfiles.com/howto/wp-content/uploads/2018/03/minion.jpg")
        list.add("http://www.personal.psu.edu/kbl5192/jpg.jpg")
        list.add("http://yamataka01.web.fc2.com/02/_1327.jpg")
        for (i in 0..11) {
            data[i] = BaseHomeNewsEntity()
            val random = Random()
            if (i % 4 == 0) {
                data[i].setType(InquiryRecyclerAdapter.TYPE_TWO)
                data[i].title = "#测试话题#"
            } else {
                data[i].setType(InquiryRecyclerAdapter.TYPE_ONE)
                data[i].title = "校园空投开始校园内测啦,速戳宇宙最全攻略!"
            }
            data[i].url = list[random.nextInt(list.size)]
        }
        onResult(data)
    }
}