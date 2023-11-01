package com.example.myshop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.activity.LoginActivity
import com.example.myshop.activity.my.AddressActivity
import com.example.myshop.activity.my.CollectActivity
import com.example.myshop.http.model.HomeModel
import com.example.myshop.tool.DisplayTool
import com.example.myshop.tool.JudgeLogin
import kotlin.math.floor

class MySortAdapter: RecyclerView.Adapter<MySortAdapter.ViewPageHolder>() {

    var dataList: ArrayList<ArrayList<HomeModel.TgoodsCategoryVo>> = ArrayList()

    var totalView = 0

    fun setData(data: List<HomeModel.TgoodsCategoryVo>?) {
        if (data != null) {
            var total = if (data.size % 8 == 0) {
                floor(data.size / 8.0).toInt()
            } else {
                floor(data.size / 8.0).toInt() + 1
            }
            totalView = total
            dataList.clear()
            for (i in 0 until total) {
                val list: ArrayList<HomeModel.TgoodsCategoryVo> = ArrayList()
                var begin = i * 8
                var end = (i + 1) * 8;
                if (end > data.size) {
                    end = data.size;
                }
                for (j in begin until end) {
                    list.add(data[j])
                }
                dataList.add(list)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_sort_page_view, parent, false)

        return ViewPageHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPageHolder, position: Int) {
        holder.adapter.setData(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewPageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val adapter = MySortAdapter.SortGridItemAdapter()
        private val gridView: RecyclerView = itemView.findViewById(R.id.grid_view)

        init {
            gridView.adapter = adapter
            val layoutManager: GridLayoutManager = object :
                GridLayoutManager(itemView.context,4, LinearLayoutManager.VERTICAL, false){
                override fun canScrollVertically(): Boolean {
                    return false
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }

            }
            gridView.layoutManager = layoutManager
        }

    }

    class SortGridItemAdapter : RecyclerView.Adapter<SortGridItemAdapter.SortGridItemHolder>() {

        private var list: ArrayList<HomeModel.TgoodsCategoryVo> = ArrayList()

        fun setData(data: ArrayList<HomeModel.TgoodsCategoryVo>) {
            list = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortGridItemHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_sort_page_item, parent, false)

            val viewLayoutParams = view.layoutParams
            viewLayoutParams.height =  DisplayTool.dpToPx(parent.context,65.0)
            view.layoutParams = viewLayoutParams
           val layoutParams =  view.findViewById<ImageView>(R.id.image).layoutParams
            layoutParams.width = DisplayTool.dpToPx(parent.context,22.0)
            layoutParams.height = DisplayTool.dpToPx(parent.context,22.0)
            view.findViewById<ImageView>(R.id.image).layoutParams = layoutParams

            view.findViewById<TextView>(R.id.titles).textSize = 12.0.toFloat()


            return SortGridItemHolder(view)
        }

        override fun onBindViewHolder(holder: SortGridItemHolder, position: Int) {
            val model = list[position]
            holder.title.text = model.cname

            if (model.sort == -1) {
                Glide.with(holder.itemView)
                    .load(model.id)
                    .into(holder.image)

            } else {
                Glide.with(holder.itemView)
                    .load(model.imgUrl)
                    .into(holder.image)
            }
            holder.itemView.setOnClickListener{
                onclickItem(it,model)
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        class SortGridItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.image)
            val title: TextView = itemView.findViewById(R.id.titles)
        }

        fun onclickItem(it:View,model:HomeModel.TgoodsCategoryVo){
            JudgeLogin.judge(it.context) { _ ->
                when (model.cname) {
                    "我的地址"->{
                        val intent = Intent(it.context, AddressActivity::class.java)
                        it.context.startActivity(intent)
                    }
                    "我的收藏"->{
                        val intent = Intent(it.context, CollectActivity::class.java)
                        it.context.startActivity(intent)
                    }
                }

            }
        }



    }

}