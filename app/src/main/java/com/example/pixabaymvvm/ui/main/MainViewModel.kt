package com.example.pixabaymvvm.ui.main

import androidx.lifecycle.MutableLiveData
import com.example.pixabaymvvm.models.Hit
import com.example.pixabaymvvm.remoteDataSource.RemoteDataSource
import com.example.pixabaymvvm.ui.BaseViewModel
import com.example.pixabaymvvm.util.extensions.with
import com.example.pixabaymvvm.util.rx.SchedulerProvider

class MainViewModel(private val remoteDataSource: RemoteDataSource, private val scheduler: SchedulerProvider) :
    BaseViewModel() {

    private val hitsLiveData: MutableLiveData<HitsUIModel> = MutableLiveData()

    fun getRatesLiveData(): MutableLiveData<HitsUIModel> = hitsLiveData

    fun getHits(searchQuery: String) {
        hitsLiveData.postValue(HitsUIModel(isLoading = true))

        launch {
            remoteDataSource
                .getHits(searchQuery)
                .with(scheduler)
                .subscribe({
                    hitsLiveData.postValue(HitsUIModel(isSuccessful = true, hits = it.hits))
                }, {
                    hitsLiveData.postValue(HitsUIModel(errorMessage = it.message))
                })
        }
    }

}

data class HitsUIModel(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val hits: ArrayList<Hit>? = null,
    val errorMessage: String? = null
)
