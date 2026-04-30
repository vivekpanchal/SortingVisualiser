package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.SortingStep
import kotlinx.coroutines.flow.Flow

interface SortingAlgorithm {
    fun sort(array: IntArray): Flow<SortingStep>
}
