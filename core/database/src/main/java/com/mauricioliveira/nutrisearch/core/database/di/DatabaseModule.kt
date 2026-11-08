package com.mauricioliveira.nutrisearch.core.database.di

import androidx.room.Room
import com.mauricioliveira.nutrisearch.core.database.NutriSearchDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = NutriSearchDatabase::class.java,
            name = NutriSearchDatabase.NAME
        ).build()
    }
    single { get<NutriSearchDatabase>().professionalDao() }
    single { get<NutriSearchDatabase>().searchResultDao() }
    single { get<NutriSearchDatabase>().remoteKeyDao() }
}
