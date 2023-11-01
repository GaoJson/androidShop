package com.example.myshop.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myshop.MainActivity
import com.example.myshop.R
import com.example.myshop.activity.GoodDetailActivity
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.db.AppDataBase
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.ShopCar
import com.example.myshop.db.ShopCarDao
import com.example.myshop.http.model.GoodModel
import com.example.myshop.tool.JudgeLogin
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryRightViewAdapter:RecyclerView.Adapter<CategoryRightViewAdapter.CategoryRightViewHolder>() {

     var dataList:ArrayList<GoodModel> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRightViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_right_view_item,parent,false)

        return CategoryRightViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryRightViewHolder, position: Int) {
        holder.updateUI(dataList[position])
        holder.shopCar.setOnClickListener {
            JudgeLogin.judge(holder.itemView.context){_->
                var locationWindow = IntArray(2)
                it.getLocationOnScreen(locationWindow)
                (holder.itemView.context as MainActivity).updateShopCar(locationWindow)
                val shopCar = ShopCar(dataList[position])
                shopCar.userId = UserInfo.user.id
                ShopCar.saveModel(shopCar,holder.itemView.context)
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,GoodDetailActivity::class.java)
            intent.putExtra("goodId",dataList[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
      return dataList.size
    }

    class CategoryRightViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            private val img:ImageView = itemView.findViewById(R.id.image)
            private val goodName:TextView = itemView.findViewById(R.id.good_name)
            private val price:TextView = itemView.findViewById(R.id.price)
            val shopCar = itemView.findViewById<ImageView>(R.id.shopCar)

        fun updateUI(model:GoodModel) {
            Glide.with(itemView)
                .load(model.goodsImg)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                .into(img)
            goodName.text = model.goodsName
            price.text = "¥${model.goodsPrice.toDouble()}"

        }


    }

}