package com.example.myshop.tabbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.example.myshop.adapter.CategoryLeftMenuAdapter
import com.example.myshop.adapter.CategoryRightViewAdapter
import com.example.myshop.databinding.FragmentCategoryBinding
import com.example.myshop.http.HttpCallback
import com.example.myshop.http.HttpUtil
import com.example.myshop.http.JsonUtils
import com.example.myshop.http.NWApi
import com.example.myshop.http.model.GoodModel
import com.example.myshop.http.model.HomeModel
import com.google.gson.Gson
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader


class CategoryFragment : Fragment() {

    private var _bind:FragmentCategoryBinding?=null
    private val binding get() = _bind!!

    private  val leftMenuAdapter = CategoryLeftMenuAdapter()
    private val rightViewAdapter = CategoryRightViewAdapter()

    private var page = 1
    private var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (_bind == null) {
            _bind = FragmentCategoryBinding.inflate(inflater,container,false)
            setUI()
            getMenuList()
        }
        
        return binding.root
    }

    private  fun getMenuList(){
        HttpUtil.get(
            activity!!,
            NWApi.goodsMain,
            object : HttpCallback {
                override fun success(data: String?) {
                    val model = JsonUtils().fromJson<HomeModel>(data.toString())
                    leftMenuAdapter.reloadData(model?.tgoodsCategoryVos)
                    categoryId = (model?.tgoodsCategoryVos?.get(0)?.id!!)
                    getContentList{
                        if (it){
                            binding.refreshLayout.finishLoadMoreWithNoMoreData()
                        }else{
                            binding.refreshLayout.resetNoMoreData()
                        }
                    }
                }

                override fun fail(error: String?) {

                }
            })
    }

    private fun getContentList(callback:(end: Boolean) -> Unit){
        val params = HashMap<String, Any>()
        params["pageNum"] = page
        params["pageSize"] = 10
        params["goodsCid"] = categoryId
        HttpUtil.post(
            activity!!,
            NWApi.getGoodsList,
            params,
            object : HttpCallback {
                override fun success(data: String?) {
                  val info:JSONObject = JSON.parseObject(data)
                    val total = "${info["total"]}".toInt()
                    val array = info["rows"] as JSONArray
                    if (page == 1){
                        rightViewAdapter.dataList.clear()
                    }
                    array.forEach {
                        val model = Gson().fromJson(JSON.toJSONString(it), GoodModel::class.java)
                        rightViewAdapter.dataList.add(model)
                    }
                    if (rightViewAdapter.dataList.size >= total) {
                        callback(true)
                    }else{
                        callback(false)
                    }
                    rightViewAdapter.notifyDataSetChanged()
                    if (rightViewAdapter.dataList.size > 0){
                        binding.noData.visibility = View.GONE
                    } else {
                        binding.noData.visibility = View.VISIBLE
                    }
                }

                override fun fail(error: String?) {
                    callback(false)
                }
            }
        )


    }

   private fun setUI() {
        binding.leftMenu.layoutManager = LinearLayoutManager(context)
        binding.leftMenu.adapter = leftMenuAdapter
        leftMenuAdapter.setClickListener {
            categoryId = leftMenuAdapter.dataList[it].id
            page = 1
            getContentList{end ->
                println(end)
                if (end){
                    binding.refreshLayout.finishLoadMoreWithNoMoreData()
                }else{
                    binding.refreshLayout.resetNoMoreData()
                }
            }
        }

       binding.refreshLayout.setRefreshHeader(ClassicsHeader(context))
       binding.refreshLayout.setRefreshFooter(ClassicsFooter(context))
       binding.refreshLayout.setOnRefreshListener {
            page = 1
           getContentList {end ->
               it.finishRefresh()
                if (end){
                    it.finishLoadMoreWithNoMoreData()
                }else{
                    it.resetNoMoreData()
                }
           }
       }
       binding.refreshLayout.setOnLoadMoreListener {
            page++
           getContentList {end ->
               it.finishLoadMore()
           if (end){
                   it.finishLoadMoreWithNoMoreData()
               }else{
               it.finishLoadMore()
           }
           }
       }
       binding.rightContent.layoutManager = LinearLayoutManager(context)
      binding.rightContent.adapter = rightViewAdapter

    }


}