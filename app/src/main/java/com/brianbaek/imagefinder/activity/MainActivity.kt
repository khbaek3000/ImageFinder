package com.brianbaek.imagefinder.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brianbaek.imagefinder.R
import com.brianbaek.imagefinder.adapter.MainAdapter
import com.brianbaek.imagefinder.databinding.ActivityMainBinding
import com.brianbaek.imagefinder.di.ViewModelFactory
import com.brianbaek.imagefinder.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mainVM: MainViewModel

    private lateinit var mainBinding: ActivityMainBinding

    lateinit var adapter: MainAdapter
    lateinit var flexLM: FlexboxLayoutManager

    var tablayoutItemMap: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainVM = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        mainVM.init()

        mainBinding.mainVM = mainVM

        if (mainBinding.recyclerview == null) {
            Log.d("MainActivity", "mainBinding.recyclerview == null")
        }

        flexLM = FlexboxLayoutManager(this)

        mainBinding.recyclerview.layoutManager = flexLM

        adapter = MainAdapter(mainBinding.recyclerview)
        adapter.loadMoreListener = object : MainAdapter.LoadMoreListener {
            override fun onLoad() {
                Log.d("MainActivity", "loadMoreListener onLoad")
                mainVM.getImage(false)
            }
        }
        mainBinding.recyclerview.adapter = adapter

        mainBinding.tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (mainVM != null) {
                    var pos: Int? = tab?.position
                    mainVM.selectCollection = mainVM.filterLive.value?.get(pos ?: 0)
                    mainVM.documentShowObs.set(null)
                    mainVM.documentShowObs.notifyChange()
                    mainVM.processShowList()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })


        mainBinding.swipeRefresh.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                if (mainVM != null) {
                    mainVM.getImage(true)
                }

                mainBinding.swipeRefresh.isRefreshing = false
            }
        })


//        val searchBtn = findViewById<Button>(R.id.btSearch)

        mainBinding.btSearch.setOnClickListener {
            // your code to perform when the user clicks on the button
            Log.d("MainViewModel", "mainBinding.etSearch onClick")
            if (mainVM != null && mainBinding.etSearch.text != null) {
                mainVM.queryText.set(mainBinding.etSearch.text.toString())
                tablayoutItemMap.clear()
                mainVM.getImage(true)
            }
        }

        initLiveData()

        mainVM.getImage(true)

    }

    private fun initLiveData() {
        mainVM.filterLive.observe(this, {list: MutableList<String> ->
            initTablayout(list)
        })
    }

    private fun initTablayout(list: MutableList<String>) {
        if (list == null) {
            return
        }

        if (mainBinding.tablayout.tabCount > 0 && tablayoutItemMap.isEmpty()) {
            tablayout.removeAllTabs()
        }

//        if (mainBinding.tablayout.tabCount > 0) {
//            tablayout.removeAllTabs()
//        }

        var pos = 0
        for (filter in list) {
            if (tablayoutItemMap.get(filter) == null) {
                mainBinding.tablayout.addTab(mainBinding.tablayout.newTab())
                var tab = mainBinding.tablayout.getTabAt(pos)
                tab?.setText(filter)
                pos++
            }
        }
    }
}