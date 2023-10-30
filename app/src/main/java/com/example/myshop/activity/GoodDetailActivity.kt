package com.example.myshop.activity

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.databinding.ActivityGoodDetailBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.Collect
import com.example.myshop.db.ShopCar
import com.example.myshop.db.User
import com.example.myshop.http.HttpCallback
import com.example.myshop.http.HttpUtil
import com.example.myshop.http.NWApi
import com.example.myshop.http.model.GoodModel
import com.example.myshop.tool.ToastTools
import com.example.myshop.userinfn.UserInfo
import com.google.gson.Gson
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GoodDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityGoodDetailBinding

    private var goodModel = GoodModel()
    private var bannerList:List<String> = arrayListOf()

    private var likeFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        loadData()
    }

    private fun loadData(){
        val id = intent.getIntExtra("goodId",0)
        val url = NWApi.getGoodsDetail+"/$id"
        HttpUtil.get(this,url,object : HttpCallback{
            override fun success(data: String?) {
                goodModel = Gson().fromJson(data,GoodModel::class.java)
                bannerList = goodModel.goodsBanner.split(",")
                binding.banner.setDatas(bannerList)

                binding.goodSalePrice.text = "¥${goodModel.goodsPrice.toDouble()}"
                binding.goodPrice.text = "¥${goodModel.originalPrice.toDouble()}"
                binding.goodName.text = goodModel.goodsName
                binding.goodCount.text = "已售：${goodModel.saleCount}"
                binding.goodPreview.text = "浏览：${goodModel.pageView}"
                binding.spec.text = goodModel.spec
                val head = "<head>" + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                        "<style>*{margin:0;padding:0;}img{max-width: 100%; width:auto; height:auto;}</style>" +
                        "</head>";
                binding.webView.loadDataWithBaseURL(null, "<html>" + head+ "<body>"
                        + goodModel.goodsContent + "</body></html>","text/html", "charset=UTF-8", null);
            }

            override fun fail(error: String?) {

            }
        })

    }


    private fun setUI() {
        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                println(newProgress)
                if (newProgress == 100) {
                    val handler = Handler()
                    handler.postDelayed({
                        binding.webView.layoutParams.height = binding.webView.contentHeight
                        binding.webView.layoutParams = binding.webView.layoutParams
                    }, 1000)
                }
            }
        }

        binding.banner.setAdapter(object : BannerImageAdapter<String>(bannerList){
            override fun onBindView(
                holder: BannerImageHolder,
                data: String?,
                position: Int,
                size: Int
            ) {
                Glide.with(holder.itemView)
                    .load(data)
                    .into(holder.imageView)
            }
        })

        binding.goodPrice.paintFlags = binding.goodPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        binding.leftBtn.setOnClickListener {
            finish()
        }

        binding.shopCarBtn.setOnClickListener{
            val model = ShopCar(goodModel)
            model.userId = UserInfo.user.id
            ShopCar.saveModel(model,this@GoodDetailActivity)
            ToastTools.showMsg(this,"加入购物车成功")
        }
        binding.likeBtn.setOnClickListener{
            if (likeFlag) {
                Collect.deleteModel(goodModel.id)
                ToastTools.showMsg(this,"取消收藏成功")
                binding.likeBtn.setImageResource(R.drawable.icon_goods_not_like)
            } else {
                Collect.saveModel(goodModel)
                ToastTools.showMsg(this,"收藏成功")
                binding.likeBtn.setImageResource(R.drawable.icon_goods_like)
            }
        }

        val id = intent.getIntExtra("goodId",0)
        GlobalScope.launch {
           val exit:Int =  AppDatabaseManager.db.collectDao.selectModel(id,UserInfo.user.id)
            runOnUiThread{
                if (exit>0){
                    likeFlag = true
                    binding.likeBtn.setImageResource(R.drawable.icon_goods_like)
                } else {
                    likeFlag = false
                    binding.likeBtn.setImageResource(R.drawable.icon_goods_not_like)
                }
            }
        }



    }

}