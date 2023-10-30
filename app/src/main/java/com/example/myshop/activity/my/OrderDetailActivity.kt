package com.example.myshop.activity.my

import DateExt
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.example.myshop.R
import com.example.myshop.activity.my.adapter.OrderAdapter
import com.example.myshop.adapter.PreShopGoodsAdapter
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.baseview.DialogTool
import com.example.myshop.databinding.ActivityOrderDetailBinding
import com.example.myshop.db.Address
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.Order
import com.example.myshop.db.ShopCar
import com.example.myshop.tool.DisplayTool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.Date

class OrderDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderDetailBinding

    val adapter = PreShopGoodsAdapter()
    var orderModel = Order()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        setUI()
        loadData()
    }

    fun loadData() {
        val orderId = intent.getIntExtra("orderId",0)
        GlobalScope.launch {
           orderModel = AppDatabaseManager.db.orderDao.selectOrder(orderId)
            runOnUiThread{
                adapter.dataList = JSON.parseArray(orderModel.goods,ShopCar::class.java) as ArrayList<ShopCar>
                adapter.notifyDataSetChanged()
                updateData()
            }
        }


    }

    private fun setUI() {
        binding.goodsList.layoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.goodsList.adapter = adapter
    }

    fun updateData(){
        binding.list.removeAllViews()

        addListItem("商品总价","¥${orderModel.price}",Color.BLACK)
        addListItem("运费","¥0.0",Color.BLACK)
        addListItem("支付优惠","¥0.0",Color.BLACK)
        addListItem("实付款","¥${orderModel.price}",Color.RED)

        val addressModel = JSON.parseObject(orderModel.address,Address::class.java)
        val address = addressModel.userName + " " + addressModel.phone + " " + addressModel.address + addressModel.addressDetail
        addListItem("收货信息","$address",Color.GRAY)
        addListItem("创建时间",getTime(orderModel.createTime),Color.GRAY)
        addListItem("支付时间",getTime(orderModel.payTime),Color.GRAY)
        addListItem("发货时间",getTime(orderModel.expressTime),Color.GRAY)
        addListItem("完成时间",getTime(orderModel.endTime),Color.GRAY)
        setState(orderModel)
    }

    fun getTime(time:String):String {
        if (time.isEmpty()){
            return ""
        }
        var date = Date()
        date.time = time.toLong()
        return DateExt.toDateTimeString(date)
    }

    fun addListItem(title:String, value:String, color: Int) {
        val view = LayoutInflater.from(this).inflate(R.layout.order_detail_list_layout,binding.list,false)
        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<TextView>(R.id.value).text = value
        view.findViewById<TextView>(R.id.value).setTextColor(color)
        if (title=="实付款"){
            view.findViewById<TextView>(R.id.value).textSize = 20.toFloat()
        }
        binding.list.addView(view)

    }

    private fun setState(model:Order){
        val menuItem1 = binding.orderItem1
        val menuItem2 = binding.orderItem2
        val menuItem3 = binding.orderItem3
        val menuItem4 = binding.orderItem4
        (menuItem1.parent as View).visibility = View.GONE
        (menuItem2.parent as View).visibility = View.GONE
        (menuItem3.parent as View).visibility = View.GONE
        (menuItem4.parent as View).visibility = View.GONE
        if (model.state == 1){
            (menuItem1.parent as View).visibility = View.VISIBLE
            (menuItem2.parent as View).visibility = View.VISIBLE
             menuItem1.text = "去支付"
             menuItem2.text = "取消订单"

             menuItem1.setOnClickListener {
                gotoPay(model, this)
            }

        } else if (model.state == 2) {
            ( menuItem1.parent as View).visibility = View.VISIBLE
             menuItem1.text = "催发货"

            ( menuItem2.parent as View).visibility = View.VISIBLE
             menuItem2.text = " 发货 "
             menuItem2.setOnClickListener {
                gotoExpress(model,  this)
            }
        } else if (model.state == 3) {
            ( menuItem1.parent as View).visibility = View.VISIBLE
             menuItem1.text = "确认收货"
             menuItem1.setOnClickListener {
                GlobalScope.launch {
                    model.endTime = "${Date().time}"
                    model.state = 4
                    AppDatabaseManager.db.orderDao.updateModel(model)

                }
            }
            ( menuItem2.parent as View).visibility = View.VISIBLE
             menuItem2.text = "查看物流"
             menuItem2.setOnClickListener {
            }
        } else if(model.state == 4) {
            ( menuItem1.parent as View).visibility = View.VISIBLE
             menuItem1.text = " 评价 "
             menuItem1.setOnClickListener {
                gotoComment(model,  this)
            }
            ( menuItem2.parent as View).visibility = View.VISIBLE
             menuItem2.text = "删除订单"
             menuItem2.setOnClickListener {
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.deleteModel(model)
                    runOnUiThread {
                        finish()
                    }
                }
            }
        } else if(model.state == 5) {
            ( menuItem1.parent as View).visibility = View.VISIBLE
             menuItem1.text = "删除订单"
             menuItem1.setOnClickListener {
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.deleteModel(model)
                    runOnUiThread {
                        finish()
                    }
                }
            }
        }
    }

    fun gotoPay(model:Order,context: Context){
        DialogTool.showInputDialog(context,"订单支付","输入交易密码",{
            model.payTime = "${Date().time}"
            model.state = 2
            GlobalScope.launch {
                AppDatabaseManager.db.orderDao.updateModel(model)
                loadData()
            }
        },{
        })
    }

    fun gotoExpress(model:Order,context: Context) {
        DialogTool.showInputDialog(context,"订单发货","输入快递号",{
            model.expressTime = "${Date().time}"
            model.state = 3
            model.expressNumber = it
            GlobalScope.launch {
                AppDatabaseManager.db.orderDao.updateModel(model)
                loadData()
            }
        },{
        })
    }

    fun gotoComment(model:Order,context: Context){
        DialogTool.showCommentDialog(context,"订单评论","输入商品评论",{
            model.content = it
            model.state = 5
            GlobalScope.launch {
                AppDatabaseManager.db.orderDao.updateModel(model)
                loadData()
            }
        },{
        })


    }

}