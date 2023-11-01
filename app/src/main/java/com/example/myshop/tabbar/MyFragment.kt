package com.example.myshop.tabbar

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.allViews
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.myshop.R
import com.example.myshop.activity.LoginActivity
import com.example.myshop.activity.SettingActivity
import com.example.myshop.activity.my.OrderActivity
import com.example.myshop.adapter.HomeSortAdapter
import com.example.myshop.adapter.MySortAdapter
import com.example.myshop.databinding.FragmentCategoryBinding
import com.example.myshop.databinding.FragmentHomeBinding
import com.example.myshop.databinding.FragmentMyBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.db.Order
import com.example.myshop.db.User
import com.example.myshop.http.model.HomeModel
import com.example.myshop.tool.JudgeLogin
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import q.rorbin.badgeview.QBadgeView

class MyFragment : Fragment() {

    private var binding: FragmentMyBinding ?= null
    private val bind get()=binding!!
   private  lateinit var launcherImg: ActivityResultLauncher<Array<String>>



   private var orderList:ArrayList<QBadgeView> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         launcherImg = registerForActivityResult( ActivityResultContracts.OpenDocument()){
             if (it != null) {
                 val  user = UserInfo.user
                 user.headIcon = it.toString()
                 if (user != null) {
                     UserInfo.saveUser(context!!,user)
                     UserInfo.updateUser()
                 }
                 updateData()
             }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (binding == null) {
            binding = FragmentMyBinding.inflate(inflater,container,false)
            setUI()
        }
        updateData()
        return bind.root
    }

    private fun setUI() {
        bind.headIconImg.setOnClickListener{
            if (UserInfo.loginFlag) {
                launcherImg.launch(arrayOf("image/*"))
            } else {
                val intent = Intent(activity,LoginActivity::class.java)
                startActivity(intent)
            }
        }

        bind.settingBtn.setOnClickListener{
            val intent = Intent(activity,SettingActivity::class.java)
            startActivity(intent)
        }

        bind.allOrderBtn.setOnClickListener{
            JudgeLogin.judge(activity!!){
                val intent = Intent(activity,OrderActivity::class.java)
                startActivity(intent)
            }
        }

        val adapter = MySortAdapter()
        bind.sortViewpage.adapter = adapter
        var list = arrayListOf<HomeModel.TgoodsCategoryVo>()
        var titleList = arrayOf("我的地址","我的收藏")
        for (i in titleList.indices) {
            val name = "ic_my_service_${i+1}"
            val rId = resources.getIdentifier(name,"drawable",activity!!.packageName)
            list.add(HomeModel.TgoodsCategoryVo(cname = titleList[i],id = rId, sort = -1))
        }
        adapter.setData(list)

        for (i in 0 until bind.orderView.children.count()) {
            val view = bind.orderView.getChildAt(i)
            val shopCarQBadgeView: QBadgeView = QBadgeView(activity)
            shopCarQBadgeView.bindTarget(view)
            shopCarQBadgeView.badgeText = "0";
            shopCarQBadgeView.badgeGravity = Gravity.TOP or Gravity.END;
            shopCarQBadgeView.setGravityOffset(5F,0F, true);
            shopCarQBadgeView.setBadgeTextSize(10F, true);
            shopCarQBadgeView.setBadgePadding(5F, true);
            shopCarQBadgeView.visibility = View.GONE
            orderList.add(shopCarQBadgeView)
            view.setOnClickListener { _ ->
               JudgeLogin.judge(activity!!){
                   val intent = Intent(activity,OrderActivity::class.java)
                   var index = i+1
                   if (i == 4){
                       index = 4
                   }
                   intent.putExtra("type",index)
                   startActivity(intent)
               }
            }

        }

    }


    override fun onResume() {
        super.onResume()
        updateData()

    }

    private fun updateData(){
        if (UserInfo.loginFlag) {
            bind.account.text = UserInfo.user.account
            Glide.with(bind.headIcon)
                .load(UserInfo.user.headIcon)
                .into(bind.headIconImg)

            GlobalScope.launch {
                val count1 = AppDatabaseManager.db.orderDao.selectCount(UserInfo.user.id,1)
                val count2 = AppDatabaseManager.db.orderDao.selectCount(UserInfo.user.id,2)
                val count3 = AppDatabaseManager.db.orderDao.selectCount(UserInfo.user.id,3)
                val count4 = AppDatabaseManager.db.orderDao.selectCount(UserInfo.user.id,4)

                activity?.runOnUiThread {
                    if (count1 > 0) {
                        orderList[0].visibility = View.VISIBLE
                        orderList[0].badgeText = "$count1"
                    } else {
                        orderList[0].visibility = View.GONE
                        orderList[0].badgeText = "$count2"
                    }
                    if (count2 > 0) {
                        orderList[1].visibility = View.VISIBLE
                        orderList[1].badgeText = "$count2"
                    }
                    if (count3 > 0) {
                        orderList[2].visibility = View.VISIBLE
                        orderList[2].badgeText = "$count3"
                    }
                    if (count4 > 0) {
                        orderList[3].visibility = View.VISIBLE
                        orderList[3].badgeText = "$count4"
                    }
                }
            }







        } else {
            bind.account.text = "登录"
            Glide.with(bind.headIcon)
                .load(R.drawable.default_avatar)
                .into(bind.headIconImg)
        }
    }


}