package com.example.sbscovidapp.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger


class LoadingStateObservable {

    private val count = AtomicInteger(0)
    private val loadingState = MutableStateFlow(count.get())

    val isLoadingFlow: Flow<Boolean> = loadingState
        .map { it > 0 }
        .distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}