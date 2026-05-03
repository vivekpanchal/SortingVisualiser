package com.vivek.sortingvisualiser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vivek.sortingvisualiser.presentation.ui.screen.SortingScreen
import com.vivek.sortingvisualiser.presentation.ui.theme.SortingVisualizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SortingVisualizerTheme {
                SortingScreen()
            }
        }
    }
}
