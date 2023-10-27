package com.example.myshop.db

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseManager {
    private const val DB_NAME = "appData.db"
    private val MIGRATIONS = arrayOf(Migration1)
    private lateinit var application: Application
    val db:AppDataBase by lazy {
        Room.databaseBuilder(application.applicationContext,AppDataBase::class.java, DB_NAME)
            .addCallback(CreatedCallBack)
            .addMigrations(*MIGRATIONS)
            .build()
    }

    fun saveApplication(application: Application){
        AppDatabaseManager.application = application
    }

    private object CreatedCallBack : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //在新装app时会调用，调用时机为数据库build()之后，数据库升级时不调用此函数
            MIGRATIONS.map {
                it.migrate(db)
            }
        }
    }

    private object Migration1 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 数据库的升级语句
            // database.execSQL("")
        }
    }

    private object Migration3_4 :Migration(3,4){
        override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE Address(id INTEGER PRIMARY KEY NOT NULL,userId INTEGER NOT NULL,defaultFlag INTEGER NOT NULL,addressDetail TEXT NOT NULL,address TEXT NOT NULL,phone TEXT NOT NULL,userName TEXT NOT NULL)")


        }


    }
}