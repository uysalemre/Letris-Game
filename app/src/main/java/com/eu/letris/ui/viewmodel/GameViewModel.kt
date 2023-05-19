package com.eu.letris.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eu.letris.data.scores.ScoresRepository
import com.eu.letris.data.words.WordsRepository
import com.eu.letris.ui.model.LetterModel
import com.eu.letris.ui.model.SelectedLetterModel
import com.eu.letris.ui.state.GameUiState
import com.eu.letris.ui.state.SelectedLetterUiState
import com.eu.letris.ui.theme.*
import com.eu.letris.ui.util.calculatePoint
import com.eu.letris.ui.util.toWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val wordsRepository: WordsRepository,
    private val scoresRepository: ScoresRepository
) : ViewModel() {

    private val vowelLetterList = listOf(
        LetterModel(character = 'A', 0xFF9EA312, 3),
        LetterModel(character = 'E', 0xFFA24106, 3),
        LetterModel(character = 'I', 0xFFE0CE6B, 5),
        LetterModel(character = 'İ', 0xFFFF6F5A, 3),
        LetterModel(character = 'O', 0xFF340579, 5),
        LetterModel(character = 'Ö', 0xFF778D60, 10),
        LetterModel(character = 'U', 0xFF0B9C7C, 5),
        LetterModel(character = 'Ü', 0xFF003E92, 7)
    )
    private val normalLetterList = listOf(
        LetterModel(character = 'B', 0xFFc63637, 6),
        LetterModel(character = 'C', 0xFF42ab49, 7),
        LetterModel(character = 'Ç', 0xFF5086c1, 7),
        LetterModel(character = 'D', 0xFF75252F, 6),
        LetterModel(character = 'F', 0xFFc999af, 10),
        LetterModel(character = 'G', 0xFF8f7193, 8),
        LetterModel(character = 'Ğ', 0xFFF34263, 10),
        LetterModel(character = 'H', 0xFF648080, 8),
        LetterModel(character = 'J', 0xFFC59C1B, 12),
        LetterModel(character = 'K', 0xFF7A6340, 3),
        LetterModel(character = 'L', 0xFF6559AD, 3),
        LetterModel(character = 'M', 0xFFB95892, 5),
        LetterModel(character = 'N', 0xFF012E68, 3),
        LetterModel(character = 'P', 0xFF494303, 8),
        LetterModel(character = 'R', 0xFF435D68, 4),
        LetterModel(character = 'S', 0xFF47332A, 5),
        LetterModel(character = 'Ş', 0xFF2D3623, 7),
        LetterModel(character = 'T', 0xFF641903, 4),
        LetterModel(character = 'V', 0xFF464774, 10),
        LetterModel(character = 'Y', 0xFF416F50, 6),
        LetterModel(character = 'Z', 0xFF614D72, 7)
    )
    private val errorLetter = LetterModel(character = 'X', 0xFF000000, 0)

    private var tickerSender: ReceiveChannel<Unit>? = null

    private val _gamingState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val gamingState get() = _gamingState.asStateFlow()

    private val _selectedLetterState: MutableStateFlow<SelectedLetterUiState> =
        MutableStateFlow(SelectedLetterUiState())
    val selectedLetterState get() = _selectedLetterState.asStateFlow()

    init {
        generateFirstThreeLines()
        setTickerSender(3000)
    }

    fun endGame() {
        tickerSender?.cancel()
        tickerSender = null
    }

    private fun generateFirstThreeLines() {
        viewModelScope.launch {
            _gamingState.update { it ->
                it.copy(
                    data = vowelLetterList.shuffled().take(5).plus(
                        normalLetterList.shuffled().take(19)
                    ).shuffled().chunked(3).map { list -> list.toCollection(SnapshotStateList()) }
                )
            }
        }
    }

    fun selectUnselectItem(listIndex: Int, itemIndex: Int) {
        viewModelScope.launch {
            val columnList = _gamingState.value.data
            val selectedCharacterList = _selectedLetterState.value.data.toMutableList()
            val column = columnList[listIndex]
            column[itemIndex] = column[itemIndex].copy(isSelected = !column[itemIndex].isSelected)
            when (column[itemIndex].isSelected) {
                true -> selectedCharacterList.add(
                    SelectedLetterModel(
                        letter = column[itemIndex].character,
                        point = column[itemIndex].point,
                        columnIndex = listIndex,
                        itemIndex = itemIndex
                    )
                )
                false -> selectedCharacterList.remove(selectedCharacterList.find { it.itemIndex == itemIndex && it.columnIndex == listIndex })
            }
            _gamingState.update {
                it.copy(data = columnList)
            }
            _selectedLetterState.update {
                it.copy(data = selectedCharacterList)
            }
        }
    }

    fun unSelectAllItems() {
        viewModelScope.launch {
            val columnList = _gamingState.value.data
            columnList.map {
                it.map { letter ->
                    letter.isSelected = false
                }
            }
            _gamingState.update {
                it.copy(data = columnList)
            }
            _selectedLetterState.update {
                it.copy(data = listOf())
            }
        }
    }

    fun checkWordFromDb() {
        viewModelScope.launch {
            wordsRepository.checkWordIsExists(_selectedLetterState.value.data.toWord())
                .collect {
                    val columnList = _gamingState.value.data
                    if (it != null) {
                        val totalPoints =
                            _gamingState.value.totalPoints + _selectedLetterState.value.data.calculatePoint()
                        columnList.map { letters ->
                            letters.removeIf { it.isSelected }
                        }
                        _gamingState.update { state ->
                            state.copy(
                                totalPoints = totalPoints,
                                data = columnList
                            )
                        }
                        _selectedLetterState.update { state ->
                            state.copy(
                                data = mutableListOf()
                            )
                        }
                    } else {
                        unSelectAllItems()
                        if (_gamingState.value.errorCount >= 2) {
                            columnList.map { letters ->
                                letters.add(errorLetter)
                            }
                            _gamingState.update { state ->
                                state.copy(
                                    data = columnList,
                                    errorCount = 0
                                )
                            }
                        } else {
                            _gamingState.update { state ->
                                state.copy(
                                    errorCount = state.errorCount + 1
                                )
                            }
                        }
                    }
                }
        }
    }

    fun addPointsToDb() {
        if (_gamingState.value.totalPoints > 0) {
            viewModelScope.launch {
                scoresRepository.addPoint(_gamingState.value.totalPoints).collect()
            }
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    fun setTickerSender(delayMillis: Long) {
        viewModelScope.launch {
            tickerSender = ticker(delayMillis = delayMillis, context = this.coroutineContext)
            tickerSender?.consumeEach {
                val columnList = _gamingState.value.data
                columnList.random().add(
                    when {
                        (0..1).random() == 0 -> vowelLetterList.random()
                        else -> normalLetterList.random()
                    }
                )
                _gamingState.update {
                    it.copy(
                        data = columnList,
                        isGameFinished = when {
                            columnList.any { column -> column.size >= 10 } -> {
                                tickerSender?.cancel()
                                true
                            }
                            else -> false
                        }
                    )
                }
                if (columnList.any { it.size >= 10 }) {
                    tickerSender?.cancel()
                    _gamingState.update {
                        it.copy(isGameFinished = true)
                    }
                }
            }
        }
    }
}