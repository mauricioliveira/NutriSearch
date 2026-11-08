package com.mauricioliveira.nutrisearch

import android.app.Application
import com.mauricioliveira.nutrisearch.core.data.di.dataModule
import com.mauricioliveira.nutrisearch.feature.professionaldetail.di.professionalDetailModule
import com.mauricioliveira.nutrisearch.feature.professionals.di.professionalsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NutriSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NutriSearchApplication)
            modules(dataModule, professionalsModule, professionalDetailModule)
        }
    }
}
