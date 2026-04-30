# Sorting Visualizer

An Android application that animates five classic sorting algorithms in real time, built entirely with **Jetpack Compose** and **Compose Canvas**. Watch bars change colour as elements are compared, swapped, and finally sorted — at a speed you control.

---

## Screenshots

> Run the app and explore — every algorithm produces its own distinct visual pattern.

---

## Features

| Feature | Detail |
|---|---|
| **5 algorithms** | Bubble, Selection, Insertion, Merge, Quick Sort |
| **Live canvas animation** | Bars drawn each frame via `Canvas` composable |
| **Colour-coded states** | Default · Comparing · Swapping · Sorted · Pivot |
| **Speed control** | Slider from 10 ms/step to 500 ms/step |
| **Array size control** | 10 → 80 elements |
| **Shuffle / Reset** | Regenerate a random permutation or reset to the same one |
| **Pause / Resume** | Suspend mid-sort and continue from the same point |
| **Live statistics** | Comparison count, swap count, elapsed time |
| **Algorithm info** | Time & space complexity shown for the selected algorithm |
| **Dark mode** | Follows system theme via Material 3 dynamic colour |

---

## System Design

### High-Level Architecture

The project follows **Clean Architecture** with **MVVM** in the presentation layer, enforcing a strict dependency rule: outer layers depend inward, never outward.

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│   SortingScreen ──► SortingViewModel                │
│   (Compose UI)       (StateFlow<UiState>)            │
└────────────────────────┬────────────────────────────┘
                         │ collects Flow<SortingStep>
┌────────────────────────▼────────────────────────────┐
│                    Domain Layer                      │
│   SortingAlgorithm (interface)                       │
│   BubbleSort · SelectionSort · InsertionSort         │
│   MergeSort · QuickSort                              │
└─────────────────────────────────────────────────────┘
```

### Package Structure

```
com.vivek.sortingvisualiser/
├── domain/
│   ├── model/
│   │   ├── AlgorithmType.kt      ← enum with display name & complexity
│   │   ├── ArrayElement.kt       ← value + ElementState
│   │   ├── ElementState.kt       ← DEFAULT | COMPARING | SWAPPING | SORTED | PIVOT
│   │   └── SortingStep.kt        ← snapshot emitted by each algorithm
│   └── algorithm/
│       ├── SortingAlgorithm.kt   ← interface: sort(IntArray): Flow<SortingStep>
│       ├── BubbleSort.kt
│       ├── SelectionSort.kt
│       ├── InsertionSort.kt
│       ├── MergeSort.kt
│       └── QuickSort.kt
└── presentation/
    ├── model/
    │   └── SortingUiState.kt     ← all UI data in one immutable data class
    ├── viewmodel/
    │   └── SortingViewModel.kt   ← collects algorithm flow, drives StateFlow
    └── ui/
        ├── theme/
        │   ├── Color.kt          ← Material 3 + bar-state colours
        │   ├── Theme.kt
        │   └── Type.kt
        ├── components/
        │   ├── SortingCanvas.kt  ← Canvas composable, draws bars
        │   ├── AlgorithmSelector.kt ← LazyRow of FilterChips
        │   ├── ControlPanel.kt   ← Sort/Pause/Reset/Shuffle + sliders
        │   ├── StatsPanel.kt     ← comparisons, swaps, time, description
        │   └── Legend.kt         ← colour legend row
        └── screen/
            └── SortingScreen.kt  ← root screen, assembles all components
```

---

### Data Flow

```
User interaction
      │
      ▼
SortingViewModel
      │  calls createAlgorithm().sort(inputArray)
      │  which returns a cold Flow<SortingStep>
      ▼
Algorithm (e.g. BubbleSort)
      │  emits one SortingStep per visual event
      │  (comparison, swap, placement, completion)
      ▼
ViewModel.collect { step →
      delay(speedToDelayMs)       ← controls animation speed
      _uiState.update { ... }     ← updates StateFlow
}
      │
      ▼
SortingScreen (collectAsStateWithLifecycle)
      │
      ▼
