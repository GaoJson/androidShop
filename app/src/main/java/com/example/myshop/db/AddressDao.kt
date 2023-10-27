package com.example.myshop.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AddressDao {

    @Insert
    fun save(vararg models:Address):List<Long>

    @Query("select * from Address where userId=:userId")
    fun getAddressList(userId:Int):List<Address>

    @Query("delete from Address where id =:id")
    fun  deleteModel(id:Int):Int

    @Query("update Address set defaultFlag=0 where userId=:userId")
    fun resetDefault(userId:Int)

    @Update
    fun updateModel(model:Address):Int
}