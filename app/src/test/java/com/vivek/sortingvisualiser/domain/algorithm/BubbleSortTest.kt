package com.vivek.sortingvisualiser.domain.algorithm

import com.vivek.sortingvisualiser.domain.model.ElementState
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BubbleSortTest {

    private val sut = BubbleSort()

    @Test
    fun `sorts a random array into ascending order`() = runTest {
        val steps = sut.sort(intArrayOf(5, 3, 1, 4, 2)).toList()
        assertEquals(listOf(1, 2, 3, 4, 5), steps.last().elements.map { it.value })
    }

    @Test
    fun `final step marks every element as SORTED`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 2)).toList()
        assertTrue(steps.last().elements.all { it.state == ElementState.SORTED })
    }

    @Test
    fun `final step has isComplete = true`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 2)).toList()
        assertTrue(steps.last().isComplete)
    }

    @Test
    fun `does not swap elements in an already sorted array`() = runTest {
        val steps = sut.sort(intArrayOf(1, 2, 3, 4, 5)).toList()
        assertEquals(0, steps.last().swaps)
    }

    @Test
    fun `handles a single-element array`() = runTest {
        val steps = sut.sort(intArrayOf(42)).toList()
        assertEquals(42, steps.last().elements.first().value)
    }

    @Test
    fun `handles a two-element reversed array`() = runTest {
        val steps = sut.sort(intArrayOf(2, 1)).toList()
        assertEquals(listOf(1, 2), steps.last().elements.map { it.value })
    }

    @Test
    fun `emits COMPARING state during a comparison`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 2)).toList()
        assertTrue(steps.any { step -> step.elements.any { it.state == ElementState.COMPARING } })
    }

    @Test
    fun `emits SWAPPING state when a swap occurs`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 2)).toList()
        assertTrue(steps.any { step -> step.elements.any { it.state == ElementState.SWAPPING } })
    }

    @Test
    fun `comparisons count is positive for unsorted input`() = runTest {
        val steps = sut.sort(intArrayOf(3, 2, 1)).toList()
        assertTrue(steps.last().comparisons > 0)
    }

    @Test
    fun `handles duplicate values correctly`() = runTest {
        val steps = sut.sort(intArrayOf(3, 1, 3, 1, 2)).toList()
        assertEquals(listOf(1, 1, 2, 3, 3), steps.last().elements.map { it.value })
    }

    @Test
    fun `input array is not mutated`() = runTest {
        val input = intArrayOf(5, 3, 1, 4, 2)
        val original = input.copyOf()
        sut.sort(input).toList()
        assertTrue(input.contentEquals(original))
    }
}
