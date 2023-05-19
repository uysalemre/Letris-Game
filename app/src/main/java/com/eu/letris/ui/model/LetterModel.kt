package com.eu.letris.ui.model

data class LetterModel(
    val character: Char,
    val backgroundColor: Long,
    val point: Int,
    var isSelected: Boolean = false
)