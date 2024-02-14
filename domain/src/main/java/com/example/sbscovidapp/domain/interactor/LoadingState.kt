package com.example.sbscovidapp.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class LoadingState {

    private val count = AtomicInteger(0)
    private val loadingState = MutableStateFlow(count.get())

    val inProgress: Flow<Boolean> = loadingState
        .map { it > 0 }
        .distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

fun <T> Flow<T>.launchWith(loadingState: LoadingState, scope: CoroutineScope): Flow<T> =
    onEach {
        val job = scope.launch { loadingState.addLoader() }
        job.invokeOnCompletion { loadingState.removeLoader() }
        job.join()
    }
