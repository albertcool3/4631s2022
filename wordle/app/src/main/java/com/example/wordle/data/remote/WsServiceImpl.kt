package com.example.wordle.data.remote

import com.example.wordle.data.remote.dto.WordleWordDto
import com.example.wordle.domain.model.WordleWord
import com.example.wordle.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class wsServiceImpl(
    private val client: HttpClient
) : WsService{

    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${WsService.Endpoints.WordleSocket.url}?username=$username")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error("Could not establish a connection")
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun sendWords(word: String) {
        try {
            socket?.send(Frame.Text(word))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeWords(): Flow<WordleWord> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText()?:""
                    val wordleWordDto = Json.decodeFromString<WordleWordDto>(json)
                    wordleWordDto.toWordleWord()
                } ?: flow { } //return an empty flow if null
        } catch(e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}