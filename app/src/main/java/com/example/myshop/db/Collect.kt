package com.example.myshop.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myshop.http.model.GoodModel
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity(tableName = "Collect")
data class Collect (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var userId:Int=0,
    var good_id:Int=0,
    var count:Int=0,
    var good_name:String="",
    var spec:String="",
    var price:String="",
    var img:String="",

    ) {

    @Ignore
    constructor():this(0)

    companion object{
        fun saveModel(model: GoodModel){
            val collect = Collect()
            collect.userId = UserInfo.user.id
            collect.img = model.goodsImg
            collect.spec = model.spec
            collect.good_id = model.id
            collect.price = model.goodsPrice
            collect.good_name = model.goodsName
            GlobalScope.launch {
                AppDatabaseManager.db.collectDao.saveModel(collect)
            }
        }

        fun deleteModel(id: Int) {
            GlobalScope.launch {
                AppDatabaseManager.db.collectDao.deleteModel(id,UserInfo.user.id)
            }

        }


    }

}