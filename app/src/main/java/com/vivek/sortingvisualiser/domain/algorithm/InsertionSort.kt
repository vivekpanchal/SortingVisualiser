package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertionSort : SortingAlgorithm {

    override fun sort(array: IntArray): Flow<SortingStep> = flow {
        val arr = array.copyOf()
        val n = arr.size
        var comparisons = 0
        var swaps = 0

        for (i in 1 until n) {
            val key = arr[i]
            var j = i - 1

            emit(buildStep(arr, comparisons, swaps,
                keyIndex = i,
                activeIndex = j,
                sortedUntil = i,
                description = "Inserting element [$key]"
            ))

            while (j >= 0 && arr[j] > key) {
                comparisons++
                arr[j + 1] = arr[j]
                swaps++
                emit(buildStep(arr, comparisons, swaps,
                    keyIndex = j + 1,
                    activeIndex = j,
                    sortedUntil = i,
                    description = "Shifting [${arr[j]}] right"
                ))
                j--
            }
            arr[j + 1] = key

            if (j + 1 != i) {
                emit(buildStep(arr, comparisons, swaps,
                    keyIndex = j + 1,
                    activeIndex = -1,
                    sortedUntil = i + 1,
                    description = "Placed [$key] at position ${j + 1}"
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
        keyIndex: Int,
        activeIndex: Int,
        sortedUntil: Int,
        description: String
    ): SortingStep {
        val elements = arr.mapIndexed { index, value ->
            ArrayElement(
                value = value,
                state = when {
                    index == keyIndex -> ElementState.SWAPPING
                    index == activeIndex -> ElementState.COMPARING
                    index < sortedUntil -> ElementState.SORTED
                    else -> ElementState.DEFAULT
                }
            )
        }
        return SortingStep(elements, comparisons, swaps, description)
    }
}
