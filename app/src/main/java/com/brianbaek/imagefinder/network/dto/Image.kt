package com.brianbaek.imagefinder.network.dto

import java.util.*

data class Image(
    var meta: Meta,
    var documents: MutableList<Document>
)

data class Meta(
    var totalCount: Int,
    var pageable_count: Int,
    var isEnd: Boolean
)

data class Document (
    var collection: String,
    var thumbnail_url: String,
    var image_url: String,
    var width: Int,
    var height: Int,
    var display_sitename: String,
    var doc_url: String,
    var datetime: Date
)

