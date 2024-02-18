package com.example.sbscovidapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.extensions.uiFormatted
import com.example.sbscovidapp.lifecycle.rememberStateWithLifecycle
import com.example.sbscovidapp.stats.CovidStatsViewModel
import com.example.sbscovidapp.ui.theme.SbsCovidAppTheme
import com.example.sbscovidapp.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val covidStatsViewModel: CovidStatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            SbsCovidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(snackbarHostState)
                        }
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            CovidStatsContent(viewModel = covidStatsViewModel, snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CovidStatsContent(viewModel: CovidStatsViewModel, snackbarHostState: SnackbarHostState) {
    val viewState = rememberStateWithLifecycle(stateFlow = viewModel.uiStateFlow)
    Log.d("Search", "UI $viewState")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            text = "Search and select a region from the menu."
        )

        AutocompleteMenuOptions(
            region = viewState.value.region,
            regionList = viewState.value.regionList,
            onItemClicked = {
                viewModel.onRegionSelected(it)
            }
        )
        when {
            viewState.value.isStatsLoading -> {
                LoadingMessage()
            }

            viewState.value.showCovidStatsError -> {
                CovidStatsErrorRetry(viewModel = viewModel, region = viewState.value.region)
            }

            else -> {
                CovidStatsTable(covidStats = viewState.value.covidStats.uiFormatted())
            }
        }

        if (viewState.value.showRegionListError) {
            RegionListErrorSnackbar(viewModel = viewModel, snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
internal fun CovidStatsTable(covidStats: List<Pair<String, String>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(covidStats) {
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
fun AutocompleteMenuOptions(
    region: Region,
    regionList: List<Region>,
    onItemClicked: (Region) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRegionText by remember { mutableStateOf(region.name) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true,
            value = selectedRegionText,
            onValueChange = { selectedRegionText = it },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        // filter options based on text field value
        val filteringOptions = regionList.filter {
            it.iso.contains(selectedRegionText, ignoreCase = true) ||
            it.name.contains(selectedRegionText, ignoreCase = true)
        }
        if (filteringOptions.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier
                    .background(Color.White)
                    .exposedDropdownSize(true)
                ,
                properties = PopupProperties(focusable = false),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                filteringOptions.forEach { region ->
                    DropdownMenuItem(
                        text = { Text(region.name) },
                        onClick = {
                            selectedRegionText = region.name
                            expanded = false
                            onItemClicked(region)

                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}


@Composable
fun LoadingMessage() {
    Box(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .padding(vertical = 24.dp)
    ) {
        Text(text = "Loading COVID data...")
    }
}

@Composable
fun CovidStatsErrorRetry(viewModel: CovidStatsViewModel, region: Region) {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "An error occurred attempting to load data"
            )
            Button(
                modifier = Modifier.padding(vertical = 16.dp),
                onClick = {
                    viewModel.getCovidStatsForRegion(region)
                }) {
                Text(text = "RETRY")
            }
        }
    }
}

@Composable
fun RegionListErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    viewModel: CovidStatsViewModel
) {
    LaunchedEffect(snackbarHostState) {
        val result = snackbarHostState.showSnackbar(
            message = "ERROR: Click retry to load the full list of available regions.",
            actionLabel = "RETRY",
            duration = SnackbarDuration.Indefinite
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.getRegionsList()
        }
    }
}