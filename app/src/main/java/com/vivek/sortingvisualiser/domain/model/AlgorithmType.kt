package com.vivek.sortingvisualiser.domain.model

enum class AlgorithmType(
    val displayName: String,
    val timeComplexity: String,
    val spaceComplexity: String,
    val description: String
) {
    BUBBLE_SORT(
        displayName = "Bubble Sort",
        timeComplexity = "O(n²)",
        spaceComplexity = "O(1)",
        description = "Repeatedly compares adjacent elements and swaps if out of order"
    ),
    SELECTION_SORT(
        displayName = "Selection Sort",
        timeComplexity = "O(n²)",
        spaceComplexity = "O(1)",
        description = "Finds the minimum in the unsorted portion and places it at the front"
    ),
    INSERTION_SORT(
        displayName = "Insertion Sort",
        timeComplexity = "O(n²)",
        spaceComplexity = "O(1)",
        description = "Builds the sorted array one element at a time by inserting into position"
    ),
    MERGE_SORT(
        displayName = "Merge Sort",
        timeComplexity = "O(n log n)",
        spaceComplexity = "O(n)",
        description = "Divides the array in half, recursively sorts, then merges"
    ),
    QUICK_SORT(
        displayName = "Quick Sort",
        timeComplexity = "O(n log n)",
        spaceComplexity = "O(log n)",
        description = "Selects a pivot and partitions elements around it recursively"
    )
}
