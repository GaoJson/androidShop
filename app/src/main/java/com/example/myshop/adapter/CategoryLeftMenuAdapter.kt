package com.example.myshop.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.http.model.HomeModel
import com.example.myshop.interfaces.ListCallback

class CategoryLeftMenuAdapter: RecyclerView.Adapter<CategoryLeftMenuAdapter.LeftMenuViewHolder>() {

     var selectIndex = 0
     var dataList:List<HomeModel.TgoodsCategoryVo> = ArrayList()

    private lateinit var listener: (position:Int) -> Unit

    fun reloadData(list:List<HomeModel.TgoodsCategoryVo>?){
        if (list != null) {
            dataList = list
        }
        println(list)
        notifyDataSetChanged()
    }

    fun setClickListener(listener: (position:Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeftMenuViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_left_menu_item,parent,false)


       return LeftMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeftMenuViewHolder, position: Int) {
        if (position == selectIndex){
            holder.itemView.setBackgroundResource(R.color.bck_color)
        } else{
            holder.itemView.setBackgroundResource(R.color.white)
        }
        holder.itemView.setOnClickListener {
            selectIndex = position
            notifyDataSetChanged()
            listener.invoke(position)
        }

        holder.titleTv.text = dataList[position].cname

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class LeftMenuViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
            val titleTv = itemView.findViewById<TextView>(R.id.titles)



    }

}