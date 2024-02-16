package com.example.sbscovidapp

import android.content.res.Resources.Theme
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.extensions.readableData
import com.example.sbscovidapp.stats.CovidStatsViewModel
import com.example.sbscovidapp.lifecycle.rememberStateWithLifecycle
import com.example.sbscovidapp.ui.theme.SbsCovidAppTheme
import com.example.sbscovidapp.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val covidStatsViewModel: CovidStatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SbsCovidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        CovidStatsContent(viewModel = covidStatsViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CovidStatsContent(viewModel: CovidStatsViewModel) {
    val viewState = rememberStateWithLifecycle(stateFlow = viewModel.uiStateFlow)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            text = "COVID Data",
            style = Typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Use the dropdown menu to select a region.")
        CountryDropdownMenu(
            region = viewState.value.region,
            items = viewState.value.regionList,
            onItemClicked = {
                viewModel.onRegionSelected(it)
            }
        )
        // TODO switch to a when statement for error state with covid stats empty
        if (viewState.value.isStatsLoading) {
            LoadingMessage()
        } else {
            CovidStatsTable(covidStats = viewState.value.covidStats)
        }
    }
}

@Composable
internal fun CovidStatsTable(covidStats: CovidStats) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tableItems = covidStats.readableData()
        items(tableItems) {
            TableRow(label = it.first, value = it.second)
        }
    }
}

@Composable
internal fun TableRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        TableCell(label, Alignment.CenterStart)
        TableCell(value, Alignment.CenterEnd)
    }
}

@Composable
internal fun RowScope.TableCell(value: String, alignment: Alignment) {
    Box(
        Modifier
            .weight(0.5f)
            .padding(vertical = 12.dp),
        contentAlignment = alignment
    ) {
        Text(text = value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdownMenu(
    region: Region,
    items: List<Region>,
    onItemClicked: (Region) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = region.name,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            items.forEach { country ->
                DropdownMenuItem(
                    text = {
                        Text(text = country.name)
                    },
                    onClick = {
                        onItemClicked(country)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingMessage() {
    Box(modifier = Modifier
        .wrapContentWidth(align = Alignment.CenterHorizontally)
        .padding(vertical = 24.dp)) {
        Text(text = "Loading Covid statistics...")
    }
}