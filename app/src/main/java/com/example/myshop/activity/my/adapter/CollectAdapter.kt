package com.example.myshop.activity.my.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.databinding.ActivityCollectBinding
import com.example.myshop.db.Collect
import com.example.myshop.db.ShopCar
import com.example.myshop.tool.ToastTools

class CollectAdapter:RecyclerView.Adapter<CollectAdapter.ViewHolder>() {

    lateinit var binding: ActivityCollectBinding
    var dataList:ArrayList<Collect> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collect_adapter_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        holder.setModel(model)

        holder.deleteBtn.setOnClickListener {
            Collect.deleteModel(model.id)
            dataList.removeAt(position)
            notifyDataSetChanged()
            if (dataList.size == 0) {
                binding.noData.visibility = View.VISIBLE
            } else {
                binding.noData.visibility = View.GONE
            }
        }
        holder.shopCarBtn.setOnClickListener {
            val shopCar = ShopCar()
            shopCar.img = model.img
            shopCar.good_id = model.good_id
            shopCar.userId = model.userId
            shopCar.good_name = model.good_name
            shopCar.spec = model.spec
            shopCar.price = model.price
            ShopCar.saveModel(shopCar,holder.itemView.context)
            ToastTools.showMsg(holder.itemView.context,"添加成功")
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(item:View):RecyclerView.ViewHolder(item){
        val img:ImageView = item.findViewById(R.id.image)
        val goodName:TextView = item.findViewById(R.id.good_name)
        val goodSpec:TextView = item.findViewById(R.id.good_spec)
        val price:TextView = item.findViewById(R.id.good_price)
        val deleteBtn:View = item.findViewById(R.id.deleteBtn)
        val shopCarBtn:View = item.findViewById(R.id.shopCar)
        fun setModel(model:Collect){
            Glide.with(itemView)
                .load(model.img)
                .into(img)
            goodName.text = model.good_name
            goodSpec.text = model.spec
            price.text = "¥${model.price.toDouble()}"
        }
    }

}