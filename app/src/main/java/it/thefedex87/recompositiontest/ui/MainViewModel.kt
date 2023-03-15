package it.thefedex87.recompositiontest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var state by mutableStateOf(
        MainScreenState(
            values = listOf(
                MyObject(text = "Text1", value = 1, isFavorite = false),
                MyObject(text = "Text2", value = 2, isFavorite = true)
            ),
            color = Color.Red
        )
    )
        private set


    fun increment() {
        state = state.copy(
            value = state.value + 1
        )
    }

    fun decrement() {
        state = state.copy(
            value = state.value - 1
        )
    }

    fun changeOption(option: Int) {
        state = state.copy(
            selectedOption = option
        )
        /*viewModelScope.launch {
            delay(100)
            state = state.copy(
                values = listOf(
                    MyObject(text = "Text1", value = if(state.values.first().value == 1) 2 else 1, isFavorite = false),
                    MyObject(text = "Text2", value = 2, isFavorite = true)
                ),
            )
        }*/
    }

    fun selectedValueChanged(selectedValue: MyObject) {
        state = state.copy(
            selectedValue = selectedValue
        )
    }

    fun changeColor(color: Color) {
        state = state.copy(
            color = color
        )
    }
}