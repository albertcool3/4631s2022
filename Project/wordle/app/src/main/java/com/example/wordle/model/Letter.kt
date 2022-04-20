package com.example.wordle.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class LetterStatus {
    Unknown, ExactPosition, WrongPosition, NotThere
}

class Letter(name: String, status: LetterStatus) {
    var status by mutableStateOf(status)
    var name by mutableStateOf(name)
}