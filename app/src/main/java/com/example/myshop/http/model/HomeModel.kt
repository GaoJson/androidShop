package com.example.myshop.http.model


import com.alibaba.fastjson.annotation.JSONField

data class HomeModel(
    @JSONField(name = "hotGoodsVo")
    val hotGoodsVo: List<HotGoodsVo> = listOf(),
    @JSONField(name = "midBannerVos")
    val midBannerVos: List<MidBannerVo> = listOf(),
    @JSONField(name = "shopNoticeVos")
    val shopNoticeVos: List<ShopNoticeVo> = listOf(),
    @JSONField(name = "tgoodsCategoryVos")
    val tgoodsCategoryVos: List<TgoodsCategoryVo> = listOf(),
    @JSONField(name = "topBannerVos")
    val topBannerVos: List<TopBannerVo> = listOf()
) {
    data class HotGoodsVo(
        @JSONField(name = "contributeValue")
        val contributeValue: String = "", // 28.000000
        @JSONField(name = "createDate")
        val createDate: String = "", // 2023-07-18 16:44:42
        @JSONField(name = "goodsAttribute")
        val goodsAttribute: Any? = Any(), // null
        @JSONField(name = "goodsBanner")
        val goodsBanner: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/271c7d14-95f7-481c-8e3f-f157618f40f8.jpg,https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/d0ea6b95-e871-4735-88ff-33235a6b3614.jpg,https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/efb9b2f8-b827-4ab9-9bd2-f012b38cd870.jpg,https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/882bb363-cf5b-4bec-8cd3-0a61cfb024ba.jpg
        @JSONField(name = "goodsCid")
        val goodsCid: Int = 0, // 27
        @JSONField(name = "goodsCommentRating")
        val goodsCommentRating: Int = 0, // 0
        @JSONField(name = "goodsContent")
        val goodsContent: String = "", // <p><img src="https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/0c3f8a93-ed74-4bda-8fdb-35f8e322396b.jpg"></p>
        @JSONField(name = "goodsImg")
        val goodsImg: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/07/18/44603232-0094-4337-a1bd-0f8af52bbb8b.jpg
        @JSONField(name = "goodsName")
        val goodsName: String = "", // 百肤邦脚气净套装20g+30ml
        @JSONField(name = "goodsPrice")
        val goodsPrice: String = "", // 58.000000
        @JSONField(name = "goodsSn")
        val goodsSn: String = "", // 5761102011
        @JSONField(name = "id")
        val id: Int = 0, // 43
        @JSONField(name = "inventory")
        val inventory: Int = 0, // 100
        @JSONField(name = "isDelete")
        val isDelete: Int = 0, // 0
        @JSONField(name = "isGood")
        val isGood: Int = 0, // 1
        @JSONField(name = "isHot")
        val isHot: Int = 0, // 1
        @JSONField(name = "originalPrice")
        val originalPrice: String = "", // 68.000000
        @JSONField(name = "pageView")
        val pageView: Int = 0, // 142
        @JSONField(name = "remark")
        val remark: Any? = Any(), // null
        @JSONField(name = "saleCount")
        val saleCount: Int = 0, // 2
        @JSONField(name = "spec")
        val spec: String = "", // 20g+30ml
        @JSONField(name = "transportFee")
        val transportFee: String = "", // 0.000000
        @JSONField(name = "updateDate")
        val updateDate: Any? = Any() // null
    )

    data class MidBannerVo(
        @JSONField(name = "coverImg")
        val coverImg: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/08/26/6698c10f-1686-499d-95f2-fde3ce0637df.jpg
        @JSONField(name = "createDate")
        val createDate: String = "", // 2023-07-24 14:45:53
        @JSONField(name = "customValue")
        val customValue: String = "", // 509
        @JSONField(name = "id")
        val id: Int = 0, // 22
        @JSONField(name = "jumpType")
        val jumpType: Int = 0, // 1
        @JSONField(name = "name")
        val name: String = "", // 【周六福】珠事顺意玛瑙手串
        @JSONField(name = "remark")
        val remark: Any? = Any(), // null
        @JSONField(name = "sort")
        val sort: Int = 0, // 4
        @JSONField(name = "state")
        val state: Int = 0, // 1
        @JSONField(name = "type")
        val type: Int = 0, // 2
        @JSONField(name = "updateDate")
        val updateDate: Any? = Any(), // null
        @JSONField(name = "url")
        val url: Any? = Any() // null
    )

    data class ShopNoticeVo(
        @JSONField(name = "createDate")
        val createDate: String = "", // 2023-08-18 19:55:13
        @JSONField(name = "headImg")
        val headImg: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/08/18/06967814-6f3c-4d3f-8f1e-520dfcd7ff11.jpg
        @JSONField(name = "id")
        val id: Int = 0, // 2
        @JSONField(name = "isEnable")
        val isEnable: Int = 0, // 1
        @JSONField(name = "noticeContent")
        val noticeContent: String = "", // <p>尊敬的悦海国际用户：</p><p>悦海国际试运营至今，得到广大的用户信任与支持，经悦海国际团队研究决定，悦海国际商城于2023年8月20日正式上线运营，商城产品放开交易购买。感谢大家的信任与支持！</p><p>&nbsp;</p><p>悦海国际团队</p><p>2023年8月18日</p>
        @JSONField(name = "noticeTitle")
        val noticeTitle: String = "", // 悦海国际商城正式运营的公告
        @JSONField(name = "noticeType")
        val noticeType: Int = 0, // 0
        @JSONField(name = "pageView")
        val pageView: Any? = Any(), // null
        @JSONField(name = "updateDate")
        val updateDate: Any? = Any() // null
    )

    data class TgoodsCategoryVo(
        @JSONField(name = "cname")
        val cname: String = "", // 专属礼包
        @JSONField(name = "createDate")
        val createDate: String = "", // 2023-07-15 18:35:16
        @JSONField(name = "id")
        val id: Int = 0, // 18
        @JSONField(name = "imgUrl")
        val imgUrl: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/08/21/230a0da1-f3de-403f-a56d-fefbc8e1e741.jpg
        @JSONField(name = "isEnable")
        val isEnable: Int = 0, // 1
        @JSONField(name = "remark")
        val remark: Any? = Any(), // null
        @JSONField(name = "sort")
        val sort: Int = 0, // 0
        @JSONField(name = "updateDate")
        val updateDate: String = "" // 2023-08-21 10:58:04
    )

    data class TopBannerVo(
        @JSONField(name = "coverImg")
        val coverImg: String = "", // https://yuehaigj.oss-cn-shenzhen.aliyuncs.com/linglu/2023/08/26/ae87d8c3-424f-49df-ae90-a727129b4c45.jpg
        @JSONField(name = "createDate")
        val createDate: String = "", // 2023-07-15 18:37:59
        @JSONField(name = "customValue")
        val customValue: String = "", // 168
        @JSONField(name = "id")
        val id: Int = 0, // 17
        @JSONField(name = "jumpType")
        val jumpType: Int = 0, // 1
        @JSONField(name = "name")
        val name: String = "", // 【邻家饭香】 东北大米 长粒香米 5kg袋 黑土地种植 一年一季LJFX86-C
        @JSONField(name = "remark")
        val remark: Any? = Any(), // null
        @JSONField(name = "sort")
        val sort: Int = 0, // 0
        @JSONField(name = "state")
        val state: Int = 0, // 1
        @JSONField(name = "type")
        val type: Int = 0, // 1
        @JSONField(name = "updateDate")
        val updateDate: Any? = Any(), // null
        @JSONField(name = "url")
        val url: Any? = Any() // null
    )
}