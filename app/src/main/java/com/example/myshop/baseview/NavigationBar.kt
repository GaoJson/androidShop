package com.example.myshop.baseview


import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.myshop.MainActivity
import com.example.myshop.R
import com.example.myshop.tool.DisplayTool


class NavigationBar(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var title = ""
    set(value){
        findViewById<TextView>(R.id.titles).text = value
        field = value
    }

    private var showBack = true
    set(value) {
        findViewById<TextView>(R.id.left_arrow).isVisible = value
        field = value
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this, true)

        val layoutParams:android.view.ViewGroup.LayoutParams = findViewById<View?>(R.id.statusBar).layoutParams
        layoutParams.height = DisplayTool.getStatusBarHeight(context)
        findViewById<View?>(R.id.statusBar).layoutParams = layoutParams


//        layoutParams.height = DisplayTool.getStatusBarHeight(context)+DisplayTool.dpToPx(context,50.0)
//        layoutParams.pa = DisplayTool.getStatusBarHeight(context)
//        this.layoutParams = layoutParams
        initUI()
        initAttribute(attrs)

    }


    private fun initUI() {
        findViewById<ImageView>(R.id.left_arrow).setOnClickListener{
         val activity =  context as Activity
            activity.finish()
        }

    }

    fun insertRightView() {
        val rightView = findViewById<LinearLayout>(R.id.right_view)
        val imageView:ImageView = ImageView(this.context)
        val  layout = MarginLayoutParams(30,30)
        layout.marginEnd = 10
        imageView.layoutParams = layout

        imageView.setImageResource(R.drawable.arrow_right)
        rightView.addView(imageView)
    }

    fun insertRightText():TextView{
        val rightView = findViewById<LinearLayout>(R.id.right_view)
        val text = TextView(this.context)
        val  layout = MarginLayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.marginEnd = 10
        text.layoutParams = layout
        rightView.addView(text)
        return text
    }


    private fun initAttribute(attrs: AttributeSet?){
        val a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar)
        this.title = a.getString(R.styleable.NavigationBar_title).toString()
        this.showBack = a.getBoolean(R.styleable.NavigationBar_show_back,true)
    }

}