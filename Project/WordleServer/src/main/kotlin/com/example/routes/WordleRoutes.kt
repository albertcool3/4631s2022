package com.example.routes

import com.example.wordle.MemberAlreadyExistsException
import com.example.wordle.WordleController
import com.example.session.WordleSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.wordleSocket(wordleController: WordleController) {
    webSocket("/wordle-socket") {
        val session = call.sessions.get<WordleSession>()
        if(session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            wordleController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )
            incoming.consumeEach { frame ->
                if(frame is Frame.Text) {
                    wordleController.sendWord(
                        senderUsername = session.username,
                        word = frame.readText()
                    )
                }
            }
        } catch(e: MemberAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            wordleController.tryDisconnect(session.username)
        }
    }
}

fun Route.getAllWords(wordleController: WordleController) {
    get("/words") {
        call.respond(
            HttpStatusCode.OK,
            wordleController.getAllWords()
        )
    }
}


fun Route.getWordOfTheDay(wordleController: WordleController) {
    get("/wordoftheday") {
        call.respond(
            HttpStatusCode.OK,
            wordleController.getWordOfTheDay()
        )
    }
}
