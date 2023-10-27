package com.example.myshop.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "User")
data class User(
 @PrimaryKey(autoGenerate = true)
 var id:Int = 0,
 var account:String = "",
 var password:String = "",
 var nickname:String = "",
 var createTime: Long = 0,
 var headIcon:String = ""
) {
    @Ignore
    constructor():this(0)

    @Ignore
    constructor(account:String,password: String) : this(){
            createTime = System.currentTimeMillis()
            this.account = account
            this.password = password
    }
}
