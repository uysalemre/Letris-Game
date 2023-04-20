package com.eu.letris.ui.model

import androidx.compose.ui.graphics.Color

data class Letter(
    val character: Char,
    val backgroundColor: Color,
    val point: Int,
    var isSelected: Boolean = false
)

data class SelectedLetter(
    val letter: Letter,
    val columnIndex : Int,
    val itemIndex : Int
)

