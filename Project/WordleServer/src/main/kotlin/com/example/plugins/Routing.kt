package com.example.plugins

import com.example.wordle.WordleController
import com.example.routes.wordleSocket
import com.example.routes.getAllWords
import com.example.routes.getWordOfTheDay
import com.example.routes.wordleSocket
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val wordleController by inject<WordleController>()
    install(Routing) {
        wordleSocket(wordleController)
        getAllWords(wordleController)
        getWordOfTheDay(wordleController)
    }
}
