package com.example.wordle

import com.example.data.HttpDataSource
import com.example.data.model.WordleWord
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class WordleController(
    private val wordDataSource: HttpDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if(members.containsKey(username)) {
            throw MemberAlreadyExistsException()
        }
        members[username] = Member(
            username = username,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendWord(senderUsername: String, word: String) {
        var errorString = ""
        lateinit var wordleWord : WordleWord
        if (word.length != 5) {
            errorString = "{word} length does not meet the length of 5 requirement"
        }
        if (errorString.length != 0) {
            wordleWord = WordleWord(
                text = errorString,
                username = "wordleBot",
                timestamp = System.currentTimeMillis()
            )
        } else {
            wordleWord = WordleWord(
                text = word,
                username = senderUsername,
                timestamp = System.currentTimeMillis()
            )
            wordDataSource.insertWord(wordleWord)
        }
        members.values.forEach { member ->
/*                val wordleWord = WordleWord(
                    text = word,
                    username = senderUsername,
                    timestamp = System.currentTimeMillis()
                )
                httpDataSource.insertWord(wordleWord)*/

            val parsedWord = Json.encodeToString(wordleWord)
            member.socket.send(Frame.Text(parsedWord))
        }
    }

    suspend fun getAllWords(): List<WordleWord> {
        return wordDataSource.getAllWords()
    }

    suspend fun getWordOfTheDay(): WordleWord {
        return wordDataSource.getWordOfTheDay()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if(members.containsKey(username)) {
            members.remove(username)
        }
    }
}