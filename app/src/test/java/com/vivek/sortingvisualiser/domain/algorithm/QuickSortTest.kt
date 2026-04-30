package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ElementState
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuickSortTest {

    private val sut = QuickSort()

    @Test
    fun `sorts a random array into ascending order`() = runTest {
        val steps = sut.sort(intArrayOf(10, 7, 8, 9, 1, 5)).toList()
        assertEquals(listOf(1, 5, 7, 8, 9, 10), steps.last().elements.map { it.value })
    }

    @Test
    fun `final step marks every element as SORTED`() = runTest {
        val steps = sut.sort(intArrayOf(5, 3, 1, 4, 2)).toList()
        assertTrue(steps.last().elements.all { it.state == ElementState.SORTED })
    }

    @Test
    fun `final step has isComplete = true`() = runTest {
        val steps = sut.sort(intArrayOf(5, 3, 1)).toList()
        assertTrue(steps.last().isComplete)
    }

    @Test
    fun `handles already sorted array`() = runTest {
        val steps = sut.sort(intArrayOf(1, 2, 3, 4, 5)).toList()
        assertEquals(listOf(1, 2, 3, 4, 5), steps.last().elements.map { it.value })
    }

    @Test
    fun `handles reverse sorted array`() = runTest {
        val steps = sut.sort(intArrayOf(5, 4, 3, 2, 1)).toList()
        assertEquals(listOf(1, 2, 3, 4, 5), steps.last().elements.map { it.value })
    }

    @Test
    fun `emits PIVOT state`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 4, 2)).toList()
        assertTrue(steps.any { step -> step.elements.any { it.state == ElementState.PIVOT } })
    }

    @Test
    fun `handles single element`() = runTest {
        val steps = sut.sort(intArrayOf(42)).toList()
        assertEquals(42, steps.last().elements.first().value)
    }

    @Test
    fun `handles duplicate values`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 3, 1, 2)).toList()
        assertEquals(listOf(1, 1, 2, 3, 3), steps.last().elements.map { it.value })
    }

    @Test
    fun `input array is not mutated`() = runTest {
        val input = intArrayOf(5, 3, 1)
        val original = input.copyOf()
        sut.sort(input).toList()
        assertTrue(input.contentEquals(original))
    }
}
