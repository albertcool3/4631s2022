package com.example.wordle.data.remote

import com.example.wordle.domain.model.WordleWord
import com.example.wordle.servername

interface HttpService {
    suspend fun getAllWords(): List<WordleWord>
    suspend fun getWordOfTheDay(): WordleWord

    companion object {
        val BASE_URL = "http://$servername:8080"
    }

    sealed class Endpoints(val url:String) {
        object GetAllWords: Endpoints("$BASE_URL/words")
        object GetWordOfTheDay: Endpoints("$BASE_URL/wordoftheday")
    }
}