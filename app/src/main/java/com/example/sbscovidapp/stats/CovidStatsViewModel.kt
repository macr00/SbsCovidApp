package com.example.sbscovidapp.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbscovidapp.domain.interactor.GetCovidStats
import com.example.sbscovidapp.domain.interactor.GetRegionList
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.model.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CovidStatsViewModel
@Inject constructor(
    private val getCovidStats: GetCovidStats,
    private val getRegionList: GetRegionList,
) : ViewModel() {

    private val covidStatsStateFlow: MutableStateFlow<CovidStats?> =
        MutableStateFlow(CovidStats.Empty)

    private val regionListStateFlow: MutableStateFlow<List<Region>> =
        MutableStateFlow(listOf(DefaultGlobal))

    private val selectedRegionStateFlow: MutableStateFlow<Region> =
        MutableStateFlow(DefaultGlobal)

    // Distinct operator is applied to only emit when the selected region changes
    private val distinctRegionFlow: Flow<Region> = selectedRegionStateFlow
        .distinctUntilChanged { old, new -> old.iso == new.iso }

    @Suppress("UNCHECKED_CAST")
    val uiStateFlow = combine(
        covidStatsStateFlow,
        getCovidStats.loadingStateFlow,
        distinctRegionFlow,
        regionListStateFlow,
        getRegionList.loadingStateFlow
    ) { args: Array<*> ->
        CovidStatsViewState(
            covidStats = args[0] as CovidStats?,
            isStatsLoading = args[1] as Boolean,
            region = args[2] as Region,
            regionList = args[3] as List<Region>,
            isRegionListLoading = args[4] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CovidStatsViewState.Initial
    )

    init {
        loadCountryList()
        observeRegionChanges()
    }

    // TODO implement retry on failure
    fun loadCountryList() {
        viewModelScope.launch {
            try {
                val result = getRegionList(Unit)
                regionListStateFlow.update {
                    listOf(DefaultGlobal) + result.getOrThrow()
                }
            } catch (e: Exception) {
                // TODO
            }
        }
    }

    fun loadRegionStats(region: Region) {
        viewModelScope.launch {
            try {
                val iso = region.iso.ifBlank { null }
                val result = getCovidStats(GetCovidStats.Params(iso))
                covidStatsStateFlow.update {
                    result.getOrThrow()
                }
            } catch (e: Exception) {
                covidStatsStateFlow.emit(null)
            }
        }
    }

    fun onRegionSelected(region: Region) {
        viewModelScope.launch {
            selectedRegionStateFlow.emit(region)
        }
    }

    private fun observeRegionChanges() {
        viewModelScope.launch {
            distinctRegionFlow
                .collectLatest {
                    loadRegionStats(it)
                }
        }
    }
}

// TODO perhaps move to domain package
val DefaultGlobal = Region("", "Global")