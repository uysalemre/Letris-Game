package com.eu.letris.ui.model

data class SelectedLetterModel(
    val letter: Char,
    val point: Int,
    val columnIndex: Int,
    val itemIndex: Int
)