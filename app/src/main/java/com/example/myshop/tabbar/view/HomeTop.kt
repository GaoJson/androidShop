package com.example.myshop.tabbar.view


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.allViews
import androidx.core.view.marginLeft
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myshop.R
import com.example.myshop.adapter.HomeSortAdapter
import com.example.myshop.http.model.HomeModel
import com.example.myshop.tool.DisplayTool
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlin.math.floor


class HomeTop(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs, 0) {

    private lateinit var banner:Banner<HomeModel.TopBannerVo,BannerImageAdapter<HomeModel.TopBannerVo>>

    private val sortAdapter = HomeSortAdapter()
    private lateinit var progress:CardView

    public fun reloadBanner(list:List<HomeModel.TopBannerVo>){
        banner.setDatas(list)
    }

    public fun reloadSort(list:List<HomeModel.TgoodsCategoryVo>){

        sortAdapter.setData(list)
        progress.layoutParams.width = DisplayTool.dpToPx(context,80.0/sortAdapter.totalView)
        progress.layoutParams = progress.layoutParams
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.home_top, this, true)
        initUI()
    }

    private fun initUI(){
        banner = findViewById(R.id.home_top_banner)

        val list:ArrayList<HomeModel.TopBannerVo> = ArrayList()
        banner.setAdapter(object :BannerImageAdapter<HomeModel.TopBannerVo>(list){
            override fun onBindView(
                holder: BannerImageHolder,
                data: HomeModel.TopBannerVo,
                position: Int,
                size: Int
            ) {
                Glide.with(holder.itemView)
                    .load(data.coverImg)
                    .into(holder.imageView)
            }
        })
        banner.indicator = CircleIndicator(context)

        progress = findViewById(R.id.progress)

       val viewPager =  findViewById<ViewPager2>(R.id.sort_viewpage)
        viewPager.adapter = sortAdapter
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val margin = progress.layoutParams as MarginLayoutParams
                margin.leftMargin = DisplayTool.dpToPx(context,80.0/sortAdapter.totalView) *position
                progress.layoutParams = margin
            }
        })
    }

}