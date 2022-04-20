package com.example.wordle.data.remote

import com.example.wordle.data.remote.dto.WordleWordDto
import com.example.wordle.domain.model.WordleWord
import io.ktor.client.*
import io.ktor.client.request.*
import java.text.DateFormat
import java.util.*



class HttpServiceImpl (
    private val client: HttpClient
): HttpService {
    override suspend fun getAllWords(): List<WordleWord> {
        return try {
            client.get<List<WordleWordDto>>(HttpService.Endpoints.GetAllWords.url)
                .map{it.toWordleWord()}
        }
        catch (e: Exception) {
            emptyList();
        }
    }
    override suspend fun getWordOfTheDay(): WordleWord {
        return try {
            client.get<WordleWordDto>(HttpService.Endpoints.GetWordOfTheDay.url).toWordleWord()
        }
        catch (e: Exception) {
            WordleWord("hoard", DateFormat
                .getDateInstance(DateFormat.DEFAULT)
                .format(Date(0)),
                "Wordle Bot")
        }
    }
}