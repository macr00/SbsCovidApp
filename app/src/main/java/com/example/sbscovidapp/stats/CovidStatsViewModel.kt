package com.example.sbscovidapp.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sbscovidapp.domain.interactor.GetCovidStats
import com.example.sbscovidapp.domain.interactor.GetRegionList
import com.example.sbscovidapp.domain.interactor.GetRegionList.Companion.DefaultRegionList
import com.example.sbscovidapp.domain.interactor.invoke
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.model.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CovidStatsViewModel
@Inject constructor(
    private val getCovidStats: GetCovidStats,
    private val getRegionList: GetRegionList,
) : ViewModel() {

    private val selectedRegionStateFlow: MutableStateFlow<Region> =
        MutableStateFlow(Region.Global)

    // Distinct operator is applied to only emit when the selected region changes
    private val distinctRegionFlow: Flow<Region> = selectedRegionStateFlow
        .distinctUntilChanged { old, new -> old.iso == new.iso }

    @Suppress("UNCHECKED_CAST")
    val uiStateFlow = combine(
        getCovidStats.flow.onStart { emit(null) },
        getCovidStats.isLoading,
        getRegionList.flow.onStart { emit(Result.success(DefaultRegionList)) },
        getRegionList.isLoading,
        distinctRegionFlow
    ) { args: Array<*> ->
        CovidStatsViewState(
            covidStatsResult = args[0] as Result<CovidStats>?,
            isStatsLoading = args[1] as Boolean,
            regionListResult = args[2] as Result<List<Region>>?,
            isRegionListLoading = args[3] as Boolean,
            region = args[4] as Region,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CovidStatsViewState.Initial
    )

    init {
        getRegionsList()
        observeRegionChanges()
    }

    fun getRegionsList() {
        viewModelScope.launch {
            getRegionList.invoke()
        }
    }

    fun getCovidStatsForRegion(region: Region) {
        viewModelScope.launch {
            getCovidStats(GetCovidStats.Params(region.iso))
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
                .collectLatest { iso ->
                    getCovidStatsForRegion(iso)
                }
        }
    }
}