SortingCanvas.Canvas { … drawRoundRect per element … }
```

Each `SortingStep` is a **pure value** — an immutable snapshot of the array at a single moment in the algorithm, together with the states of every element (which bar colour to use) and running stats.

---

### Key Design Decisions

| Decision | Rationale |
|---|---|
| `Flow<SortingStep>` per algorithm | Cold flow → no resources wasted until collected; cancellable; testable with `flow.toList()` in `runTest` |
| `StateFlow<SortingUiState>` in ViewModel | Single source of truth; `collectAsStateWithLifecycle` ensures no updates while the screen is in background |
| Immutable `SortingStep` & `ArrayElement` | Compose state comparison works correctly; no shared mutable state between coroutine and UI thread |
| `Canvas` composable for drawing | Full pixel-level control; no custom View subclassing; runs in the Compose rendering pipeline |
| No DI framework | Keeps the project self-contained; the ViewModel creates algorithm instances itself — easy to swap in tests by directly instantiating the class under test |
| Pause via `while (isPaused) delay(50)` | Cooperative suspension inside the collector; pausing does not cancel the underlying flow |

---

### Sorting Algorithms

| Algorithm | Best | Average | Worst | Space |
|---|---|---|---|---|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) |

Each algorithm emits coloured visual states:

- **Orange** — two elements being compared
- **Red** — elements being swapped / shifted
- **Purple** — the current pivot (Quick Sort only)
- **Green** — element confirmed in its final sorted position
- **Blue** — untouched element

---

### Animation Speed

```
Speed slider value (1–10) → delay per step
Speed 1  → 460 ms   (very slow, educational)
Speed 5  → 260 ms   (medium)
Speed 10 →  10 ms   (fast, 100 steps/s)

formula: delayMs = 510 - (speed × 50), floored at 10 ms
```

---

## Tech Stack

| Component | Library / Version |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose (BOM 2024.09.00) |
| Design system | Material 3 |
| Architecture | MVVM + Clean Architecture |
| Async | Kotlin Coroutines 1.8.1 + Flow |
| State | `StateFlow` / `collectAsStateWithLifecycle` |
| Build | Android Gradle Plugin 8.5.2 · Gradle 8.9 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 (Android 15) |

---

## Testing

The project follows **TDD** — tests were written to specify algorithm behaviour before implementation was complete.

### Unit Tests (`src/test`)

| Test class | Coverage |
|---|---|
| `BubbleSortTest` | 11 cases — correctness, state emission, immutability, edge cases |
| `SelectionSortTest` | 8 cases — correctness, state emission, duplicates, single element |
| `InsertionSortTest` | 8 cases — correctness, already-sorted optimisation, edge cases |
| `MergeSortTest` | 9 cases — correctness, COMPARING state, two-element, duplicates |
| `QuickSortTest` | 9 cases — correctness, PIVOT state, worst-case input, duplicates |
| `SortingViewModelTest` | 22 cases — initial state, all state mutations, guard conditions |

All algorithm tests use `kotlinx-coroutines-test` `runTest` + `flow.toList()` to collect the full step sequence without real delays.

### Instrumented Tests (`src/androidTest`)

`SortingScreenTest` — 12 Compose UI tests covering:
- Top bar visibility
- Default chip selection
- All algorithm chips visible and clickable
- All control buttons present and enabled/disabled correctly
- Legend and stats labels visible

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected device / emulator)
./gradlew connectedAndroidTest
```

---

## Build & Run

```bash
git clone https://github.com/vivekpanchal/sortingvisualiser.git
cd sortingvisualiser
./gradlew assembleDebug
# or open in Android Studio and click Run
```

Requires **Android Studio Ladybug** (2024.2+) or later with Kotlin 2.0 plugin support.

---

## Project Structure at a Glance

```
SortingVisualiser/
├── app/
│   ├── build.gradle
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/vivek/sortingvisualiser/
│       │   │   ├── MainActivity.kt
│       │   │   ├── domain/
│       │   │   │   ├── model/          (4 files)
│       │   │   │   └── algorithm/      (6 files)
│       │   │   └── presentation/
│       │   │       ├── model/          (1 file)
│       │   │       ├── viewmodel/      (1 file)
│       │   │       └── ui/
│       │   │           ├── theme/      (3 files)
│       │   │           ├── components/ (5 files)
│       │   │           └── screen/     (1 file)
│       │   └── res/values/
│       ├── test/                       (6 unit test files)
│       └── androidTest/                (2 instrumented test files)
├── build.gradle
└── settings.gradle
```
