package com.vivek.sortingvisualiser.domain.model

data class SortingStep(
    val elements: List<ArrayElement>,
    val comparisons: Int,
    val swaps: Int,
    val description: String = "",
    val isComplete: Boolean = false
)
