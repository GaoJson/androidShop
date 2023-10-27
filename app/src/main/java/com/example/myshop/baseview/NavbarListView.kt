package com.example.myshop.baseview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.adapter.NavbarListAdapter

class NavbarListView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    val adapter = NavbarListAdapter()
    init {
        LayoutInflater.from(context).inflate(R.layout.navbar_list_view_layout, this, true)
        val listView = findViewById<RecyclerView>(R.id.listView)
        listView.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        listView.adapter = adapter
    }

}