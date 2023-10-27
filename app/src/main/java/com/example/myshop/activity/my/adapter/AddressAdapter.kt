package com.example.myshop.activity.my.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.example.myshop.R
import com.example.myshop.activity.my.AddressEditActivity
import com.example.myshop.db.Address
import com.example.myshop.http.model.GoodModel

class AddressAdapter:RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var list:List<Address> = ArrayList()

    private lateinit var listener: (position:Int) -> Unit

    fun setClickListener(listener: (position:Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_list_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])

        holder.editBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context,AddressEditActivity::class.java)
            intent.putExtra("data",JSON.toJSONString(list[position]))
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            listener.invoke(position)
        }
    }

    override fun getItemCount(): Int {

        return list.size

    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val address:TextView = itemView.findViewById(R.id.address)
        val detailAddress:TextView = itemView.findViewById(R.id.detailAddress)
        val userName:TextView = itemView.findViewById(R.id.userName)
        val phone:TextView = itemView.findViewById(R.id.phone)
        val editBtn:ImageView = itemView.findViewById(R.id.editBtn)
        val defaultAddress:View = itemView.findViewById(R.id.defaultAddress)

        fun setData(addressModel: Address){
            address.text = addressModel.address
            detailAddress.text = addressModel.addressDetail
            userName.text = addressModel.userName
            if (addressModel.phone.length > 6) {
                phone.text = addressModel.phone.substring(0,3) +"****"+ addressModel.phone.substring(addressModel.phone.length-4,addressModel.phone.length)
            } else {
                phone.text = addressModel.phone
            }

            if (addressModel.defaultFlag == 1) {
                defaultAddress.visibility = View.VISIBLE
            } else {
                defaultAddress.visibility = View.GONE
            }
        }


    }

}