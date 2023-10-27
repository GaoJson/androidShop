package com.example.myshop.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.*
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityLogin2Binding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.User
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    lateinit var binding:ActivityLogin2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
    }


    private fun setUI() {
        binding.registerBtn.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.submit.setOnClickListener{
            val imm: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(SHOW_FORCED, 0)
            loginAction {
                if (it == null) {
                    ToastTools.show(this,"用户名或秘密错误")
                } else {
                    ToastTools.show(this,"登录成功")
                    UserInfo.loginFlag = true
                    UserInfo.saveUser(this,it)
                    finish()
                }
            }
        }
    }


    private fun loginAction(callback:(user: User?) -> Unit){
        GlobalScope.launch {
            val model = AppDatabaseManager.db.userDao.confirmAccount(binding.account.text.toString(),binding.password.text.toString())
            runOnUiThread{
                callback(model)
            }
        }


    }


}