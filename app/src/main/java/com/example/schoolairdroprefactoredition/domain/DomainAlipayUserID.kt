package com.example.schoolairdroprefactoredition.domain

data class DomainAlipayUserID(
        val code: Int,
        val `data`: Data,
        val msg: String
) {
    data class Data(
            val alipay_id: String
    ) {
        override fun toString(): String {
            return "Data(alipay_id='$alipay_id')"
        }
    }
}

/**
{
"code": 200,
"msg": null,
"time": "1616079742",
"data": {
"alipay_id": 2088422649192711
}
}
 */