package com.example.plugins

import com.example.session.WordleSession
import io.ktor.sessions.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<WordleSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if(call.sessions.get<WordleSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(WordleSession(username, generateNonce()))
        }
    }
}
