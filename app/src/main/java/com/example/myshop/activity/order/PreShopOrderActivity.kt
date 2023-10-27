package com.example.myshop.activity.order

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.example.myshop.activity.my.AddressActivity
import com.example.myshop.adapter.PreShopGoodsAdapter

import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityPreShopOrderBinding
import com.example.myshop.db.*
import com.example.myshop.tool.DisplayTool
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask


class PreShopOrderActivity : BaseActivity() {
    private lateinit var binding:ActivityPreShopOrderBinding

    private var shopList:ArrayList<ShopCar> = arrayListOf()

    private var addressModel = Address()

    private var goodsAdapter = PreShopGoodsAdapter()

    private var allPrice = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreShopOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        setUI()



    }



    fun setUI() {

        binding.goodsList.layoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.goodsList.adapter = goodsAdapter
        val startAddressActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
               val data = it.data?.getStringExtra("data")
                addressModel = Gson().fromJson(data,Address::class.java)

                updateAddress()
            }
        }
        binding.addressView.setOnClickListener {
            val intent = Intent(this@PreShopOrderActivity,AddressActivity::class.java)
            intent.putExtra("selectFlag",true)
            startAddressActivity.launch(intent)
        }
        binding.submitBtn.setOnClickListener{
            submitAction()
        }
        getAddressList()
        getShopList()
    }

    fun getAddressList(){
        GlobalScope.launch {
           val list:List<Address> = AppDatabaseManager.db.addressDao.getAddressList(UserInfo.user.id)
            for (i in list.indices){
                if (list[i].defaultFlag == 1) {
                        addressModel = list[i];
                    break
                }
            }
            runOnUiThread {
                binding.noAddress.visibility = View.GONE
                if (addressModel.phone.isEmpty()) {
                    if (list.isEmpty()) {
                        binding.noAddress.visibility = View.VISIBLE
                    } else {
                        addressModel = list[0]
                    }
                }
                updateAddress()
            }
        }
    }

    fun updateAddress() {
        if (addressModel.defaultFlag == 1) {
            binding.defaultAddress.visibility = View.VISIBLE
        } else {
            binding.defaultAddress.visibility = View.GONE
        }
        binding.address.text = addressModel.address
        binding.detailAddress.text = addressModel.addressDetail
        binding.userName.text = addressModel.userName
        binding.phone.text = addressModel.phone
    }


    fun getShopList(){
        val shopData = intent.getStringExtra("data");
        val array = JSONArray.parseArray(shopData)
        var price = 0.0
        array.forEach {
            val obj = it as JSONObject
            val model:ShopCar = Gson().fromJson(obj.toJSONString(),ShopCar::class.java)
            price += model.price.toDouble() * model.count
            shopList.add(model)
        }
        goodsAdapter.dataList = shopList
        goodsAdapter.notifyDataSetChanged()

        val format = DecimalFormat("#.##")
        format.roundingMode = RoundingMode.FLOOR
        val prices = format.format(price)
        allPrice = price
        binding.goodsPrice.text = "¥$prices"
        binding.allPrice.text = "¥$prices"
        binding.submitPrice.text = "¥$prices"
    }

    fun submitAction() {
        if (addressModel.phone.isEmpty()){
            ToastTools.show(this,"收获地址为空")
            return
        }

        val editText = EditText(this)
        editText.hint = "请输入交易密码"
        editText.background = null

        val order = Order()
        order.address = JSON.toJSONString(addressModel)
        order.goods = JSON.toJSONString(shopList)
        order.createTime = "${Date().time}"
        order.userId = UserInfo.user.id
        order.state = 1
        order.price = "$allPrice"

        var orderId = 0
        GlobalScope.launch {
          val data =  AppDatabaseManager.db.orderDao.saveModel(order)
          order.id = data[0].toInt()
        }


        AlertDialog.Builder(this)
            .setTitle("支付订单")
            .setView(editText)
            .setPositiveButton("确定",DialogInterface.OnClickListener { dialogInterface, i ->
                order.payTime = "${Date().time}"
                order.state = 2
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.updateModel(order)
                    cleanShopCar()
                }
            })
            .setNegativeButton("取消",{dialogInterface, i ->
                cleanShopCar()
            }
            )
            .setOnDismissListener {
                cleanShopCar()
            }
            .show()
    }

    fun cleanShopCar(){
        GlobalScope.launch {
            shopList.forEach {
                AppDatabaseManager.db.shopCarDao.deleteModel(it.id)
            }
            runOnUiThread {
                ToastTools.show(this@PreShopOrderActivity,"支付成功")
                Timer("delay").schedule(object :TimerTask(){
                    override fun run() {
                        setResult(RESULT_OK,intent)
                        finish()
                    }
                },1000)

            }

        }

    }

}