package com.example.myshop.tabbar


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader

import com.example.myshop.R
import com.example.myshop.activity.GoodDetailActivity
import com.example.myshop.adapter.HomeAdapter
import com.example.myshop.adapter.HomeSortAdapter
import com.example.myshop.databinding.FragmentHomeBinding
import com.example.myshop.http.HttpCallback
import com.example.myshop.http.HttpUtil
import com.example.myshop.http.JsonUtils
import com.example.myshop.http.NWApi
import com.example.myshop.http.model.GoodModel
import com.example.myshop.http.model.HomeModel
import com.example.myshop.tabbar.view.HomeTop
import com.example.myshop.tool.DisplayTool
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var homeModel: HomeModel? = null
    private var homeAdapter = HomeAdapter()

    private var homeTopView: HomeTop? = null

    private var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshData{ }
        loadMoreData { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            setUI()
        }
        return binding.root
    }


    private fun refreshData(call: (end: Boolean) -> Unit) {
        HttpUtil.get(
            activity!!,
            NWApi.goodsMain,
            object : HttpCallback {
                override fun success(data: String?) {
                    val model = JsonUtils().fromJson<HomeModel>(data.toString())
                    homeModel = model
                    homeModel?.let {
                        homeTopView?.reloadBanner(it.topBannerVos)
                        homeTopView?.reloadSort(it.tgoodsCategoryVos)
                    }
                    call(true)
                }

                override fun fail(error: String?) {
                    call(true)
                }
            })
    }


    private fun loadMoreData(call: (end: Boolean) -> Unit) {
        val params = HashMap<String, Any>()
        params["pageNum"] = page
        params["pageSize"] = 4
        HttpUtil.post(
            activity!!,
            NWApi.goodsList,
            params,
            object : HttpCallback {
                override fun success(data: String?) {
                    val info: JSONObject = JSON.parseObject(data)
                    val total = "${info["total"]}".toInt()
                    val array = info["rows"] as JSONArray
                    if (page == 1){
                        homeAdapter.list.clear()
                    }
                    array.forEach {
                        val model = Gson().fromJson(JSON.toJSONString(it), GoodModel::class.java)
                        homeAdapter.list.add(model)
                    }
                    if (homeAdapter.list.size >= total) {
                        call(true)
                    }else{
                        call(false)
                    }
                    homeAdapter.notifyDataSetChanged()
                }

                override fun fail(error: String?) {
                    call(true)
                }
            }
        )

    }


    private fun setUI() {

        _binding?.refreshLayout?.setRefreshHeader(ClassicsHeader(this.context))
        _binding?.refreshLayout?.setRefreshFooter(ClassicsFooter(this.context))
        _binding?.refreshLayout?.setOnRefreshListener {
            page = 1
            refreshData{ _ ->
                it.finishRefresh()
            }
            loadMoreData { }
        }
        _binding?.refreshLayout?.setOnLoadMoreListener {
            page += 1
            loadMoreData { success ->
                if (success) {
                    it.finishLoadMoreWithNoMoreData()
                } else {
                    it.finishLoadMore(true)
                }
            }
        }
        binding.recycle.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)

        binding.recycle.adapter = homeAdapter
        homeAdapter.setClickListener {
            val intent = Intent(context,GoodDetailActivity::class.java)
            intent.putExtra("goodId",it.id)
            startActivity(intent)
        }

        val header = RecyclerViewHeader.fromXml(activity, R.layout.home_top_view)
        header?.attachTo(binding.recycle)
        homeTopView = header.findViewById(R.id.home_top)
        header.layoutParams.height = (DisplayTool.getScreenWidth(context!!)/2.46 + DisplayTool.dpToPx(context!!,160.0)).toInt()
        header.layoutParams = header.layoutParams

    }

}