package com.example.myshop.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1,
    exportSchema = false,
    entities = [User::class,ShopCar::class,Address::class,Order::class,Collect::class])

abstract class AppDataBase :RoomDatabase() {
    val userDao:UserDao by lazy { createUserDao() }
    abstract fun  createUserDao():UserDao

    val shopCarDao:ShopCarDao by lazy { createShopCarDao() }
    abstract fun  createShopCarDao():ShopCarDao

    val addressDao:AddressDao by lazy { createAddressDao() }
    abstract fun createAddressDao():AddressDao

    val orderDao:OrderDao by lazy { createOrderDao() }
    abstract fun createOrderDao():OrderDao

    val collectDao:CollectDao by lazy { createCollectDao() }
    abstract fun createCollectDao():CollectDao
}