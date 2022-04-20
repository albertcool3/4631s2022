package com.example.data

import com.example.data.model.WordleWord
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.text.DateFormat
import java.util.*

class HttpDataSourceImpl(
    private val db: CoroutineDatabase
): HttpDataSource {

    private val wordleWords = db.getCollection<WordleWord>()

    override suspend fun getAllWords(): List<WordleWord> {
        return wordleWords.find()
            .descendingSort(WordleWord::timestamp)
            .toList()
    }
    override suspend fun getWordOfTheDay(): WordleWord {
        return wordleWords.find()
            .descendingSort(WordleWord::timestamp)
            .toList().random()
//        return WordleWord("hoard",
//            0,
//            "Wordle Bot")
    }
    override suspend fun insertWord(word: WordleWord) {
        wordleWords.insertOne(word)
    }
}