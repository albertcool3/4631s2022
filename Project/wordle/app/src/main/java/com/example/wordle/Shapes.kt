package com.example.wordle


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color


@Composable
fun Circle(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        drawCircle(color)
    }
}

@Composable
fun Rectangle(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        drawRect(size = Size(width = size.width, height = size.height), topLeft = Offset(0f, 0f), color = color)
    }
}

