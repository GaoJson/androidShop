package com.example.myshop.activity

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.bumptech.glide.Glide
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityGoodDetailBinding
import com.example.myshop.http.HttpCallback
import com.example.myshop.http.HttpUtil
import com.example.myshop.http.NWApi
import com.example.myshop.http.model.GoodModel
import com.google.gson.Gson
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class GoodDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityGoodDetailBinding

    private var goodModel = GoodModel()
    private var bannerList:List<String> = arrayListOf()

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


    }

}