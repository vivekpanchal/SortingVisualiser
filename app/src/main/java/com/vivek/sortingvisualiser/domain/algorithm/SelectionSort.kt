package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectionSort : SortingAlgorithm {

    override fun sort(array: IntArray): Flow<SortingStep> = flow {
        val arr = array.copyOf()
        val n = arr.size
        var comparisons = 0
        var swaps = 0

        for (i in 0 until n - 1) {
            var minIndex = i

            for (j in i + 1 until n) {
                comparisons++
                emit(buildStep(arr, comparisons, swaps,
                    comparing = setOf(j, minIndex),
                    swapping = emptySet(),
                    sortedUntil = i,
                    description = "Comparing [${arr[j]}] with min [${arr[minIndex]}]"
                ))
                if (arr[j] < arr[minIndex]) {
                    minIndex = j
                }
            }

            if (minIndex != i) {
                swaps++
                arr.swap(i, minIndex)
                emit(buildStep(arr, comparisons, swaps,
                    comparing = emptySet(),
                    swapping = setOf(i, minIndex),
                    sortedUntil = i,
                    description = "Placing minimum [${arr[i]}] at position $i"
                ))
            }
        }

        emit(SortingStep(
            elements = arr.map { ArrayElement(it, ElementState.SORTED) },
            comparisons = comparisons,
            swaps = swaps,
            description = "Sorted!",
            isComplete = true
        ))
    }

    private fun buildStep(
        arr: IntArray,
        comparisons: Int,
        swaps: Int,
        comparing: Set<Int>,
        swapping: Set<Int>,
        sortedUntil: Int,
        description: String
    ): SortingStep {
        val elements = arr.mapIndexed { index, value ->
            ArrayElement(
                value = value,
                state = when {
                    index in swapping -> ElementState.SWAPPING
                    index in comparing -> ElementState.COMPARING
                    index < sortedUntil -> ElementState.SORTED
                    else -> ElementState.DEFAULT
                }
            )
        }
        return SortingStep(elements, comparisons, swaps, description)
    }

    private fun IntArray.swap(i: Int, j: Int) {
        val tmp = this[i]; this[i] = this[j]; this[j] = tmp
    }
}
