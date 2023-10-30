package com.example.myshop

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.core.animation.addListener
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.navigation.ui.AppBarConfiguration
import com.example.myshop.activity.LoginActivity
import com.example.myshop.broadcast.ShopCarCountReceiver
import com.example.myshop.databinding.ActivityAddressBinding
import com.example.myshop.databinding.ActivityMainBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.User
import com.example.myshop.tabbar.CategoryFragment
import com.example.myshop.tabbar.HomeFragment
import com.example.myshop.tabbar.MyFragment
import com.example.myshop.tabbar.ShopCatFragment
import com.example.myshop.tool.DisplayTool
import com.example.myshop.userinfn.UserInfo
import com.gx.city_picker.CityPickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import q.rorbin.badgeview.QBadgeView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var tabbarList:List<View>
    private var currectIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserInfo.getUser(this)

        val wic = ViewCompat.getWindowInsetsController(window.decorView)
        if (wic != null){
            wic.isAppearanceLightStatusBars = true
        }
        DisplayTool.getStatusBarHeight(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabbarSetting()
        transparentStatusBar(window)
    }

    fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility
        window.statusBarColor = Color.TRANSPARENT

        //设置状态栏文字颜色
        setStatusBarTextColor(window, true)
    }

    fun setStatusBarTextColor(window: Window, light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = if (light) { //白色文字
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else { //黑色文字
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }



    override fun onResume() {
        super.onResume()
        fragmentList[currectIndex].onResume()
    }

    private fun tabbarSetting(){

        binding.tabbarItemHome.setOnClickListener {
            clickTabbarItem(0)
        }
        binding.tabbarItemCategory.setOnClickListener {
            clickTabbarItem(1)
        }
        binding.tabbarItemShopCar.setOnClickListener {
            clickTabbarItem(2)
        }
        binding.tabbarItemMy.setOnClickListener {
            clickTabbarItem(3)
        }

        tabbarList = arrayListOf(binding.tabbarItemHome,binding.tabbarItemCategory,binding.tabbarItemShopCar,binding.tabbarItemMy)
        clickTabbarItem(0)

        val shopCarQBadgeView:QBadgeView = QBadgeView(this)
        val linearLayout:LinearLayout = tabbarList[2] as LinearLayout;
        shopCarQBadgeView.bindTarget(linearLayout)
        shopCarQBadgeView.badgeText = "0";
        shopCarQBadgeView.badgeGravity = Gravity.TOP or Gravity.START;
        shopCarQBadgeView.setGravityOffset(50F,0F, true);
        shopCarQBadgeView.setBadgeTextSize(10F, true);
        shopCarQBadgeView.setBadgePadding(5F, true);

        val countReceiver = ShopCarCountReceiver()
        val intentFilter = IntentFilter("com.app.SHOPCARCOUNT")
        registerReceiver(countReceiver,intentFilter)
        ShopCarCountReceiver.updateShopCar(this)
        countReceiver.setListener {
            if (UserInfo.loginFlag) {
                GlobalScope.launch {
                    val data =  AppDatabaseManager.db.shopCarDao.getShopCarList(UserInfo.user.id)
                    runOnUiThread {
                        var count = 0
                        data.forEach{
                            count += it.count
                        }
                        shopCarQBadgeView.badgeText = "$count"
                    }
                }
            } else {
                shopCarQBadgeView.badgeText = "0"
            }
        }
    }

    private val titleList = arrayListOf("首页","分类","购物车","我的")
    private val list = arrayListOf(
        R.drawable.tab1,
        R.drawable.tab2,
        R.drawable.tab4,
        R.drawable.tab5
    )
    private val selectList = arrayListOf(
        R.drawable.tab1_select,
        R.drawable.tab2_select,
        R.drawable.tab4_select,
        R.drawable.tab5_select
    )
    private  val  fragmentList = arrayListOf(HomeFragment(),CategoryFragment(),ShopCatFragment(),MyFragment())

    private fun clickTabbarItem(index: Int){
        currectIndex = index
        val count = tabbarList.size
        for (i in 0 until count){
            val view:LinearLayout = tabbarList[i] as LinearLayout
            var textView = view[1] as TextView
            textView.text = titleList[i]
            var imageView = view[0] as ImageView
            if (index == i){
                supportFragmentManager.beginTransaction().replace(R.id.main_frame,fragmentList[i]).commit()
                imageView.setImageResource(selectList[i])
                textView.setTextColor(Color.parseColor("#fE7725"))
            } else{
                imageView.setImageResource(list[i])
                textView.setTextColor(Color.parseColor("#999999"))
            }

        }


    }

    fun updateShopCar(data:IntArray){

        val x = data[0]+DisplayTool.dpToPx(this,8.0)
        val y = data[1]-DisplayTool.dpToPx(this,22.0)
        var animatorX =  ObjectAnimator.ofFloat(binding.shopCar, "translationX", x.toFloat())
        var animatorY =  ObjectAnimator.ofFloat(binding.shopCar, "translationY", y.toFloat())
        val animatorSet = AnimatorSet()
        animatorSet.play(animatorX).with(animatorY)
        animatorSet.duration = 1
        animatorSet.start()
        animatorSet.addListener(object : AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(p0: Animator?) {
               binding.shopCar.visibility = View.VISIBLE
                shopCarAnimator()
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
    }

    fun shopCarAnimator() {
        val y = DisplayTool.getScreenHeight(this) - DisplayTool.dpToPx(this,50.0)
        val x = DisplayTool.getScreenWidth(this) * 0.6
        val endX =  ObjectAnimator.ofFloat(binding.shopCar, "translationX", x.toFloat())
        val endY =  ObjectAnimator.ofFloat(binding.shopCar, "translationY", y.toFloat())
        val endSet = AnimatorSet()
        endSet.play(endX).with(endY)
        endSet.duration = 500
        endSet.start()
        endSet.addListener(object : AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                binding.shopCar.clearAnimation()
                binding.shopCar.visibility = View.GONE

            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }
        })
    }



}