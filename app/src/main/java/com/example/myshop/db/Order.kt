package com.example.myshop.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class Order(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var userId:Int = 0,
    var address:String = "",
    var price:String = "",
    var state:Int = 0,
    var expressNumber:String = "",
    var content:String = "",
    var goods:String = "",
    var createTime:String="",
    var payTime:String="",
    var expressTime:String="",
    var endTime:String=""
){
    @Ignore
    constructor():this(0)
}
