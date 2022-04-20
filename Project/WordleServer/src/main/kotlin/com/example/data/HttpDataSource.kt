package com.example.data

import com.example.data.model.WordleWord

interface HttpDataSource {

    suspend fun getAllWords(): List<WordleWord>
    suspend fun getWordOfTheDay(): WordleWord
    suspend fun insertWord(word: WordleWord)
}