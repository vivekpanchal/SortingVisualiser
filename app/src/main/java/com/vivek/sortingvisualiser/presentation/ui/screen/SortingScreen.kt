package com.vivek.sortingvisualiser.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vivek.sortingvisualiser.presentation.ui.components.AlgorithmSelector
import com.vivek.sortingvisualiser.presentation.ui.components.ControlPanel
import com.vivek.sortingvisualiser.presentation.ui.components.Legend
import com.vivek.sortingvisualiser.presentation.ui.components.SortingCanvas
import com.vivek.sortingvisualiser.presentation.ui.components.StatsPanel
import com.vivek.sortingvisualiser.presentation.viewmodel.SortingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingScreen(
    viewModel: SortingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sorting Visualizer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AlgorithmSelector(
                selectedAlgorithm = uiState.selectedAlgorithm,
                enabled = !uiState.isRunning,
                onAlgorithmSelected = viewModel::setAlgorithm,
                modifier = Modifier.fillMaxWidth()
            )

            SortingCanvas(
                elements = uiState.elements,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Legend()

            StatsPanel(
                comparisons = uiState.comparisons,
                swaps = uiState.swaps,
                elapsedTimeMs = uiState.elapsedTimeMs,
                algorithm = uiState.selectedAlgorithm,
                currentDescription = uiState.currentDescription,
                isComplete = uiState.isComplete
            )

            ControlPanel(
                isRunning = uiState.isRunning,
                isPaused = uiState.isPaused,
                isComplete = uiState.isComplete,
                arraySize = uiState.arraySize,
                speed = uiState.speed,
                onStart = viewModel::startSort,
                onPauseResume = viewModel::pauseResume,
                onReset = viewModel::reset,
                onShuffle = viewModel::shuffle,
                onArraySizeChange = viewModel::setArraySize,
                onSpeedChange = viewModel::setSpeed,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
