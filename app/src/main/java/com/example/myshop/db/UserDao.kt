package com.example.myshop.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
     fun save(vararg users:User):List<Long>

    @Update
    fun updateModel(user: User):Int

    @Query("select count(id) from User where account=:account")
    fun existAccount(account:String):Long

    @Query("select * from User where account=:account and password=:password")
    fun confirmAccount(account: String,password:String):User




}