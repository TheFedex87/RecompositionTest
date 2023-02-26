package it.thefedex87.recompositiontest.ui

import androidx.compose.ui.graphics.Color

data class MainScreenState(
    val value: Int = 0,
    val values: List<MyObject> = emptyList(),
    val color: Color,
    val selectedOption: Int = 0
)