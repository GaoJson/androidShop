package com.example.myshop.http

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.alibaba.fastjson.serializer.SerializerFeature
import com.example.myshop.http.model.HomeModel
import okhttp3.*
import java.io.IOException


object HttpUtil {

    fun get(context:Activity,url: String, callback: HttpCallback) {
        request(context,url,null,callback)
    }

    fun post(context:Activity,url: String,params:HashMap<String,Any>, callback: HttpCallback) {
        request(context,url,params,callback)
    }

    private fun request(context: Activity, url: String, params: HashMap<String, Any>?, callback: HttpCallback){
        Thread {
            val uri = NWApi.baseUrl+url
            val client: OkHttpClient = OkHttpClient.Builder().build()
            val request: okhttp3.Request = okhttp3.Request.Builder()
                .url(uri)
                .also { builder ->
                    if (params != null){
                        val formBody = FormBody.Builder()
                            .also { builders ->
                                params.forEach { (name, value) ->
                                    //参数需要 add 进入FormBody.Builder
                                    builders.add(name, "$value")
                                }
                            }.build()
                        builder.post(formBody)
                    }
                    builder.addHeader("token","111")
                }
                .build()

            client.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    context.runOnUiThread {
                        callback.fail(e.localizedMessage)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body()?.string().toString()
                    val result = JSON.parseObject(res,TResult::class.java)
                    if (result.code == 200){
                        context.runOnUiThread {
                            callback.success(result.data)
                        }
                    } else{
                        context.runOnUiThread {
                            callback.fail(result.msg)
                        }
                    }
                }
            })
        }.start()
    }





}

