package com.example.pixabaymvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pixabaymvvm.di.remoteDataSourceModule
import com.example.pixabaymvvm.di.rxModule
import com.example.pixabaymvvm.di.viewModelModule
import com.example.pixabaymvvm.models.Hits
import com.example.pixabaymvvm.remoteDataSource.RemoteDataSource
import com.example.pixabaymvvm.ui.main.HitsUIModel
import com.example.pixabaymvvm.ui.main.MainViewModel
import com.example.pixabaymvvm.utils.TestSchedulerProvider
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainViewModelTest : KoinTest {

    lateinit var mainViewModel: MainViewModel

    @Mock
    lateinit var remoteDataSource: RemoteDataSource
    @Mock
    lateinit var hitsObserver: Observer<HitsUIModel>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        startKoin {
            modules(
                listOf(
                    rxModule,
                    remoteDataSourceModule(),
                    viewModelModule
                )
            )
        }
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(remoteDataSource, TestSchedulerProvider())
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testGetHitsSuccess() {
        val returnedData = Hits(ArrayList(), 1, 1)
        val searchQuery = "Flowers"

        Mockito.`when`(remoteDataSource.getHits(searchQuery))
            .thenReturn(Single.just(returnedData))

        mainViewModel.getRatesLiveData().observeForever(hitsObserver)

        mainViewModel.getHits(searchQuery)

        verify(hitsObserver).onChanged(
            HitsUIModel(isSuccessful = true, hits = returnedData.hits)
        )

    }

    @Test
    fun testGetHitsFailed() {
        val returnedData = IllegalStateException("error!")
        val searchQuery = "Flowers"

        Mockito.`when`(remoteDataSource.getHits(searchQuery))
            .thenReturn(Single.error(returnedData))

        mainViewModel.getRatesLiveData().observeForever(hitsObserver)

        mainViewModel.getHits(searchQuery)

        verify(hitsObserver).onChanged(
            HitsUIModel(errorMessage = returnedData.message)
        )

    }

}