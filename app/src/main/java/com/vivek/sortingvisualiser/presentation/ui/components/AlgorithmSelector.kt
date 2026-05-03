package com.vivek.sortingvisualiser.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vivek.sortingvisualiser.domain.model.AlgorithmType
import com.vivek.sortingvisualiser.presentation.ui.theme.SortingVisualizerTheme

@Composable
fun AlgorithmSelector(
    selectedAlgorithm: AlgorithmType,
    enabled: Boolean,
    onAlgorithmSelected: (AlgorithmType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(AlgorithmType.entries) { algorithm ->
            FilterChip(
                selected = algorithm == selectedAlgorithm,
                onClick = { if (enabled) onAlgorithmSelected(algorithm) },
                enabled = enabled,
                label = {
                    Text(
                        text = algorithm.displayName,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlgorithmSelectorPreview() {
    SortingVisualizerTheme(dynamicColor = false) {
        AlgorithmSelector(
            selectedAlgorithm = AlgorithmType.BUBBLE_SORT,
            enabled = true,
            onAlgorithmSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Disabled")
@Composable
private fun AlgorithmSelectorDisabledPreview() {
    SortingVisualizerTheme(dynamicColor = false) {
        AlgorithmSelector(
            selectedAlgorithm = AlgorithmType.QUICK_SORT,
            enabled = false,
            onAlgorithmSelected = {}
        )
    }
}
