package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ArrayElement
import com.vivek.sortingvisualiser.domain.model.ElementState
import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MergeSort : SortingAlgorithm {

    override fun sort(array: IntArray): Flow<SortingStep> = flow {
        val arr = array.copyOf()
        var comparisons = 0
        var swaps = 0
        val sortedIndices = mutableSetOf<Int>()

        suspend fun snapshot(
            comparing: Set<Int> = emptySet(),
            swapping: Set<Int> = emptySet(),
            description: String
        ) {
            emit(SortingStep(
                elements = arr.mapIndexed { idx, v ->
                    ArrayElement(v, when {
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

        suspend fun merge(left: Int, mid: Int, right: Int) {
            val leftSlice = arr.copyOfRange(left, mid + 1)
            val rightSlice = arr.copyOfRange(mid + 1, right + 1)
            var i = 0; var j = 0; var k = left

            while (i < leftSlice.size && j < rightSlice.size) {
                comparisons++
                snapshot(
                    comparing = setOf(left + i, mid + 1 + j),
                    description = "Merging: [${leftSlice[i]}] vs [${rightSlice[j]}]"
                )
                if (leftSlice[i] <= rightSlice[j]) {
                    arr[k] = leftSlice[i]; i++
                } else {
                    arr[k] = rightSlice[j]; j++
                }
                swaps++
                k++
            }
            while (i < leftSlice.size) { arr[k++] = leftSlice[i++]; swaps++ }
            while (j < rightSlice.size) { arr[k++] = rightSlice[j++]; swaps++ }

            for (idx in left..right) sortedIndices.add(idx)
            snapshot(description = "Merged subarray [$left..$right]")
        }

        suspend fun mergeSort(left: Int, right: Int) {
            if (left >= right) {
                sortedIndices.add(left)
                return
            }
            val mid = (left + right) / 2
            mergeSort(left, mid)
            mergeSort(mid + 1, right)
            merge(left, mid, right)
        }

        mergeSort(0, arr.size - 1)

        emit(SortingStep(
            elements = arr.map { ArrayElement(it, ElementState.SORTED) },
            comparisons = comparisons,
            swaps = swaps,
            description = "Sorted!",
            isComplete = true
        ))
    }
}
