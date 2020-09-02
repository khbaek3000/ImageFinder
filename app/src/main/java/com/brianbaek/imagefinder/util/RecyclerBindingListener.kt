package com.brianbaek.imagefinder.util

import com.brianbaek.imagefinder.network.dto.Document

interface RecyclerBindingListener {
    fun setData(list: MutableList<Document>?)
}