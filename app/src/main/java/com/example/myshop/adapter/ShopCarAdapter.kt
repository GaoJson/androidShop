package com.example.myshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.db.ShopCar
import com.example.myshop.http.model.GoodModel

class ShopCarAdapter : RecyclerView.Adapter<ShopCarAdapter.ShopCarViewHolder>()  {

    var dataList:List<ShopCar> = ArrayList()

    private lateinit var listener: (position:Int) -> Unit

    fun setClickListener(listener: (position:Int) -> Unit) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCarViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopcat_item_layout,parent,false)
        return ShopCarViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopCarViewHolder, position: Int) {
        holder.setModel(dataList[position])

        holder.addBtn.setOnClickListener{
            dataList[position].count += 1
            ShopCar.updateShopCar(dataList[position])
            notifyDataSetChanged()
            listener.invoke(position)
        }
        holder.reduceBtn.setOnClickListener {
            dataList[position].count -= 1
            if (dataList[position].count<=0){
                dataList[position].count = 1
            }
            ShopCar.updateShopCar(dataList[position])
            notifyDataSetChanged()
            listener.invoke(position)
        }
        holder.selectBtn.setOnClickListener{
            if (dataList[position].selectFlag == 0){
                dataList[position].selectFlag = 1
            } else{
                dataList[position].selectFlag = 0
            }
            ShopCar.updateShopCar(dataList[position])
            notifyDataSetChanged()
            listener.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }



    class ShopCarViewHolder(iteView:View):RecyclerView.ViewHolder(iteView){
        private val goodName:TextView = iteView.findViewById(R.id.good_name)
        private val goodSpec:TextView = iteView.findViewById(R.id.good_spec)
        private val img:ImageView = iteView.findViewById(R.id.image)
        private val goodPrice:TextView = iteView.findViewById(R.id.good_price)
        val count:EditText = iteView.findViewById(R.id.count)
        val addBtn:TextView = iteView.findViewById(R.id.addBtn)
        val reduceBtn:TextView = iteView.findViewById(R.id.reduceBtn)
        val selectBtn:CheckBox = iteView.findViewById(R.id.selectStatus)


        fun setModel(model:ShopCar){
            goodName.text = model.good_name
            goodSpec.text = model.spec
            goodPrice.text = "¥${model.price.toDouble()}"
            Glide.with(itemView)
                .load(model.img)
                .into(img)
            count.setText("${model.count}", TextView.BufferType.EDITABLE)
            selectBtn.isChecked = model.selectFlag == 1
        }


    }
}