package com.example.myshop.userinfn

import android.content.Context
import android.content.SharedPreferences
import com.alibaba.fastjson.JSON
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.User
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object UserInfo {
    var token:String = ""
    var loginFlag:Boolean = false
    var user:User = User()

    fun saveUser(context: Context,user: User){
        this.user = user
        loginFlag = true
        putString("user",JSON.toJSONString(user),context)
        putString("loginFlag","1",context)
    }

    fun exitUser(context: Context){
        this.user = User()
        loginFlag = false
        putString("loginFlag","0",context)
        putString("user",null,context)
    }

    fun getUser(context: Context){
       val str = getString("user",context)
        if (str != null){
            user = Gson().fromJson(str,User::class.java)
        }
       val flag = getString("loginFlag",context)?.toInt()
        loginFlag = flag == 1
    }

    private var sps:SharedPreferences?=null

    private fun getSps(context: Context):SharedPreferences{
        if(sps==null){
            sps=context.getSharedPreferences("default",Context.MODE_PRIVATE)
        }
        return sps!!
    }

    private fun putString(key:String, value:String?, context:Context){
        if(!value.isNullOrBlank()){
            var editor:SharedPreferences.Editor=getSps(context).edit()
            editor.putString(key,value)
            editor.commit()
        }
    }
    private fun getString(key:String, context:Context):String?{
        if(!key.isNullOrBlank()){
            var sps:SharedPreferences=getSps(context)
            return sps.getString(key,null)
        }
        return null
    }


    fun updateUser(){
        GlobalScope.launch {
          val data =  AppDatabaseManager.db.userDao.updateModel(user!!)
            println(data)
        }


    }

}