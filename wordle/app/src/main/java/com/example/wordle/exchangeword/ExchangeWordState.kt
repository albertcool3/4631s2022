package com.example.wordle.exchangeword

import com.example.wordle.domain.model.WordleWord
import java.text.DateFormat
import java.util.*

data class ExchangeWordState(
    val wordleWords: List<WordleWord> = emptyList(),
    val wordleWordOfTheDay: WordleWord = WordleWord("CRANE", DateFormat
        .getDateInstance(DateFormat.DEFAULT)
        .format(Date(0)),
        "Wordle Bot"),
    val isLoading: Boolean = false
)