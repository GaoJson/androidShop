package com.example.myshop.tool

import android.content.Context
import android.content.Intent
import com.example.myshop.activity.LoginActivity
import com.example.myshop.userinfn.UserInfo

class JudgeLogin {
    companion object{
        fun judge(context: Context,callback: (isLogin: Boolean) -> Unit){
            if (UserInfo.loginFlag){
                callback(true)
            } else {
                val intent = Intent(context,LoginActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}