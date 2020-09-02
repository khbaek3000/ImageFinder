package com.brianbaek.imagefinder.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class ImageBindingAdapter {
}

@BindingAdapter("thumbURL")
fun ImageView.bindThumb(thumbURL: String) {
    if (thumbURL != null) {
        Log.d("ImageView.bind", "thumbURL != null")
        Glide.with(this.context).asBitmap().load(thumbURL).into(this)
    }
}