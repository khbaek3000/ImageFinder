package com.brianbaek.imagefinder.di.module

import android.content.Context
import com.brianbaek.imagefinder.BuildConfig
import com.brianbaek.imagefinder.network.KakaoService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val NAME_KAKAO_SERVICE = "kakaoHost"

@Module
class NetworkModule (){
    val CONNECT_TIMEOUT = 20L
    val WRITE_TIMEOUT = 20L
    val READ_TIMEOUT = 20L
    val CACHE_SIZE: Long = 10 * 1024 * 1024



    @Provides
    @Singleton
    fun provideOkHttpCache(application: Context): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        var httpLogingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLogingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return httpLogingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        var clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        clientBuilder
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    @Named(NAME_KAKAO_SERVICE)
    fun provideRetrofit(okhttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(BuildConfig.KakaoApiHost)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoService(@Named(NAME_KAKAO_SERVICE) retrofit: Retrofit): KakaoService {
        return retrofit.create(KakaoService::class.java)
    }
}