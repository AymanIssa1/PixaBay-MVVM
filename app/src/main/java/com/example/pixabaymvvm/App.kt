package com.example.pixabaymvvm

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.pixabaymvvm.di.remoteDataSourceModule
import com.example.pixabaymvvm.di.rxModule
import com.example.pixabaymvvm.di.viewModelModule
import com.facebook.stetho.Stetho
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(
                    remoteDataSourceModule(),
                    rxModule,
                    viewModelModule
                )
            )
        }

        // To track internet Connectivity
        val networkInspectorConfiguration = ConnectionBuddyConfiguration.Builder(this).build()
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration)

    }
}