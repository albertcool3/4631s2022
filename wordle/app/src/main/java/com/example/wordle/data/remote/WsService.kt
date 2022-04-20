package com.example.wordle.data.remote

import com.example.wordle.domain.model.WordleWord
import com.example.wordle.servername
import com.example.wordle.util.Resource
import kotlinx.coroutines.flow.Flow


interface WsService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendWords(word: String)

    fun observeWords(): Flow<WordleWord>

    suspend fun closeSession()

    companion object {

        val BASE_URL = "ws://$servername:8080"
    }

    sealed class Endpoints(val url:String) {
        object WordleSocket: Endpoints("$BASE_URL/wordle-socket")
    }

}