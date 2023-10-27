package com.example.myshop.tool

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

class DisplayTool {
    companion object {

        fun getScreenWidth(context: Context):Int {
            return context.resources.displayMetrics.widthPixels
        }

        fun getScreenHeight(context: Context):Int {
            return context.resources.displayMetrics.heightPixels
        }

        fun dpToPx(context: Context,value: Double):Int{
            val scale = context.resources.displayMetrics.density
            return (value*scale).toInt()
        }

        fun pxToDx(context: Context,value: Double):Int{
            val scale = context.resources.displayMetrics.density
            return (value/scale).toInt()
        }
        fun getStatusBarHeight(context: Context):Int {
            var height = 0
            val resId = context.resources.getIdentifier("status_bar_height","dimen","android")
            if (resId > 0){
                height = context.resources.getDimensionPixelSize(resId)
            }
            return height
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



    }
}