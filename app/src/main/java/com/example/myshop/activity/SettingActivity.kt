package com.example.myshop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.example.myshop.R
import com.example.myshop.baseview.BaseActivity
import com.example.myshop.tool.DisplayTool
import com.example.myshop.userinfn.UserInfo

class SettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        DisplayTool.transparentStatusBar(window)

        findViewById<TextView>(R.id.exitAccount).setOnClickListener{
            UserInfo.exitUser(this)
            finish()
        }



       val listview = findViewById<ListView>(R.id.listView)
        val listTitle = ArrayList<HashMap<String,Any>>()
        for (i in 0 until 5){
            val map = HashMap<String,Any>()
            map["name"] = "标签$i"
            listTitle.add(map)
        }
        listview.divider = null;
        listview.adapter = SimpleAdapter(this,
            listTitle,
            R.layout.list_title_layout,
            arrayOf("name"),
            intArrayOf(R.id.titles)
            )
    }
}