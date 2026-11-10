package com.mauricioliveira.nutrisearch.core.data.di

import com.mauricioliveira.nutrisearch.core.data.BuildConfig
import com.mauricioliveira.nutrisearch.core.data.remote.HttpClientFactory
import com.mauricioliveira.nutrisearch.core.data.remote.NutriSearchApi
import com.mauricioliveira.nutrisearch.core.data.repository.DefaultProfessionalRepository
import com.mauricioliveira.nutrisearch.core.database.di.databaseModule
import com.mauricioliveira.nutrisearch.core.domain.repository.ProfessionalRepository
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule)
    single {
        HttpClientFactory.create(
            engine = OkHttp.create(),
            baseUrl = BuildConfig.API_BASE_URL,
            enableLogging = BuildConfig.DEBUG,
        )
    }
    single { NutriSearchApi(get()) }
    single<ProfessionalRepository> { DefaultProfessionalRepository(api = get(), database = get()) }
}
