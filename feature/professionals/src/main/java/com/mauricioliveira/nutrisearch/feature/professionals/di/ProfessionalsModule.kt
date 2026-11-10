package com.mauricioliveira.nutrisearch.feature.professionals.di

import com.mauricioliveira.nutrisearch.feature.professionals.ui.ProfessionalsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val professionalsModule = module {
    viewModelOf(::ProfessionalsViewModel)
}
