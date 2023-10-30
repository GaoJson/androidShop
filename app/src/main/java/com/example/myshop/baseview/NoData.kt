package com.example.myshop.baseview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myshop.R

class NoData(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs)  {

    init {
        LayoutInflater.from(context).inflate(R.layout.no_data_layout, this, true)
    }


}