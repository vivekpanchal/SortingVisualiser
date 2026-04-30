package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSort : SortingAlgorithm {

    override fun sort(array: IntArray): Flow<SortingStep> = flow {
        val arr = array.copyOf()
        val n = arr.size
        var comparisons = 0
        var swaps = 0

        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                comparisons++
                emit(buildStep(arr, comparisons, swaps,
                    comparing = setOf(j, j + 1),
                    swapping = emptySet(),
                    sortedFrom = n - i,
                    description = "Comparing [${arr[j]}] and [${arr[j + 1]}]"
                ))

                if (arr[j] > arr[j + 1]) {
                    swaps++
                    arr.swap(j, j + 1)
                    emit(buildStep(arr, comparisons, swaps,
                        comparing = emptySet(),
                        swapping = setOf(j, j + 1),
                        sortedFrom = n - i,
                        description = "Swapping → [${arr[j]}] and [${arr[j + 1]}]"
                    ))
                }
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
        sortedFrom: Int,
        description: String
    ): SortingStep {
        val elements = arr.mapIndexed { index, value ->
            ArrayElement(
                value = value,
                state = when {
                    index in swapping -> ElementState.SWAPPING
                    index in comparing -> ElementState.COMPARING
                    index >= sortedFrom -> ElementState.SORTED
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
