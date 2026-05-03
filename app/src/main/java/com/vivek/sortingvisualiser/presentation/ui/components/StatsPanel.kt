package com.vivek.sortingvisualiser.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vivek.sortingvisualiser.domain.model.AlgorithmType
import com.vivek.sortingvisualiser.presentation.ui.theme.SortingVisualizerTheme

@Composable
fun StatsPanel(
    comparisons: Int,
    swaps: Int,
    elapsedTimeMs: Long,
    algorithm: AlgorithmType,
    currentDescription: String,
    isComplete: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Comparisons", value = comparisons.toString())
            StatItem(label = "Swaps", value = swaps.toString())
            StatItem(label = "Time", value = "${elapsedTimeMs}ms")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = algorithm.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Time: ${algorithm.timeComplexity}  ·  Space: ${algorithm.spaceComplexity}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isComplete) {
                Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                    Text(
                        text = "✓ Sorted",
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        if (currentDescription.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = currentDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, name = "In Progress")
@Composable
private fun StatsPanelInProgressPreview() {
    SortingVisualizerTheme(dynamicColor = false) {
        StatsPanel(
            comparisons = 128,
            swaps = 64,
            elapsedTimeMs = 342,
            algorithm = AlgorithmType.QUICK_SORT,
            currentDescription = "Partitioning around pivot 42",
            isComplete = false
        )
    }
}

@Preview(showBackground = true, name = "Complete")
@Composable
private fun StatsPanelCompletePreview() {
    SortingVisualizerTheme(dynamicColor = false) {
        StatsPanel(
            comparisons = 256,
            swaps = 128,
            elapsedTimeMs = 1024,
            algorithm = AlgorithmType.MERGE_SORT,
            currentDescription = "",
            isComplete = true
        )
    }
}
