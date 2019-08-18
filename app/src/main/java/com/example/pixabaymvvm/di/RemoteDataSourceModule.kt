package com.example.pixabaymvvm.di

import com.example.pixabaymvvm.BuildConfig
import com.example.pixabaymvvm.remoteDataSource.RemoteDataSource
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun remoteDataSourceModule(): Module {
    return module {
        this.factory { createHttpLoggingInterceptor() }
        factory { createOkHttpClient(get()) }
        factory { createGsonConverter() }
        factory { createRxJava2CallAdapter() }
        single { createWebService<RemoteDataSource>(get(), get(), get(), BuildConfig.baseUrl) }
    }
}

fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return httpLoggingInterceptor
}

fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.connectTimeout(60L, TimeUnit.SECONDS)
    httpClient.readTimeout(60L, TimeUnit.SECONDS)
    httpClient.addInterceptor(httpLoggingInterceptor)
    if (BuildConfig.testMode)
        httpClient.addNetworkInterceptor(StethoInterceptor())
    return httpClient.build()
}

fun createGsonConverter(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun createRxJava2CallAdapter(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    rxJava2CallAdapter: RxJava2CallAdapterFactory,
    url: String
): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(rxJava2CallAdapter)
        .build().create(T::class.java)
}