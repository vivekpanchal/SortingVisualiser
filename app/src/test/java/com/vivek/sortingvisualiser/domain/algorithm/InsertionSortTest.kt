package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ElementState
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InsertionSortTest {

    private val sut = InsertionSort()

    @Test
    fun `sorts a random array into ascending order`() = runTest {
        val steps = sut.sort(intArrayOf(12, 11, 13, 5, 6)).toList()
        assertEquals(listOf(5, 6, 11, 12, 13), steps.last().elements.map { it.value })
    }

    @Test
    fun `final step marks every element as SORTED`() = runTest {
        val steps = sut.sort(intArrayOf(5, 2, 4)).toList()
        assertTrue(steps.last().elements.all { it.state == ElementState.SORTED })
    }

    @Test
    fun `final step has isComplete = true`() = runTest {
        val steps = sut.sort(intArrayOf(5, 2, 4)).toList()
        assertTrue(steps.last().isComplete)
    }

    @Test
    fun `no swaps for already sorted array`() = runTest {
        val steps = sut.sort(intArrayOf(1, 2, 3, 4, 5)).toList()
        assertEquals(0, steps.last().swaps)
    }

    @Test
    fun `handles reversed array`() = runTest {
        val steps = sut.sort(intArrayOf(5, 4, 3, 2, 1)).toList()
        assertEquals(listOf(1, 2, 3, 4, 5), steps.last().elements.map { it.value })
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
        val input = intArrayOf(3, 1, 2)
        val original = input.copyOf()
        sut.sort(input).toList()
        assertTrue(input.contentEquals(original))
    }
}
