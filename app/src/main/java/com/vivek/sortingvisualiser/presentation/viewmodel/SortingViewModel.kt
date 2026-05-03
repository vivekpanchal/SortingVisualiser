package com.vivek.sortingvisualiser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.sortingvisualiser.domain.algorithm.BubbleSort
import com.vivek.sortingvisualiser.domain.algorithm.InsertionSort
import com.vivek.sortingvisualiser.domain.algorithm.MergeSort
import com.vivek.sortingvisualiser.domain.algorithm.QuickSort
import com.vivek.sortingvisualiser.domain.algorithm.SelectionSort
import com.vivek.sortingvisualiser.domain.algorithm.SortingAlgorithm
import com.vivek.sortingvisualiser.domain.model.AlgorithmType
import com.vivek.sortingvisualiser.presentation.model.SortingUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SortingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SortingUiState())
    val uiState: StateFlow<SortingUiState> = _uiState.asStateFlow()

    private var sortingJob: Job? = null
    private var startTime: Long = 0L

    fun setAlgorithm(algorithm: AlgorithmType) {
        if (_uiState.value.isRunning) return
        _uiState.update { it.copy(selectedAlgorithm = algorithm) }
    }

    fun setSpeed(speed: Float) {
        _uiState.update { it.copy(speed = speed) }
    }

    fun setArraySize(size: Int) {
        if (_uiState.value.isRunning) return
        _uiState.update {
            it.copy(
                arraySize = size,
                elements = SortingUiState.generateElements(size),
                comparisons = 0,
                swaps = 0,
                elapsedTimeMs = 0L,
                isComplete = false,
                currentDescription = ""
            )
        }
    }

    fun shuffle() {
        if (_uiState.value.isRunning) return
        sortingJob?.cancel()
        val size = _uiState.value.arraySize
        _uiState.update {
            it.copy(
                elements = SortingUiState.generateElements(size),
                comparisons = 0,
                swaps = 0,
                elapsedTimeMs = 0L,
                isComplete = false,
                isRunning = false,
                isPaused = false,
                currentDescription = ""
            )
        }
    }

    fun startSort() {
        if (_uiState.value.isRunning || _uiState.value.isComplete) return

        val algorithm = createAlgorithm(_uiState.value.selectedAlgorithm)
        val inputArray = _uiState.value.elements.map { it.value }.toIntArray()

        _uiState.update { it.copy(isRunning = true, isPaused = false) }
        startTime = System.currentTimeMillis()

        sortingJob = viewModelScope.launch {
            algorithm.sort(inputArray).collect { step ->
                while (_uiState.value.isPaused && isActive) {
                    delay(50)
                }
                if (!isActive) return@collect

                val delayMs = speedToDelayMs(_uiState.value.speed)
                delay(delayMs)

                _uiState.update {
                    it.copy(
                        elements = step.elements,
                        comparisons = step.comparisons,
                        swaps = step.swaps,
                        elapsedTimeMs = System.currentTimeMillis() - startTime,
                        currentDescription = step.description,
                        isComplete = step.isComplete
                    )
                }
            }
            _uiState.update { it.copy(isRunning = false) }
        }
    }

    fun pauseResume() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun reset() {
        sortingJob?.cancel()
        val size = _uiState.value.arraySize
        _uiState.update {
            it.copy(
                elements = SortingUiState.generateElements(size),
                isRunning = false,
                isPaused = false,
                isComplete = false,
                comparisons = 0,
                swaps = 0,
                elapsedTimeMs = 0L,
                currentDescription = ""
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        sortingJob?.cancel()
    }

    private fun createAlgorithm(type: AlgorithmType): SortingAlgorithm = when (type) {
        AlgorithmType.BUBBLE_SORT -> BubbleSort()
        AlgorithmType.SELECTION_SORT -> SelectionSort()
        AlgorithmType.INSERTION_SORT -> InsertionSort()
        AlgorithmType.MERGE_SORT -> MergeSort()
        AlgorithmType.QUICK_SORT -> QuickSort()
    }

    // Speed 1 = 500ms delay (slowest), Speed 10 = 10ms delay (fastest)
    private fun speedToDelayMs(speed: Float): Long =
        (510L - (speed.toLong() * 50L)).coerceAtLeast(10L)
}
