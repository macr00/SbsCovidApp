package com.example.sbscovidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.extensions.readableData
import com.example.sbscovidapp.lifecycle.rememberStateWithLifecycle
import com.example.sbscovidapp.ui.theme.SbsCovidAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GlobalStatsViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SbsCovidAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CovidStatsTable(viewModel = viewModel)
                    //Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun CovidStatsTable(viewModel: GlobalStatsViewModel) {
    val viewState = rememberStateWithLifecycle(stateFlow = viewModel.uiStateFlow)
    CovidStatsTable(covidStats = viewState.value.globalStats)
}

@Composable
internal fun CovidStatsTable(covidStats: CovidStats) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SbsCovidAppTheme {
        Greeting("Android")
    }
}