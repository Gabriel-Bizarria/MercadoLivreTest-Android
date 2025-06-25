package br.com.devtest.mercadolivre.di

import androidx.lifecycle.SavedStateHandle
import br.com.devtest.mercadolivre.data.datasource.createMockKtorClient
import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.repository.ProductDetailRepositoryImpl
import br.com.devtest.mercadolivre.data.repository.SearchRepositoryImpl
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.viewmodels.ProductDetailViewModel
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { createMockKtorClient(context = get()) }
    single { ApiService(get()) }

    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<ProductDetailRepository> { ProductDetailRepositoryImpl(get()) }

    viewModel {
        SearchViewModel(
            repository = get()
        )
    }
    viewModel { (handle: SavedStateHandle) ->
        ProductDetailViewModel(get(), handle)
    }
}
