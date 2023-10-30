package com.example.myshop.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CollectDao {

    @Insert
    fun saveModel(vararg models:Collect):List<Long>

    @Delete
    fun deleteModel(model:Collect):Int

    @Query("select count(id) from Collect where userId=:userId and good_id=:goodId")
    fun selectModel(goodId:Int,userId:Int):Int

    @Query("delete from Collect where userId=:userId and id=:goodId")
    fun deleteModel(goodId:Int,userId:Int):Int

    @Query("select * from `Collect` where userId=:userId")
    fun select(userId:Int):List<Collect>
}