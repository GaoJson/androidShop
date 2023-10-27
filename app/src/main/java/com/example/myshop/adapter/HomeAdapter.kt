package com.example.myshop.adapter

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
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.ShopCar
import com.example.myshop.http.model.GoodModel
import com.example.myshop.interfaces.ListCallback
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list:ArrayList<GoodModel> = ArrayList()

    private lateinit var listener: (model:GoodModel) -> Unit

    fun setClickListener(listener: (model:GoodModel) -> Unit) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_view, parent, false)


        return HomeItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view:HomeItemViewHolder = holder as HomeItemViewHolder
        view.setData(list[position])
        view.itemView.setOnClickListener {
            listener.invoke(list[position])
        }
        view.shopCar.setOnClickListener {
            var locationWindow = IntArray(2)
            it.getLocationOnScreen(locationWindow)
            (holder.itemView.context as MainActivity).updateShopCar(locationWindow)
            val shopCar = ShopCar(list[position])
            shopCar.userId = UserInfo.user!!.id
            GlobalScope.launch {
                val data = AppDatabaseManager.db.shopCarDao.save(shopCar)
                ShopCarCountReceiver.updateShopCar(holder.itemView.context)
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class HomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val img = itemView.findViewById<ImageView>(R.id.image)
            private val title = itemView.findViewById<TextView>(R.id.titles)
            private val price = itemView.findViewById<TextView>(R.id.priceTv)
            val shopCar = itemView.findViewById<ImageView>(R.id.shopCar)
            fun setData(model:GoodModel) {
                title.text = model.goodsName
                price.text = "¥${model.goodsPrice.toDouble()}"
                Glide.with(itemView)
                    .load(model.goodsImg)
                    .into(img)
            }
    }



}