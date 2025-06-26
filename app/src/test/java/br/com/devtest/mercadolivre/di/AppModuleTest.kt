package br.com.devtest.mercadolivre.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.viewmodels.ProductDetailViewModel
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel
import io.ktor.client.HttpClient
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject

class AppModuleTest : KoinTest {

    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        stopKoin()
        mockContext = mockk(relaxed = true)
    }

    @Test
    fun `should verify all dependencies are declared`() {
        startKoin {
            modules(appModule)
            // Provide mock context for testing
            modules(module {
                single<Context> { mockContext }
            })
        }

        // Verify all dependencies can be resolved
        val httpClient: HttpClient = get()
        val apiService: ApiService = get()
        val searchRepository: SearchRepository = get()
        val productDetailRepository: ProductDetailRepository = get()
        val searchViewModel: SearchViewModel = get()

        assertNotNull(httpClient)
        assertNotNull(apiService)
        assertNotNull(searchRepository)
        assertNotNull(productDetailRepository)
        assertNotNull(searchViewModel)
    }

    @Test
    fun `should inject HttpClient successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val httpClient: HttpClient = get()
        assertNotNull(httpClient)
    }

    @Test
    fun `should inject ApiService successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val apiService: ApiService = get()
        assertNotNull(apiService)
    }

    @Test
    fun `should inject SearchRepository successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val searchRepository: SearchRepository = get()
        assertNotNull(searchRepository)
    }

    @Test
    fun `should inject ProductDetailRepository successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val productDetailRepository: ProductDetailRepository = get()
        assertNotNull(productDetailRepository)
    }

    @Test
    fun `should inject SearchViewModel successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val searchViewModel: SearchViewModel = get()
        assertNotNull(searchViewModel)
    }

    @Test
    fun `should inject ProductDetailViewModel with SavedStateHandle successfully`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val savedStateHandle = SavedStateHandle(mapOf("itemId" to "MLB123456789"))
        val productDetailViewModel: ProductDetailViewModel = get { parametersOf(savedStateHandle) }
        assertNotNull(productDetailViewModel)
    }

    @Test
    fun `should inject all dependencies using inject() delegate`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val httpClient: HttpClient by inject()
        val apiService: ApiService by inject()
        val searchRepository: SearchRepository by inject()
        val productDetailRepository: ProductDetailRepository by inject()
        val searchViewModel: SearchViewModel by inject()

        assertNotNull(httpClient)
        assertNotNull(apiService)
        assertNotNull(searchRepository)
        assertNotNull(productDetailRepository)
        assertNotNull(searchViewModel)
    }

    @Test
    fun `should create new instances for ViewModels each time`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val searchViewModel1: SearchViewModel = get()
        val searchViewModel2: SearchViewModel = get()

        // ViewModels should be different instances (not singletons)
        assert(searchViewModel1 !== searchViewModel2)
    }

    @Test
    fun `should create new instances for ProductDetailViewModel with different SavedStateHandle`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        val savedStateHandle1 = SavedStateHandle(mapOf("itemId" to "MLB123456789"))
        val savedStateHandle2 = SavedStateHandle(mapOf("itemId" to "MLB987654321"))

        val productDetailViewModel1: ProductDetailViewModel = get { parametersOf(savedStateHandle1) }
        val productDetailViewModel2: ProductDetailViewModel = get { parametersOf(savedStateHandle2) }

        // ViewModels should be different instances
        assert(productDetailViewModel1 !== productDetailViewModel2)
        
        // Should have different itemIds
        assert(productDetailViewModel1.itemId != productDetailViewModel2.itemId)
    }

    @Test
    fun `should inject dependencies in correct order`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        // HttpClient should be available first
        val httpClient: HttpClient = get()
        assertNotNull(httpClient)

        // ApiService should depend on HttpClient
        val apiService: ApiService = get()
        assertNotNull(apiService)

        // Repositories should depend on ApiService
        val searchRepository: SearchRepository = get()
        val productDetailRepository: ProductDetailRepository = get()
        assertNotNull(searchRepository)
        assertNotNull(productDetailRepository)

        // ViewModels should depend on repositories
        val searchViewModel: SearchViewModel = get()
        assertNotNull(searchViewModel)
    }

    @Test
    fun `should handle multiple Koin starts and stops`() {
        // First start
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }
        val searchViewModel1: SearchViewModel = get()
        assertNotNull(searchViewModel1)
        stopKoin()

        // Second start
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }
        val searchViewModel2: SearchViewModel = get()
        assertNotNull(searchViewModel2)
        stopKoin()
    }

    @Test
    fun `should verify module structure matches expected dependencies`() {
        startKoin {
            modules(appModule)
            modules(module {
                single<Context> { mockContext }
            })
        }

        // Verify the module contains the expected dependencies by trying to resolve them
        // This is a functional verification instead of structural
        val httpClient: HttpClient = get()
        val apiService: ApiService = get()
        val searchRepository: SearchRepository = get()
        val productDetailRepository: ProductDetailRepository = get()
        val searchViewModel: SearchViewModel = get()
        val savedStateHandle = SavedStateHandle(mapOf("itemId" to "MLB123456789"))
        val productDetailViewModel: ProductDetailViewModel = get { parametersOf(savedStateHandle) }

        // All dependencies should be resolved successfully
        assertNotNull(httpClient)
        assertNotNull(apiService)
        assertNotNull(searchRepository)
        assertNotNull(productDetailRepository)
        assertNotNull(searchViewModel)
        assertNotNull(productDetailViewModel)
    }
} 