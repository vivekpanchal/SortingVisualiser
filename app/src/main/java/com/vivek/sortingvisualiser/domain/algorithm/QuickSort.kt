package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuickSort : SortingAlgorithm {

    override fun sort(array: IntArray): Flow<SortingStep> = flow {
        val arr = array.copyOf()
        var comparisons = 0
        var swaps = 0
        val sortedIndices = mutableSetOf<Int>()

        suspend fun snapshot(
            pivotIdx: Int? = null,
            comparing: Set<Int> = emptySet(),
            swapping: Set<Int> = emptySet(),
            description: String
        ) {
            emit(SortingStep(
                elements = arr.mapIndexed { idx, v ->
                    ArrayElement(v, when {
                        idx == pivotIdx -> ElementState.PIVOT
                        idx in swapping -> ElementState.SWAPPING
                        idx in comparing -> ElementState.COMPARING
                        idx in sortedIndices -> ElementState.SORTED
                        else -> ElementState.DEFAULT
                    })
                },
                comparisons = comparisons,
                swaps = swaps,
                description = description
            ))
        }

        suspend fun partition(low: Int, high: Int): Int {
            val pivot = arr[high]
            var i = low - 1

            snapshot(pivotIdx = high, description = "Pivot: [${pivot}] at index $high")

            for (j in low until high) {
                comparisons++
                snapshot(
                    pivotIdx = high,
                    comparing = setOf(j),
                    description = "Comparing [${arr[j]}] with pivot [$pivot]"
                )
                if (arr[j] <= pivot) {
                    i++
                    if (i != j) {
                        swaps++
                        val tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp
                        snapshot(
                            pivotIdx = high,
                            swapping = setOf(i, j),
                            description = "Swapping [${arr[i]}] and [${arr[j]}]"
                        )
                    }
                }
            }

            swaps++
            val tmp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = tmp
            sortedIndices.add(i + 1)
            snapshot(
                swapping = setOf(i + 1, high),
                description = "Placed pivot [$pivot] at final position ${i + 1}"
            )
            return i + 1
        }

        suspend fun quickSort(low: Int, high: Int) {
            if (low >= high) {
                if (low == high) sortedIndices.add(low)
                return
            }
            val pi = partition(low, high)
            quickSort(low, pi - 1)
            quickSort(pi + 1, high)
        }

        quickSort(0, arr.size - 1)

        emit(SortingStep(
            elements = arr.map { ArrayElement(it, ElementState.SORTED) },
            comparisons = comparisons,
            swaps = swaps,
            description = "Sorted!",
            isComplete = true
        ))
    }
}
