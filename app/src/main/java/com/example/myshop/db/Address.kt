package com.example.myshop.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "Address")
data class Address (
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var userId:Int = 0,
    var userName:String = "",
    var phone:String = "",
    var address:String = "",
    var addressDetail:String = "",
    var defaultFlag:Int = 0
        ){
    @Ignore
    constructor():this(0)

}