package com.example.sbscovidapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbscovidapp.domain.interactor.GetCovidStats
import com.example.sbscovidapp.domain.model.CovidStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalStatsViewModel
@Inject constructor(
    private val getCovidStats: GetCovidStats,
) : ViewModel() {

    //private val globalStatsLoadingState = LoadingState()
    private val globalStatsStateFlow = MutableStateFlow(CovidStats.Empty)

    private val _uiStateFlow = MutableStateFlow(GlobalViewState())

    val uiStateFlow = combine(
        globalStatsStateFlow,
        getCovidStats.loadingStateFlow,
    ) { args: Array<*> ->
        GlobalViewState(
            globalStats = args[0] as CovidStats,
            isLoading = args[1] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GlobalViewState()
    )

    init {
        loadGlobalStats()
    }

    private fun loadGlobalStats() {
        viewModelScope.launch {
            try {
                val result = getCovidStats(GetCovidStats.Params("AUS"))
                if (result.isSuccess) {
                    globalStatsStateFlow.update { result.getOrThrow() }
                }
            } catch (e: Exception) {
                // TODO handle exception
            }
        }
    }

    /*
    private fun loadGlobalStats() {
        viewModelScope.launch {
            getGlobalStats(Unit).launchWith(
                scope = this,
                loadingState = globalStatsLoadingState,
            ).catch { t ->
                // TODO handle exception - perhaps in collectResult
            }.collectResult(
                onSuccess = { value -> globalStatsState.update { value } },
                onFailure = { }
            )
        }
    }

     */

}