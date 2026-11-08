package com.mauricioliveira.nutrisearch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mauricioliveira.nutrisearch.core.database.dao.ProfessionalDao
import com.mauricioliveira.nutrisearch.core.database.dao.RemoteKeyDao
import com.mauricioliveira.nutrisearch.core.database.dao.SearchResultDao
import com.mauricioliveira.nutrisearch.core.database.entity.ProfessionalEntity
import com.mauricioliveira.nutrisearch.core.database.entity.RemoteKeyEntity
import com.mauricioliveira.nutrisearch.core.database.entity.SearchResultEntity

@Database(
    entities = [ProfessionalEntity::class, SearchResultEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class NutriSearchDatabase : RoomDatabase() {
    abstract fun professionalDao(): ProfessionalDao
    abstract fun searchResultDao(): SearchResultDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val NAME = "nutrisearch.db"
    }
}
