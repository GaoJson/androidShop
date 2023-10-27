package com.example.myshop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ShopCarDao {

    @Insert
    fun save(vararg models:ShopCar):List<Long>

    @Update
    fun updateModel(model: ShopCar):Int

    @Query("select * from ShopCar where userId=:userId")
    fun getShopCarList(userId:Int):List<ShopCar>

    @Query("update ShopCar set selectFlag=:status where userId=:userId")
    fun  updateAllSelect(status:Int,userId:Int):Int

    @Query("delete from ShopCar where id=:id")
    fun  deleteModel(id:Int):Int



}