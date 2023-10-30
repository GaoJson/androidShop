package com.example.myshop.db

import android.content.Context
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.http.model.GoodModel
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Entity(tableName = "ShopCar")
data class ShopCar(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var userId:Int=0,
    var good_id:Int=0,
    var count:Int=0,
    var good_name:String="",
    var spec:String="",
    var price:String="",
    var img:String="",
    var selectFlag:Int=0
){
    @Ignore
    constructor():this(0)

    @Ignore
    constructor(model: GoodModel) : this(){
        good_id = model.id
        count = 1
        good_name = model.goodsName
        spec = model.spec
        price = model.goodsPrice
        img = model.goodsImg
    }
    companion object {
        fun updateShopCar(model:ShopCar){
            GlobalScope.launch {
                AppDatabaseManager.db.shopCarDao.updateModel(model)
            }
        }
        fun updateShopCarCount(status:Int){
            GlobalScope.launch {
                AppDatabaseManager.db.shopCarDao.updateAllSelect(status, UserInfo.user.id ?: 0)
            }
        }

        fun saveModel(model:ShopCar,context: Context){
            GlobalScope.launch {
              val data =  AppDatabaseManager.db.shopCarDao.select(model.good_id,UserInfo.user.id)
                if (data != null) {
                    data.count += 1
                    updateShopCar(data)
                } else {
                    model.count = 1
                    AppDatabaseManager.db.shopCarDao.save(model)
                }
                ShopCarCountReceiver.updateShopCar(context)
            }
        }
    }
}
