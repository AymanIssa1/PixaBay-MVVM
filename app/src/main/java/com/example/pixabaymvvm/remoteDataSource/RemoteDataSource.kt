package com.example.pixabaymvvm.remoteDataSource

import com.example.pixabaymvvm.BuildConfig
import com.example.pixabaymvvm.models.Hits
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteDataSource {

    @GET("/api/")
    fun getHits(@Query("q") searchQuery: String, @Query("key") apiKey: String = BuildConfig.apiKey): Single<Hits>

}