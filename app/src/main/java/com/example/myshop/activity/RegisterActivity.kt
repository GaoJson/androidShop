package com.example.myshop.activity

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.databinding.ActivityRegisterBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.User
import com.example.myshop.tool.ToastTools
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()

    }

    private fun setUI (){
        binding.submit.setOnClickListener{submitAction()}


    }

    private fun submitAction() {
        val text = binding.account.text
        if (text.isEmpty()){
            Toast.makeText(this,"请输入用户名",1).show()

            return
        }
        if (binding.password.text.isEmpty()){
            Toast.makeText(this,"请输入密码",1).show()
            return
        }
        if (binding.passwordConform.text.isEmpty()){
            Toast.makeText(this,"请输入密码",1).show()
            return
        }
        val that = this
        GlobalScope.launch {
            val existFlag = AppDatabaseManager.db.userDao.existAccount(text.toString())
            if (existFlag.toInt() == 1) {
                ToastTools.show(that,"用户已存在！")

            } else{
                val user = User(binding.account.text.toString(),binding.passwordConform.text.toString())
                AppDatabaseManager.db.userDao.save(user)
                runOnUiThread {
                    Toast.makeText(that, "注册成功！", 1).show()
                    finish()
                }
            }
        }


    }

}