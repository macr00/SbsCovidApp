package com.example.sbscovidapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.extensions.readableData
import com.example.sbscovidapp.stats.CovidStatsViewModel
import com.example.sbscovidapp.lifecycle.rememberStateWithLifecycle
import com.example.sbscovidapp.ui.theme.SbsCovidAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var globalStatsViewModel: CovidStatsViewModel
    private lateinit var countryViewModel: CovidStatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SbsCovidAppTheme {
                globalStatsViewModel = hiltViewModel(key = "global")
                countryViewModel = hiltViewModel(key = "country")
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        CovidStatsTable(viewModel = globalStatsViewModel)
                        CovidStatsTable(viewModel = countryViewModel)
                    }
                }
            }
            // TODO update this
            globalStatsViewModel.loadGlobalStats()
            countryViewModel.loadGlobalStats("SWE")
        }
    }
}

@Composable
fun CovidStatsTable(viewModel: CovidStatsViewModel) {
    val viewState = rememberStateWithLifecycle(stateFlow = viewModel.uiStateFlow)
    Log.d("View State", viewState.toString())
    CovidStatsTable(covidStats = viewState.value.globalStats)
}

@Composable
internal fun CovidStatsTable(covidStats: CovidStats) {
    LazyColumn(Modifier.padding(16.dp)) {
        val tableItems = covidStats.readableData()
        items(tableItems) {
            TableRow(label = it.first, value = it.second)
        }
    }
}

@Composable
internal fun TableRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        LabelCell(label = label)
        ValueCell(value = value)
    }
}

@Composable
internal fun RowScope.LabelCell(label: String) {
    Text(text = label, Modifier.weight(0.5f))
}

@Composable
internal fun RowScope.ValueCell(value: String) {
    Text(text = value, Modifier.weight(0.5f))
}
