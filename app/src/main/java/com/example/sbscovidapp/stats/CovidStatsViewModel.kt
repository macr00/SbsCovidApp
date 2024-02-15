package com.example.sbscovidapp.stats

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
class CovidStatsViewModel
@Inject constructor(
    private val getCovidStats: GetCovidStats,
) : ViewModel() {

    private val globalStatsStateFlow = MutableStateFlow(CovidStats.Empty)

    val uiStateFlow = combine(
        globalStatsStateFlow,
        getCovidStats.loadingStateFlow,
    ) { args: Array<*> ->
        CovidStatsViewState(
            globalStats = args[0] as CovidStats,
            isLoading = args[1] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CovidStatsViewState()
    )
    fun loadGlobalStats(isoCode: String? = null) {
        viewModelScope.launch {
            try {
                val result = getCovidStats(GetCovidStats.Params(isoCode))
                if (result.isSuccess) {
                    globalStatsStateFlow.update { result.getOrThrow() }
                }
            } catch (e: Exception) {
                // TODO handle exception
            }
        }
    }
}