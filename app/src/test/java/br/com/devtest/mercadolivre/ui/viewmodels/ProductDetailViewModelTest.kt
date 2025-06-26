package br.com.devtest.mercadolivre.ui.viewmodels

import app.cash.turbine.test
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import br.com.devtest.mercadolivre.ui.models.AttributeUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.After
import org.junit.Test
import androidx.lifecycle.SavedStateHandle
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.ui.state.UiState
import java.math.BigDecimal
import kotlin.test.assertFails
import kotlin.test.assertNull
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {
    private lateinit var repository: ProductDetailRepository
    private lateinit var viewModel: ProductDetailViewModel
    private val testDispatcher = StandardTestDispatcher()
    // Sample itemId for testing
    private val itemId = "MLB123456789"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        val savedStateHandle = SavedStateHandle(mapOf("itemId" to itemId))
        viewModel = ProductDetailViewModel(repository = repository, savedStateHandle = savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test data factory methods
    private fun makeProductDetail(
        id: String = itemId,
        title: String = "Produto Teste",
        price: BigDecimal = BigDecimal("99.99"),
        images: List<String> = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
        freeShipping: Boolean = true,
        originalPrice: BigDecimal? = BigDecimal("129.99"),
        description: String? = "Descrição detalhada do produto de teste",
        currencyId: String? = "BRL",
        attributes: List<AttributeUiModel> = listOf(
            AttributeUiModel("BRAND", "Marca", "Nike"),
            AttributeUiModel("COLOR", "Cor", "Azul")
        )
    ) = ProductDetailUiModel(
        id = id,
        title = title,
        price = price,
        images = images,
        freeShipping = freeShipping,
        originalPrice = originalPrice,
        description = description,
        currencyId = currencyId,
        attributes = attributes
    )

    private fun makeMinimalProductDetail(
        id: String = itemId,
        title: String = "Produto Mínimo",
        price: BigDecimal = BigDecimal.TEN
    ) = ProductDetailUiModel(
        id = id,
        title = title,
        price = price,
        images = emptyList(),
        freeShipping = false
    )

    // Success scenarios
    @Test
    fun `should emit Loading then Success when repository returns valid data`() = runTest {
        val productDetail = makeProductDetail()
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productDetail)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(productDetail), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with minimal data`() = runTest {
        val minimalProduct = makeMinimalProductDetail()
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(minimalProduct)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(minimalProduct), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with all optional fields`() = runTest {
        val completeProduct = makeProductDetail(
            originalPrice = BigDecimal("149.99"),
            description = "Descrição muito detalhada com várias linhas de texto",
            currencyId = "USD",
            attributes = listOf(
                AttributeUiModel("BRAND", "Brand", "Apple"),
                AttributeUiModel("MODEL", "Model", "iPhone 15"),
                AttributeUiModel("COLOR", "Color", "Black"),
                AttributeUiModel("STORAGE", "Storage", "256GB")
            )
        )
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(completeProduct)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successStateItem = awaitItem()
            assertTrue(successStateItem is UiState.Success)
            val successState = successStateItem as UiState.Success
            assertEquals(completeProduct, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Error scenarios
    @Test
    fun `should emit Loading then Error when repository returns GenericError`() = runTest {
        val errorMessage = "Product not found"
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.GenericError(errorMessage)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertNull(errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error when repository returns NetworkError`() = runTest {
        val errorMessage = "Network connection failed"
        val errorCode = 500
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertEquals(errorCode, errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Loading then Error when repository returns NetworkError without code`() = runTest {
        val errorMessage = "Connection timeout"
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.NetworkError(errorMessage)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertEquals(null, errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Edge cases
    @Test
    fun `should handle empty images list`() = runTest {
        val productWithNoImages = makeProductDetail(images = emptyList())
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productWithNoImages)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successStateItem = awaitItem()
            assertTrue(successStateItem is UiState.Success)
            val successData = (successStateItem as UiState.Success).data
            assertTrue(successData.images.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with null original price`() = runTest {
        val productWithoutOriginalPrice = makeProductDetail(originalPrice = null)
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productWithoutOriginalPrice)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = (successState as UiState.Success).data
            assertEquals(null, successData.originalPrice)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with null description`() = runTest {
        val productWithoutDescription = makeProductDetail(description = null)
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productWithoutDescription)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = (successState as UiState.Success).data
            assertEquals(null, successData.description)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with empty attributes list`() = runTest {
        val productWithoutAttributes = makeProductDetail(attributes = emptyList())
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productWithoutAttributes)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = (successState as UiState.Success).data
            assertTrue(successData.attributes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle product with free shipping disabled`() = runTest {
        val productWithoutFreeShipping = makeProductDetail(freeShipping = false)
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.Success(productWithoutFreeShipping)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val successData = (successState as UiState.Success).data
            assertEquals(false, successData.freeShipping)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // SavedStateHandle tests
    @Test
    fun `should expose correct itemId from SavedStateHandle`() {
        assertEquals(itemId, viewModel.itemId)
    }

    @Test
    fun `should throw exception when itemId is missing from SavedStateHandle`() {
        val emptySavedStateHandle = SavedStateHandle()

        assertFails {
            ProductDetailViewModel(repository, savedStateHandle = emptySavedStateHandle)
        }
    }

    @Test
    fun `should handle different itemId values`() = runTest {
        val differentItemId = "MLB987654321"
        val savedStateHandle = SavedStateHandle(mapOf("itemId" to differentItemId))
        val viewModelWithDifferentId = ProductDetailViewModel(repository, savedStateHandle = savedStateHandle)
        
        val productDetail = makeProductDetail(id = differentItemId)
        coEvery { repository.getProductDetails(differentItemId) } returns NetworkResponse.Success(productDetail)

        viewModelWithDifferentId.uiState.test {
            viewModelWithDifferentId.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(productDetail), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Multiple fetch tests
    @Test
    fun `should handle multiple fetch calls correctly`() = runTest {
        val firstProduct = makeProductDetail(title = "First Product")
        val secondProduct = makeProductDetail(title = "Second Product")
        
        coEvery { repository.getProductDetails(itemId) } returnsMany listOf(
            NetworkResponse.Success(firstProduct),
            NetworkResponse.Success(secondProduct)
        )

        viewModel.uiState.test {
            // First fetch
            viewModel.fetchProductDetail()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(firstProduct), awaitItem())
            
            // Second fetch
            viewModel.fetchProductDetail()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(secondProduct), awaitItem())
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle fetch after error correctly`() = runTest {
        val errorMessage = "Initial error"
        val productDetail = makeProductDetail()
        
        coEvery { repository.getProductDetails(itemId) } returnsMany listOf(
            NetworkResponse.GenericError(errorMessage),
            NetworkResponse.Success(productDetail)
        )

        viewModel.uiState.test {
            // First fetch - error
            viewModel.fetchProductDetail()
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            
            // Second fetch - success
            viewModel.fetchProductDetail()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(productDetail), awaitItem())
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Specific error code tests
    @Test
    fun `should verify NetworkResponse_NetworkError constructor works correctly`() {
        val errorMessage = "Test error"
        val errorCode = 500
        val networkError = NetworkResponse.NetworkError(errorMessage, errorCode)
        
        assertEquals(errorMessage, networkError.message)
        assertEquals(errorCode, networkError.code)
    }

    @Test
    fun `should handle 404 error code correctly`() = runTest {
        val errorMessage = "Product not found"
        val errorCode = 404
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertEquals(errorCode, errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle 500 error code correctly`() = runTest {
        val errorMessage = "Internal server error"
        val errorCode = 500
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertEquals(errorCode, errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle 503 error code correctly`() = runTest {
        val errorMessage = "Service unavailable"
        val errorCode = 503
        coEvery { repository.getProductDetails(itemId) } returns NetworkResponse.NetworkError(errorMessage, errorCode)

        viewModel.uiState.test {
            viewModel.fetchProductDetail()
            
            assertEquals(UiState.Loading, awaitItem())
            val errorStateItem = awaitItem()
            assertTrue(errorStateItem is UiState.Error)
            val errorState = errorStateItem as UiState.Error
            assertEquals(errorMessage, errorState.message)
            assertEquals(errorCode, errorState.code)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
