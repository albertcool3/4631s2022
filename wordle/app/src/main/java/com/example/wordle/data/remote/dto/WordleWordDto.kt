package com.example.wordle.data.remote.dto

import com.example.wordle.domain.model.WordleWord
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class WordleWordDto (
    val text: String,
    val timestamp: Long,
    val username: String,
    val id: String
) {
    fun toWordleWord(): WordleWord {
        val date = Date(timestamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return WordleWord(
            text = text,
            formattedTime = formattedDate,
            username = username
        )
    }
}