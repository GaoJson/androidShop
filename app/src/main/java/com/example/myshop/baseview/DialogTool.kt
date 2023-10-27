package com.example.myshop.baseview

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.ViewGroup
import android.widget.EditText
import java.util.Date

class DialogTool {

    companion object {
        fun showInputDialog(context: Context,title:String,hint:String,ensure:(value:String)->Unit,cancel:()->Unit) {
            val editText = EditText(context)
            editText.hint = hint
            editText.background = null
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            val lay = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,200)
            editText.setPadding(50,30,50,0)
            lay.marginStart = 100
            lay.marginEnd = 100
            editText.layoutParams = lay
            AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                    ensure.invoke(editText.text.toString())
                })
                .setNegativeButton("取消") { _, _ ->

                }
                .setOnDismissListener {

                }
                .show()




        }

        fun showCommentDialog(context: Context,title:String,hint:String,ensure:(value:String)->Unit,cancel:()->Unit) {
            val editText = EditText(context)
            editText.hint = hint
            editText.background = null
            val lay = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,200)
            editText.setPadding(50,30,50,0)
            lay.marginStart = 100
            lay.marginEnd = 100
            editText.layoutParams = lay
            AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                    ensure.invoke(editText.text.toString())
                })
                .setNegativeButton("取消") { _, _ ->

                }
                .setOnDismissListener {

                }
                .show()




        }

    }


}