package com.example.myshop.tabbar

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.myshop.R
import com.example.myshop.activity.order.PreShopOrderActivity
import com.example.myshop.adapter.ShopCarAdapter
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.databinding.FragmentHomeBinding
import com.example.myshop.databinding.FragmentShopCatBinding
import com.example.myshop.db.Address
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.ShopCar
import com.example.myshop.db.User
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class ShopCatFragment : Fragment() {

    private var _bind:FragmentShopCatBinding ?= null
    private val binding get() = _bind!!

    private val adapter = ShopCarAdapter()

    private var editStatusFlag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_bind == null) {
            _bind = FragmentShopCatBinding.inflate(inflater, container, false)
            setUI()
        }
        refresh()
        return binding.root
    }

    private fun setUI() {
        val rightText = binding.navbar.insertRightText()
        rightText.text = "编辑"
        rightText.setOnClickListener{
           editStatusFlag = !editStatusFlag
            if (editStatusFlag) {
                rightText.text = "完成"
                binding.deleteBtn.visibility = View.VISIBLE
                binding.buyBtn.visibility = View.GONE
            } else {
                binding.buyBtn.visibility = View.VISIBLE
                binding.deleteBtn.visibility = View.GONE
                rightText.text = "编辑"
            }
        }

        binding.listView.layoutManager = LinearLayoutManager(context)
        binding.listView.adapter = adapter
        adapter.setClickListener {
            countGoodPrice()

        }

        binding.selectBox.setOnCheckedChangeListener { compoundButton, b ->
            var status = binding.selectBox.isChecked
            var selectFlag = if (status) {
                1
            } else {
                0
            }
            adapter.dataList.forEach {
                it.selectFlag = selectFlag
            }
            ShopCar.updateShopCarCount(selectFlag)
            adapter.notifyDataSetChanged()
            countGoodPrice()
        }


        binding.deleteBtn.setOnClickListener{
            GlobalScope.launch {
                adapter.dataList.forEach {
                    if (it.selectFlag == 1){
                        AppDatabaseManager.db.shopCarDao.deleteModel(it.id)
                    }
                }
                refresh()
            }
        }




        val startAddressActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                    refresh()
            }
        }

        binding.gotoPay.setOnClickListener {
            val selectArray:ArrayList<ShopCar> = arrayListOf()
            adapter.dataList.forEach {
                if (it.selectFlag == 1) {
                    selectArray.add(it)
                }
            }
            if (selectArray.isNotEmpty()){
                val intent = Intent(activity,PreShopOrderActivity::class.java)
                intent.putExtra("data",JSON.toJSONString(selectArray))
                startAddressActivity.launch(intent)
            } else {
                ToastTools.show(activity,"请选择商品")
            }
        }

    }

    private fun refresh() {
        if (!UserInfo.loginFlag){
            adapter.dataList = arrayListOf()
            adapter.notifyDataSetChanged()
            countGoodPrice()
            return
        }

        GlobalScope.launch {
            val data =  AppDatabaseManager.db.shopCarDao.getShopCarList(UserInfo.user!!.id)
            activity!!.runOnUiThread {
                adapter.dataList = data
                adapter.notifyDataSetChanged()
                if (data.isNotEmpty()) {
                    binding.noData.visibility = View.GONE
                } else{
                    binding.noData.visibility = View.VISIBLE
                }
                countGoodPrice()
            }
        }
    }

    private fun countGoodPrice() {
        var price = 0.00
        var count = 0
        adapter.dataList.forEach {
            if (it.selectFlag == 1){
                price += it.price.toDouble()*it.count
                count += it.count
            }
        }
        val format = DecimalFormat("0.00")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        val prices = format.format(price)
        binding.priceLeft.text = prices.split(".")[0]+"."
        binding.priceRight.text = prices.split(".")[1]
        binding.gotoPay.text = "去结算($count)"
        ShopCarCountReceiver.updateShopCar(activity!!)

    }



}