package com.example.myshop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {

    @Insert
    fun saveModel(vararg models:Order):List<Long>

    @Update
    fun updateModel(models:Order):Int

    @Query("select * from `Order` where userId=:userId")
    fun select(userId:Int):List<Order>

    @Query("select * from `Order` where userId=:userId and state=:state")
    fun select(userId:Int,state: Int):List<Order>

    @Query("select * from `Order` where userId=:userId and (state=5 or state=4)")
    fun selectDone(userId:Int):List<Order>

    @Query("select count(id) from `Order` where userId=:userId and state=:state")
    fun selectCount(userId: Int,state:Int):Int

    @Query("select * from `Order` where id=:orderId")
    fun selectOrder(orderId: Int):Order

    @Delete
    fun deleteModel(models:Order):Int



}