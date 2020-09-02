package com.brianbaek.imagefinder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brianbaek.imagefinder.R
import com.brianbaek.imagefinder.databinding.ItemMainImageBinding
import com.brianbaek.imagefinder.util.RecyclerBindingListener
import com.google.android.flexbox.FlexboxLayoutManager

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>, RecyclerBindingListener {
    var doclist: MutableList<com.brianbaek.imagefinder.network.dto.Document> = mutableListOf()

    val visibleThreshold: Int = 5
    var loading: Boolean = true
    var prevListSize: Int = 0
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0

    var flexboxLayoutManager: FlexboxLayoutManager

    lateinit var loadMoreListener: LoadMoreListener

    interface LoadMoreListener {
        fun onLoad()
    }

    constructor(recyclerview: RecyclerView) {
        flexboxLayoutManager = recyclerview.layoutManager as FlexboxLayoutManager

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = flexboxLayoutManager.itemCount
                lastVisibleItem = flexboxLayoutManager.findLastVisibleItemPosition()

                if (totalItemCount < 1) {
                    return
                }

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    Log.d("mainAdapter", "loading == true")
                    loading = true

                    if (loadMoreListener != null) {
                        loadMoreListener.onLoad()
                    }
                }
            }
        })
    }

    override fun setData(list: MutableList<com.brianbaek.imagefinder.network.dto.Document>?) {
        if (list == null) {
            Log.d("mainAdapter", "list == null")
            doclist.clear()
            prevListSize = 0
            notifyDataSetChanged()
            return
        }

        Log.d("mainAdapter", "setData list size = ${list.size}")

        if (doclist == null || doclist.size == 0) {
            Log.d("mainAdapter", "doclist == null || doclist.size == 0")
            doclist = list
            notifyDataSetChanged()
            prevListSize = list.size
        } else {
            Log.d("mainAdapter", "setData else")
            notifyItemRangeChanged(prevListSize, doclist.size)
            prevListSize = list.size
        }

        loading = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        var itemMainImageBinding: ItemMainImageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_main_image, parent, false)

        return MainViewHolder(itemMainImageBinding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        Log.d("mainAdapter", "onBindViewHolder pos = $position")
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (doclist == null) {
            return 0
        }

        return doclist.size
    }

    inner class MainViewHolder : RecyclerView.ViewHolder {
        var imageBinding: ItemMainImageBinding
        var mPos: Int = 0

        constructor(imageBinding: ItemMainImageBinding) :super(imageBinding.root) {
            this.imageBinding = imageBinding
        }

        fun onBind(pos: Int) {
            this.mPos = pos

            if(doclist != null) {
                Log.d("mainAdapter", "doclist != null")
                imageBinding.thumbURL = doclist.get(mPos).thumbnail_url
                imageBinding.displaySite = doclist.get(mPos).display_sitename
            }
        }
    }
}