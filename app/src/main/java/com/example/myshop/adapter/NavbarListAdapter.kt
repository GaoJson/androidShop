package com.example.myshop.adapter

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.strictmode.FragmentStrictMode
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myshop.R
import com.example.myshop.tool.DisplayTool
import java.time.format.TextStyle

class NavbarListAdapter:RecyclerView.Adapter<NavbarListAdapter.ViewHolder>() {


    private lateinit var listener: (position: Int) ->Unit
    fun setClickListener(listener: (position:Int) -> Unit) {
        this.listener = listener
    }

    var dataList:ArrayList<String> = arrayListOf()
    var selectIndex = 0

    fun loadData(list:ArrayList<String>){
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.navbar_list_adapter_item,parent,false)
        val layout = view.layoutParams
        layout.width = DisplayTool.getScreenWidth(parent.context)/5
        view.layoutParams = layout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectIndex == position) {
            holder.line.visibility = View.VISIBLE
            holder.title.paint.isFakeBoldText = true
        } else {
            holder.line.visibility = View.GONE
            holder.title.paint.isFakeBoldText = false
        }

        holder.itemView.setOnClickListener {
            selectIndex = position
            notifyDataSetChanged()
            listener.invoke(position)
        }
        holder.title.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val title:TextView = itemView.findViewById(R.id.title)
        val line:View = itemView.findViewById(R.id.line)



    }


}