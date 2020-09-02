package com.brianbaek.imagefinder.network

import com.brianbaek.imagefinder.network.dto.Image
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {
    @GET("/v2/search/image")
    fun getSearchImage(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Single<Response<Image>>
}