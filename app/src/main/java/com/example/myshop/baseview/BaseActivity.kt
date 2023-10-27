package com.example.myshop.baseview

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

open class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wic = ViewCompat.getWindowInsetsController(window.decorView)
        if (wic != null){
            wic.isAppearanceLightStatusBars = true
        }

    }

}