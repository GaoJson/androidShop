package com.example.myshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.db.ShopCar

class PreShopGoodsAdapter:RecyclerView.Adapter<PreShopGoodsAdapter.ViewHolder>() {


    var dataList:ArrayList<ShopCar> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.preshop_shop_goods_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setModel(dataList[position])
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }




    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val image:ImageView = itemView.findViewById(R.id.image)
        val goodsName = itemView.findViewById<TextView>(R.id.goodsName)
        val goodsSpec = itemView.findViewById<TextView>(R.id.goodsSpec)
        val goodsPrice = itemView.findViewById<TextView>(R.id.goodsPrice)
        val goodsCount = itemView.findViewById<TextView>(R.id.goodsCount)

        fun setModel(model:ShopCar) {
            goodsName.text = model.good_name
            goodsSpec.text = model.spec
            goodsPrice.text = "¥${model.price}"
            goodsCount.text = "x${model.count}"
            Glide.with(itemView)
                .load(model.img)
                .into(image)

        }

    }


}