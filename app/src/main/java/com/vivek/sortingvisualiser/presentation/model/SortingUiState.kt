package com.vivek.sortingvisualiser.presentation.model

import com.vivek.sortingvisualiser.domain.model.AlgorithmType
import com.vivek.sortingvisualiser.domain.model.ArrayElement

data class SortingUiState(
    val elements: List<ArrayElement> = generateElements(DEFAULT_SIZE),
    val selectedAlgorithm: AlgorithmType = AlgorithmType.BUBBLE_SORT,
    val arraySize: Int = DEFAULT_SIZE,
    val speed: Float = 5f,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isComplete: Boolean = false,
    val comparisons: Int = 0,
    val swaps: Int = 0,
    val elapsedTimeMs: Long = 0L,
    val currentDescription: String = ""
) {
    companion object {
        const val DEFAULT_SIZE = 30

        fun generateElements(size: Int): List<ArrayElement> =
            (1..size).shuffled().map { ArrayElement(it) }
    }
}
