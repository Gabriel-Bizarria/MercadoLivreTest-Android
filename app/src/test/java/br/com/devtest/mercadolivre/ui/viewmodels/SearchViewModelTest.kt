package br.com.devtest.mercadolivre.ui.viewmodels

import app.cash.turbine.test
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.models.AttributeUiModel
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import io.ktor.http.HttpStatusCode
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    // Test data factory methods
    private fun makeProduct(
        id: String = "1",
        title: String = "Test Product",
        price: Double = 100.0,
        isFreeShipping: Boolean = false,
        originalPrice: Double? = null,
        currencyId: String? = null,
        productImage: String? = "https://example.com/image.jpg",
        attributes: List<AttributeUiModel> = emptyList()
    ) = ProductListItemUiModel(
        id = id,
        title = title,
        price = price.toBigDecimal(),
        isFreeShipping = isFreeShipping,
        originalPrice = originalPrice?.toBigDecimal(),
        currencyId = currencyId,
        productImage = productImage,
        attributes = attributes
    )

    // Success scenarios
    @Test
    fun `should emit Loading then Success when repository returns data`() = testScope.runTest {
        val products = listOf(makeProduct())
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(products)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            val isSuccess = successState is UiState.Success
            assertTrue(isSuccess, "Expected UiState.Success, but was: $successState")
            assertEquals(products, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle multiple products in search results`() = testScope.runTest {
        val products = listOf(
            makeProduct(id = "1", title = "Product 1", price = 100.0),
            makeProduct(id = "2", title = "Product 2", price = 200.0, isFreeShipping = true),
            makeProduct(id = "3", title = "Product 3", price = 150.0, originalPrice = 180.0)
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(products)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertEquals(3, successData.size)
            assertEquals("Product 1", successData[0].title)
            assertEquals("Product 2", successData[1].title)
            assertTrue(successData[1].isFreeShipping)
            assertEquals("Product 3", successData[2].title)
            assertEquals(180.0.toBigDecimal(), successData[2].originalPrice)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle empty search results`() = testScope.runTest {
        val emptyProducts = emptyList<ProductListItemUiModel>()
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(emptyProducts)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertTrue(successData.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with all optional fields`() = testScope.runTest {
        val productWithAllFields = makeProduct(
            id = "complete-product",
            title = "Complete Product",
            price = 299.99,
            isFreeShipping = true,
            originalPrice = 399.99,
            currencyId = "BRL",
            productImage = "https://example.com/complete-image.jpg",
            attributes = listOf(
                AttributeUiModel("BRAND", "Brand", "Apple"),
                AttributeUiModel("COLOR", "Color", "Black")
            )
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(listOf(productWithAllFields))

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertEquals(1, successData.size)
            val product = successData[0]
            assertEquals("complete-product", product.id)
            assertEquals("Complete Product", product.title)
            assertEquals(299.99.toBigDecimal(), product.price)
            assertTrue(product.isFreeShipping)
            assertEquals(399.99.toBigDecimal(), product.originalPrice)
            assertEquals("BRL", product.currencyId)
            assertEquals("https://example.com/complete-image.jpg", product.productImage)
            assertEquals(2, product.attributes.size)
            assertEquals("Apple", product.attributes[0].valueName)
            assertEquals("Black", product.attributes[1].valueName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Error scenarios
    @Test
    fun `should emit Loading then Error when repository returns GenericError`() = testScope.runTest {
        val errorMessage = "Search error occurred"
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.GenericError(errorMessage)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error, "Expected UiState.Error, but was: $errorState")
            val error = errorState
            assertEquals(errorMessage, error.message)
            assertNull(error.messageStringRes)
            assertNull(error.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error when repository returns NetworkError`() = testScope.runTest {
        val errorMessage = "Network connection failed"
        val errorCode = 500
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val error = errorState
            assertEquals(errorMessage, error.message)
            assertEquals(errorCode, error.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error when repository returns NetworkError without code`() = testScope.runTest {
        val errorMessage = "Connection timeout"
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.NetworkError(errorMessage)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val error = errorState
            assertEquals(errorMessage, error.message)
            assertNull(error.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // HTTP Status Code specific error handling
    @Test
    fun `should handle 404 error with string resource`() = testScope.runTest {
        val errorMessage = "Not found"
        val errorCode = HttpStatusCode.NotFound.value
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val error = errorState
            assertEquals(R.string.no_product_found, error.messageStringRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle 400 error with message`() = testScope.runTest {
        val errorMessage = "Bad request"
        val errorCode = HttpStatusCode.BadRequest.value
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val error = errorState
            assertEquals(errorMessage, error.message)
            assertEquals(errorCode, error.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle other HTTP error codes with message`() = testScope.runTest {
        val errorMessage = "Internal server error"
        val errorCode = 500
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val error = errorState
            assertEquals(errorMessage, error.message)
            assertEquals(errorCode, error.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Query input handling
    @Test
    fun `should use current query input when fetching search results`() = testScope.runTest {
        val query = "smartphone"
        val products = listOf(makeProduct(title = "Smartphone"))
        
        viewModel.setQueryInput(query)
        coEvery { searchRepository.getSearchResults(query) } returns NetworkResponse.Success(products)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(products, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle empty query input`() = testScope.runTest {
        val emptyQuery = ""
        val products = listOf(makeProduct(title = "Default Product"))
        
        viewModel.setQueryInput(emptyQuery)
        coEvery { searchRepository.getSearchResults(emptyQuery) } returns NetworkResponse.Success(products)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(products, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle whitespace query input`() = testScope.runTest {
        val whitespaceQuery = "   "
        val products = listOf(makeProduct(title = "Whitespace Product"))
        
        viewModel.setQueryInput(whitespaceQuery)
        coEvery { searchRepository.getSearchResults(whitespaceQuery) } returns NetworkResponse.Success(products)

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(products, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Query input state management
    @Test
    fun `should update query input state`() {
        val initialQuery = ""
        val newQuery = "laptop"
        
        // Verify initial state
        assertEquals(initialQuery, viewModel.queryInput.value)
        
        // Update query
        viewModel.setQueryInput(newQuery)
        assertEquals(newQuery, viewModel.queryInput.value)
    }

    @Test
    fun `should handle multiple query input updates`() {
        val queries = listOf("", "phone", "smartphone", "iPhone")
        
        queries.forEach { query ->
            viewModel.setQueryInput(query)
            assertEquals(query, viewModel.queryInput.value)
        }
    }

    // Edge cases
    @Test
    fun `should handle product with null optional fields`() = testScope.runTest {
        val productWithNullFields = makeProduct(
            id = "null-fields",
            title = "Product with Null Fields",
            price = 50.0,
            isFreeShipping = false,
            originalPrice = null,
            currencyId = null,
            productImage = null,
            attributes = emptyList()
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(listOf(productWithNullFields))

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertEquals(1, successData.size)
            val product = successData[0]
            assertEquals("null-fields", product.id)
            assertEquals("Product with Null Fields", product.title)
            assertEquals(50.0.toBigDecimal(), product.price)
            assertFalse(product.isFreeShipping)
            assertNull(product.originalPrice)
            assertNull(product.currencyId)
            assertNull(product.productImage)
            assertTrue(product.attributes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with zero price`() = testScope.runTest {
        val freeProduct = makeProduct(
            id = "free-product",
            title = "Free Product",
            price = 0.0,
            isFreeShipping = true
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(listOf(freeProduct))

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertEquals(1, successData.size)
            val product = successData[0]
            assertEquals("free-product", product.id)
            assertEquals("Free Product", product.title)
            assertEquals(0.0.toBigDecimal(), product.price)
            assertTrue(product.isFreeShipping)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with very high price`() = testScope.runTest {
        val expensiveProduct = makeProduct(
            id = "expensive-product",
            title = "Expensive Product",
            price = 999999.99,
            originalPrice = 1000000.00
        )
        coEvery { searchRepository.getSearchResults("") } returns NetworkResponse.Success(listOf(expensiveProduct))

        viewModel.uiState.test {
            viewModel.fetchSearchResults()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = successState.data
            assertEquals(1, successData.size)
            val product = successData[0]
            assertEquals("expensive-product", product.id)
            assertEquals("Expensive Product", product.title)
            assertEquals(999999.99.toBigDecimal(), product.price)
            assertEquals(1000000.00.toBigDecimal(), product.originalPrice)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Multiple fetch scenarios
    @Test
    fun `should handle multiple consecutive fetch calls`() = testScope.runTest {
        val firstProducts = listOf(makeProduct(id = "1", title = "First Product"))
        val secondProducts = listOf(makeProduct(id = "2", title = "Second Product"))
        
        coEvery { searchRepository.getSearchResults("") } returnsMany listOf(
            NetworkResponse.Success(firstProducts),
            NetworkResponse.Success(secondProducts)
        )

        viewModel.uiState.test {
            // First fetch
            viewModel.fetchSearchResults()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(firstProducts), awaitItem())
            
            // Second fetch
            viewModel.fetchSearchResults()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(secondProducts), awaitItem())
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle fetch after error`() = testScope.runTest {
        val errorMessage = "First attempt failed"
        val products = listOf(makeProduct(title = "Success Product"))
        
        coEvery { searchRepository.getSearchResults("") } returnsMany listOf(
            NetworkResponse.GenericError(errorMessage),
            NetworkResponse.Success(products)
        )

        viewModel.uiState.test {
            // First fetch - error
            viewModel.fetchSearchResults()
            assertEquals(UiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            assertEquals(errorMessage, errorState.message)
            
            // Second fetch - success
            viewModel.fetchSearchResults()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(products), awaitItem())
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Initial state tests
    @Test
    fun `should start with Loading state`() {
        assertEquals(UiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `should start with empty query input`() {
        assertEquals("", viewModel.queryInput.value)
    }
}
