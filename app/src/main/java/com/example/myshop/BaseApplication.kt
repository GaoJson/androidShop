package com.example.myshop

import android.app.Application
import com.example.myshop.db.AppDatabaseManager

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabaseManager.saveApplication(this)

    }


}