package com.vivek.sortingvisualiser.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vivek.sortingvisualiser.presentation.ui.theme.BarComparing
import com.vivek.sortingvisualiser.presentation.ui.theme.BarDefault
import com.vivek.sortingvisualiser.presentation.ui.theme.BarPivot
import com.vivek.sortingvisualiser.presentation.ui.theme.BarSorted
import com.vivek.sortingvisualiser.presentation.ui.theme.BarSwapping

@Composable
fun Legend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(label = "Default", color = BarDefault)
        LegendItem(label = "Compare", color = BarComparing)
        LegendItem(label = "Swap", color = BarSwapping)
        LegendItem(label = "Sorted", color = BarSorted)
        LegendItem(label = "Pivot", color = BarPivot)
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
