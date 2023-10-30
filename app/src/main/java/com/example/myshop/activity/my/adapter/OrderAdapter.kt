package com.example.myshop.activity.my.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myshop.R
import com.example.myshop.activity.my.OrderDetailActivity
import com.example.myshop.baseview.DialogTool
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.Order
import com.example.myshop.db.ShopCar
import com.example.myshop.tool.EnumTool
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class OrderAdapter:RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    var dataList:List<Order> = arrayListOf()

    private var refresher: ((Int) -> Unit?)? = null

    fun refresher(refresher: (position:Int) -> Unit) {
        this.refresher = refresher
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        val goods = JSON.parseArray(model.goods,ShopCar::class.java)
        for (i in 0 until holder.imageList.childCount) {
            val image = holder.imageList.getChildAt(i) as ImageView
            if (goods.size > i){
                Glide.with(holder.itemView)
                    .load(goods[i].img)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                    .into(image)
            }else {
                image.setImageResource(0)
            }
        }
        holder.price.text = "¥${model.price}"
        holder.count.text = "共${goods.size}件"
        holder.state.text = EnumTool.orderStateName(model.state)
        setState(model,holder)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,OrderDetailActivity::class.java)
            intent.putExtra("orderId",model.id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
      return dataList.size
    }


    private fun setState(model:Order,holder: ViewHolder){
        (holder.menuItem1.parent as View).visibility = View.GONE
        (holder.menuItem2.parent as View).visibility = View.GONE
        (holder.menuItem3.parent as View).visibility = View.GONE
        (holder.menuItem4.parent as View).visibility = View.GONE

        if (model.state == 0){
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = "删除订单"
            holder.menuItem1.setOnClickListener {
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.deleteModel(model)
                    if (refresher != null) {
                        refresher!!.invoke(1)
                    }
                }
            }
        } else  if (model.state == 1){
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            (holder.menuItem2.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = "去支付"
            holder.menuItem2.text = "取消订单"

            holder.menuItem1.setOnClickListener {
                gotoPay(model,holder.itemView.context)
            }
            holder.menuItem2.setOnClickListener {
                GlobalScope.launch {
                    model.state = 0
                    AppDatabaseManager.db.orderDao.updateModel(model)
                    if (refresher != null) {
                        refresher!!.invoke(1)
                    }
                }
            }

        } else if (model.state == 2) {
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = "催发货"

            (holder.menuItem2.parent as View).visibility = View.VISIBLE
            holder.menuItem2.text = " 发货 "
            holder.menuItem2.setOnClickListener {
                gotoExpress(model, holder.itemView.context)
            }
        } else if (model.state == 3) {
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = "确认收货"
            holder.menuItem1.setOnClickListener {
                    GlobalScope.launch {
                        model.endTime = "${Date().time}"
                        model.state = 4
                        AppDatabaseManager.db.orderDao.updateModel(model)
                        if (refresher != null) {
                            refresher!!.invoke(1)
                        }
                    }
            }
            (holder.menuItem2.parent as View).visibility = View.VISIBLE
            holder.menuItem2.text = "查看物流"
            holder.menuItem2.setOnClickListener {
            }
        } else if(model.state == 4) {
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = " 评价 "
            holder.menuItem1.setOnClickListener {
                gotoComment(model, holder.itemView.context)
            }
            (holder.menuItem2.parent as View).visibility = View.VISIBLE
            holder.menuItem2.text = "删除订单"
            holder.menuItem2.setOnClickListener {
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.deleteModel(model)
                    if (refresher != null) {
                        refresher!!.invoke(1)
                    }
                }
            }
        } else if(model.state == 5) {
            (holder.menuItem1.parent as View).visibility = View.VISIBLE
            holder.menuItem1.text = "删除订单"
            holder.menuItem1.setOnClickListener {
                GlobalScope.launch {
                    AppDatabaseManager.db.orderDao.deleteModel(model)
                    if (refresher != null) {
                        refresher!!.invoke(1)
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
                if (refresher != null) {
                    refresher!!.invoke(1)
                }
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
                if (refresher != null) {
                    refresher!!.invoke(1)
                }
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
                if (refresher != null) {
                    refresher!!.invoke(1)
                }
            }
        },{
        })


    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
         val imageList:LinearLayout = itemView.findViewById(R.id.imageList)
         val price:TextView = itemView.findViewById(R.id.price)
         val count:TextView = itemView.findViewById(R.id.count)
         val state:TextView = itemView.findViewById(R.id.stateName)

        val menuItem1:TextView = itemView.findViewById(R.id.orderItem_1)
        val menuItem2:TextView = itemView.findViewById(R.id.orderItem_2)
        val menuItem3:TextView = itemView.findViewById(R.id.orderItem_3)
        val menuItem4:TextView = itemView.findViewById(R.id.orderItem_4)


    }


}