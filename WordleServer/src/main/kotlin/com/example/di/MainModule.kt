package com.example.di

import com.example.data.HttpDataSource
import com.example.data.HttpDataSourceImpl
import com.example.wordle.WordleController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("word_db_upper")
    }
    single<HttpDataSource> {
        HttpDataSourceImpl(get())
    }
    single {
        WordleController(get())
    }
}