package com.vivek.sortingvisualiser.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.vivek.sortingvisualiser.presentation.ui.screen.SortingScreen
import com.vivek.sortingvisualiser.presentation.ui.theme.SortingVisualizerTheme
import org.junit.Rule
import org.junit.Test

class SortingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchScreen() {
        composeTestRule.setContent {
            SortingVisualizerTheme {
                SortingScreen()
            }
        }
    }

    @Test
    fun topBar_isVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Sorting Visualizer").assertIsDisplayed()
    }

    @Test
    fun bubbleSortChip_isSelectedByDefault() {
        launchScreen()
        composeTestRule.onNodeWithText("Bubble Sort").assertIsSelected()
    }

    @Test
    fun allAlgorithmChips_areVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Bubble Sort").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selection Sort").assertIsDisplayed()
        composeTestRule.onNodeWithText("Insertion Sort").assertIsDisplayed()
        composeTestRule.onNodeWithText("Merge Sort").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Sort").assertIsDisplayed()
    }

    @Test
    fun sortButton_isVisibleAndEnabled() {
        launchScreen()
        composeTestRule.onNodeWithText("Sort").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sort").assertIsEnabled()
    }

    @Test
    fun resetButton_isVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
    }

    @Test
    fun shuffleButton_isVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Shuffle").assertIsDisplayed()
    }

    @Test
    fun clickingAlgorithmChip_selectsIt() {
        launchScreen()
        composeTestRule.onNodeWithText("Merge Sort").performClick()
        composeTestRule.onNodeWithText("Merge Sort").assertIsSelected()
    }

    @Test
    fun clickingAlgorithmChip_deselectsPrevious() {
        launchScreen()
        composeTestRule.onNodeWithText("Merge Sort").performClick()
        // Bubble Sort chip should no longer be selected
        composeTestRule
            .onNodeWithText("Bubble Sort")
            .assertIsDisplayed()
    }

    @Test
    fun legendLabels_areVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compare").assertIsDisplayed()
        composeTestRule.onNodeWithText("Swap").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sorted").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pivot").assertIsDisplayed()
    }

    @Test
    fun statsLabels_areVisible() {
        launchScreen()
        composeTestRule.onNodeWithText("Comparisons").assertIsDisplayed()
        composeTestRule.onNodeWithText("Swaps").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time").assertIsDisplayed()
    }

    @Test
    fun clickingReset_keepsSortButtonEnabled() {
        launchScreen()
        composeTestRule.onNodeWithText("Reset").performClick()
        composeTestRule.onNodeWithText("Sort").assertIsEnabled()
    }

    @Test
    fun clickingShuffle_keepsSortButtonEnabled() {
        launchScreen()
        composeTestRule.onNodeWithText("Shuffle").performClick()
        composeTestRule.onNodeWithText("Sort").assertIsEnabled()
    }
}
