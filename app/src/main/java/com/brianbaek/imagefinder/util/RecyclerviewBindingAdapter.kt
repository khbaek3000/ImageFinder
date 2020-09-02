package com.brianbaek.imagefinder.util

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brianbaek.imagefinder.network.dto.Document

class RecyclerviewBindingAdapter {

}

@BindingAdapter("dataList")
fun RecyclerView.bindDataList(list: MutableList<Document>?){

    Log.d("RecyclerView.dataList", "list != null")

    var recyclerBindingListener: RecyclerBindingListener = this.adapter as RecyclerBindingListener

    if (recyclerBindingListener != null) {
        Log.d("RecyclerView.dataList", "recyclerBindingListener != null")
        recyclerBindingListener.setData(list)
    }
}