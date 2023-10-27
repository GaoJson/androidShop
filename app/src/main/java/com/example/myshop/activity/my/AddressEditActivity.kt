package com.example.myshop.activity.my

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.example.myshop.R
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityAddressEditBinding
import com.example.myshop.db.Address
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.tool.DisplayTool
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import com.google.gson.Gson
import com.gx.city_picker.CityPickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddressEditActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressEditBinding
    private var editFlag = false
    lateinit var editAddress: Address


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddressEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        setUI()
    }

    private fun setUI(){
        val data = intent.getStringExtra("data")
        if (data != null){
            editFlag = true
            val model = Gson().fromJson(data,Address::class.java)
            editAddress = model
            binding.userName.setText(model.userName,TextView.BufferType.EDITABLE)
            binding.phone.setText(model.phone,TextView.BufferType.EDITABLE)
            binding.addressOne.setText(model.address,TextView.BufferType.EDITABLE)
            binding.addressTwo.setText(model.addressDetail,TextView.BufferType.EDITABLE)
            val rightText = binding.navbar.insertRightText()
            rightText.text = "删除"
            rightText.setOnClickListener{
                GlobalScope.launch {
                    AppDatabaseManager.db.addressDao.deleteModel(model.id)
                    runOnUiThread {
                        finish()
                    }
                }
            }
        }

        binding.addressOne.setOnClickListener{
            CityPickerDialog.Build(this)
                .setAddressListener { level, province, city, area, town ->
                   binding.addressOne.setText("${province.provinceName} ${city.cityName} ${area.areaName}", TextView.BufferType.EDITABLE)

                }
                .show()
        }
        binding.submitBtn.setOnClickListener {

            var address = Address()
            if (editFlag) {
                address = editAddress
            }
            address.userId = UserInfo.user.id ?: 0
            address.address = binding.addressOne.text.toString()
            address.addressDetail = binding.addressTwo.text.toString()
            address.phone = binding.phone.text.toString()
            address.userName = binding.userName.text.toString()
            if (binding.checkbox.isChecked) {
                address.defaultFlag = 1
            } else {
                address.defaultFlag = 0
            }
            GlobalScope.launch {
                if (address.defaultFlag == 1) {
                  AppDatabaseManager.db.addressDao.resetDefault(UserInfo.user.id)
                }
                if (editFlag){
                    AppDatabaseManager.db.addressDao.updateModel(address)
                }else {
                    AppDatabaseManager.db.addressDao.save(address)
                }
                runOnUiThread {
                    ToastTools.show(this@AddressEditActivity,"${if (editFlag) "修改" else "添加"}地址成功")
                    finish()
                }
            }



        }



    }


}