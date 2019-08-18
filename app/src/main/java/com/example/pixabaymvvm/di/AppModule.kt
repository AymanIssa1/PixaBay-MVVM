package com.example.pixabaymvvm.di

import com.example.pixabaymvvm.ui.main.MainViewModel
import com.example.pixabaymvvm.util.rx.ApplicationSchedulerProvider
import com.example.pixabaymvvm.util.rx.SchedulerProvider
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module


val rxModule = module {
    factory<SchedulerProvider> { ApplicationSchedulerProvider() }
}

val viewModelModule = module {
    viewModel<MainViewModel>()
}