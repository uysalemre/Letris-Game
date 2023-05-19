package com.eu.letris.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eu.letris.data.scores.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(private val scoresRepository: ScoresRepository) :
    ViewModel() {

    private val _scoresState: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())
    val scoresState get() = _scoresState.asStateFlow()

    init {
        viewModelScope.launch {
            scoresRepository.getTopPointList().collect { scores ->
                _scoresState.value = scores
            }
        }
    }
}
