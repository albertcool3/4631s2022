package com.example.wordle.exchangeword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.remote.HttpService
import com.example.wordle.data.remote.WsService
import com.example.wordle.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.wordle.username
import com.example.wordle.wordOfTheDay

@HiltViewModel
class ExchangeWordViewModel @Inject constructor(
    private val httpService: HttpService,
    private val wsService: WsService,
    private val savedStatehandle: SavedStateHandle
) : ViewModel() {
    private val _wordText = mutableStateOf("")
    val wordText: State<String> = _wordText

    private val _state = mutableStateOf(ExchangeWordState())
    val state: State<ExchangeWordState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat() {
        getAllWordleWords()
        getWordOfTheDay()
        //savedStatehandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                val result = wsService.initSession(username)
                when(result) {
                    is Resource.Success -> {
                        wsService.observeWords()
                            .onEach { message ->
                                val newList = state.value.wordleWords.toMutableList().apply {
                                    add(0, message)
                                }
                                _state.value = state.value.copy(
                                    wordleWords = newList
                                )
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit(result.message?: "Unkown error")
                    }
                }
            }
       // }
    }

    fun onWordChange(input: String) {
        _wordText.value = input.uppercase()
    }

    fun disconnect() {
        viewModelScope.launch {
            wsService.closeSession()
        }
    }

    fun getAllWordleWords() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = httpService.getAllWords()
            _state.value = state.value.copy(
                wordleWords = result,
                isLoading = false
            )
        }
    }
    fun getWordOfTheDay() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = httpService.getWordOfTheDay()
            _state.value = state.value.copy(
                wordleWordOfTheDay = result,
                isLoading = false
            )
            wordOfTheDay = result.text
        }
    }

    fun sendWord() {
        viewModelScope.launch {
            if(wordText.value.isNotBlank()) {
                wsService.sendWords(wordText.value)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}