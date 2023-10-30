package com.example.myshop.activity.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.activity.my.adapter.CollectAdapter
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.databinding.ActivityCollectBinding
import com.example.myshop.db.AppDatabaseManager
import com.example.myshop.tool.DisplayTool
import com.example.myshop.userinfn.UserInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollectActivity : BaseActivity() {

    private lateinit var binding:ActivityCollectBinding

    private var adapter = CollectAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DisplayTool.transparentStatusBar(window)
        setUI()
        loadData()
    }

    fun loadData(){
        GlobalScope.launch {
           val data = AppDatabaseManager.db.collectDao.select(UserInfo.user.id)
            runOnUiThread {
                adapter.dataList.addAll(data)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setUI(){
        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView.adapter = adapter
        adapter.binding = binding

    }

}