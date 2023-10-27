package com.example.myshop.http.model


import com.alibaba.fastjson.annotation.JSONField

data class GoodModel(
    @JSONField(name = "contributeValue")
    val contributeValue: String = "",
    @JSONField(name = "createDate")
    val createDate: String = "",
    @JSONField(name = "goodsBanner")
    val goodsBanner: String = "",
    @JSONField(name = "goodsCid")
    val goodsCid: Int = 0,
    @JSONField(name = "goodsCommentRating")
    val goodsCommentRating: Int = 0,
    @JSONField(name = "goodsContent")
    val goodsContent: String = "",
    @JSONField(name = "goodsImg")
    val goodsImg: String = "",
    @JSONField(name = "goodsName")
    val goodsName: String = "",
    @JSONField(name = "goodsPrice")
    val goodsPrice: String = "",
    @JSONField(name = "goodsSn")
    val goodsSn: String = "",
    @JSONField(name = "id")
    val id: Int = 0,
    @JSONField(name = "inventory")
    val inventory: Int = 0,
    @JSONField(name = "isDelete")
    val isDelete: Int = 0,
    @JSONField(name = "isGood")
    val isGood: Int = 0,
    @JSONField(name = "isHot")
    val isHot: Int = 0,
    @JSONField(name = "originalPrice")
    val originalPrice: String = "",
    @JSONField(name = "pageView")
    val pageView: Int = 0,
    @JSONField(name = "saleCount")
    val saleCount: Int = 0,
    @JSONField(name = "spec")
    val spec: String = "",
    @JSONField(name = "transportFee")
    val transportFee: String = ""
)