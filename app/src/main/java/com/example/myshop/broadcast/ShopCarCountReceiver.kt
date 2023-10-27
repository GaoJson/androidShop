package com.example.myshop.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ShopCarCountReceiver: BroadcastReceiver() {

    private lateinit var listener: (position:Int) -> Unit

    fun setListener(listener: (position:Int) -> Unit) {
        this.listener = listener
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        listener.invoke(0)
    }

    companion object{
       fun updateShopCar(context: Context){
           val intent = Intent("com.app.SHOPCARCOUNT")
           intent.setPackage(context.packageName)
           context.sendBroadcast(intent)
       }
    }

}