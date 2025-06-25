package br.com.devtest.mercadolivre.ui.viewmodels

import app.cash.turbine.test
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @MockK
    private lateinit var searchRepository: SearchRepository
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchRepository = mockk()
        viewModel = SearchViewModel(searchRepository, testDispatcher)
    }

    @Test
    fun `fetchSearchResults emits Success when repository returns data`() = testScope.runTest {
        val products = listOf(
            ProductListItemUiModel(
                id = "1",
                title = "Product",
                price = 100.0.toBigDecimal(),
                isFreeShipping = false,
                originalPrice = null,
                currencyId = null,
                productImage = "Desc"
            )
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(products)

        viewModel.fetchSearchResults()

        viewModel.searchUiState.test {
            val first = awaitItem()
            println("[TESTE] Primeiro estado emitido: $first")
            assert(first is UiState.Loading)
            val second = awaitItem()
            println("[TESTE] Segundo estado emitido: $second")
            if (second is UiState.Success) {
                assertEquals(products, second.data)
            } else {
                throw AssertionError("Esperado UiState.Success, mas foi: $second")
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchSearchResults emits Error when repository returns error`() = testScope.runTest {
        val errorMsg = "Erro de busca"
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.GenericError(
            message = errorMsg
        )

        viewModel.fetchSearchResults()

        viewModel.searchUiState.test {
            val first = awaitItem()
            println("[TESTE] Primeiro estado emitido: $first")
            assert(first is UiState.Loading)
            val errorState = awaitItem()
            println("[TESTE] Segundo estado emitido: $errorState")
            if (errorState is UiState.Error) {
                assert(errorState.message.contains(errorMsg)) {
                    "Mensagem de erro inesperada: '${errorState.message}'"
                }
            } else {
                throw AssertionError("Esperado UiState.Error, mas foi: $errorState")
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
