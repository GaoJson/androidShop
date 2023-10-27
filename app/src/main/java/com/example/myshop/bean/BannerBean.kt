package com.example.myshop.bean

class BannerBean {
    var url:String ?= null
    var viewType: Int = 0

    constructor(url:String?,viewType:Int){
        this.url = url
        this.viewType = viewType
    }

}