package com.mauricioliveira.nutrisearch.feature.professionaldetail.di

import com.mauricioliveira.nutrisearch.feature.professionaldetail.ui.ProfessionalDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val professionalDetailModule = module {
    viewModel { params ->
        ProfessionalDetailViewModel(
            professionalId = params.get(),
            repository = get()
        )
    }
}