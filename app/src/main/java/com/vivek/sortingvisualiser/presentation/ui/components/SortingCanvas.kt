package com.vivek.sortingvisualiser.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.presentation.ui.theme.BarComparing
import com.vivek.sortingvisualiser.presentation.ui.theme.BarDefault
import com.vivek.sortingvisualiser.presentation.ui.theme.BarPivot
import com.vivek.sortingvisualiser.presentation.ui.theme.BarSorted
import com.vivek.sortingvisualiser.presentation.ui.theme.BarSwapping

@Composable
fun SortingCanvas(
    elements: List<ArrayElement>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (elements.isEmpty()) return@Canvas

        val canvasWidth = size.width
        val canvasHeight = size.height
        val count = elements.size
        val maxValue = elements.maxOf { it.value }.toFloat()
        val slotWidth = canvasWidth / count
        val gapFraction = 0.12f
        val gap = (slotWidth * gapFraction).coerceAtLeast(1f)
        val barWidth = slotWidth - gap
        val cornerRadius = (barWidth * 0.15f).coerceIn(2f, 6f)

        elements.forEachIndexed { index, element ->
            val barHeight = (element.value / maxValue) * canvasHeight * 0.97f
            val left = index * slotWidth + gap / 2f
            val top = canvasHeight - barHeight

            drawRoundRect(
                color = element.state.toColor(),
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

private fun ElementState.toColor(): Color = when (this) {
    ElementState.DEFAULT -> BarDefault
    ElementState.COMPARING -> BarComparing
    ElementState.SWAPPING -> BarSwapping
    ElementState.SORTED -> BarSorted
    ElementState.PIVOT -> BarPivot
}

@Preview(showBackground = true, widthDp = 360, heightDp = 300)
@Composable
private fun SortingCanvasPreview() {
    SortingCanvas(
        elements = (1..20).shuffled().map { ArrayElement(it) }
    )
}
