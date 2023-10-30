package com.example.myshop.activity.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.activity.my.adapter.OrderAdapter
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityOrderBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.tool.DisplayTool
import com.example.myshop.userinfn.UserInfo
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrderActivity : BaseActivity() {

    lateinit var binding:ActivityOrderBinding

    private val orderAdapter = OrderAdapter()
    private var orderState:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        orderState = intent.getIntExtra("type",0)
        setUI()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun setUI(){
        binding.navbarList.adapter.loadData(arrayListOf("全部","待支付","未发货","已发货","已完成"))
        binding.navbarList.adapter.setClickListener {
            orderState = it
            loadData()
        }
        binding.navbarList.adapter.selectIndex = orderState
        binding.navbarList.adapter.notifyDataSetChanged()

        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = orderAdapter
        orderAdapter.refresher {
            loadData()
        }

        binding.refreshLayout.setRefreshHeader(ClassicsHeader(this))
        binding.refreshLayout.setRefreshFooter(ClassicsFooter(this))
        binding.refreshLayout.setOnRefreshListener {
            it.finishRefresh(1000)
        }
        binding.refreshLayout.setOnLoadMoreListener {
            it.finishLoadMore(1000)
        }
    }

    fun loadData(){
        GlobalScope.launch {
           var list = if (orderState == 0) {
                AppDatabaseManager.db.orderDao.select(UserInfo.user.id)
           } else if(orderState == 4 || orderState == 5){
               AppDatabaseManager.db.orderDao.selectDone(UserInfo.user.id)
           }
           else {
                AppDatabaseManager.db.orderDao.select(UserInfo.user.id,orderState)
            }

            runOnUiThread {
                if (list.isEmpty()) {
                    binding.noData.visibility = View.VISIBLE
                }else {
                    binding.noData.visibility = View.GONE
                }
                orderAdapter.dataList = list.reversed()
                orderAdapter.notifyDataSetChanged()
            }

        }


    }


}