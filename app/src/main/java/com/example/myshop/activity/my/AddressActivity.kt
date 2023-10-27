package com.example.myshop.activity.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.myshop.R
import com.example.myshop.activity.my.adapter.AddressAdapter
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityAddressBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.tool.DisplayTool
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddressActivity : BaseActivity() {

    private lateinit var binding:ActivityAddressBinding

    var adaper = AddressAdapter()

    var selectFlag = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        if (intent.getBooleanExtra("selectFlag",false)){
            selectFlag = true
        }

        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView.adapter = adaper
        adaper.setClickListener {
           if (selectFlag) {
               val address = adaper.list[it]
               intent.putExtra("data",JSON.toJSONString(address))
               setResult(RESULT_OK,intent)
               finish()
           }
        }
        binding.addAddress.setOnClickListener {
            val intent = Intent(this,AddressEditActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch {
            var data = AppDatabaseManager.db.addressDao.getAddressList(UserInfo.user!!.id)
            runOnUiThread {
                adaper.list = data
                adaper.notifyDataSetChanged()
                if (data.isNotEmpty()){
                    binding.noData.visibility = View.GONE
                } else {
                    binding.noData.visibility = View.VISIBLE
                }
            }
        }

    }



}