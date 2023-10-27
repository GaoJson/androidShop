package com.example.myshop.tool

class EnumTool {

    companion object{
        fun orderStateName(state:Int):String {
            var value = ""
            when(state) {
                1-> value = "待支付"
                2-> value = "未发货"
                3-> value = "已发货"
                4-> value = "待评价"
                5-> value = "已完成"
            }
            return value
        }


    }


}