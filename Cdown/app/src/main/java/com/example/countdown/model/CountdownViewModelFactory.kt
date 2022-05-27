package com.example.countdown.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.countdown.repository.Repository
import com.example.countdown.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

class CountdownViewModelFactory(private val repository: Repository, private val dispatcherProvider: CoroutineDispatcherProvider): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CountdownViewModel::class.java)) return CountdownViewModel(repository, dispatcherProvider) as T
        throw IllegalArgumentException("Wrong ViewModel")
    }

}