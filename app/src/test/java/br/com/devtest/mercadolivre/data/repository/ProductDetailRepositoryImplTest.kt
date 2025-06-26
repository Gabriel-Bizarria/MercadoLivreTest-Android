package br.com.devtest.mercadolivre.data.repository

import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.models.productdetails.ProductDetailsResponse
import br.com.devtest.mercadolivre.data.models.productdetails.PicturesItem
import br.com.devtest.mercadolivre.data.models.productdetails.Shipping
import br.com.devtest.mercadolivre.data.models.productdetails.AttributesItem
import br.com.devtest.mercadolivre.data.models.productdescription.ProductDescriptionResponse
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailRepositoryImplTest {
    private lateinit var apiService: ApiService
    private lateinit var repository: ProductDetailRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = ProductDetailRepositoryImpl(apiService)
    }

    @Test
    fun `getProductDetails returns success when details and description are valid`() = runTest {
        val details = ProductDetailsResponse(
            id = "1",
            title = "Produto Teste",
            price = 100.0,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição do produto")
        coEvery { apiService.queryProductDetails("1") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("1") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("1")
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success<ProductDetailUiModel>).data
        assertEquals("1", data.id)
        assertEquals("Produto Teste", data.title)
        assertEquals("Descrição do produto", data.description)
        assertEquals(true, data.freeShipping)
        assertEquals("BRL", data.currencyId)
    }

    @Test
    fun `getProductDetails returns generic error when details are invalid`() = runTest {
        val details = ProductDetailsResponse(
            id = null,
            title = null,
            price = null,
            originalPrice = null,
            pictures = null,
            shipping = null,
            attributes = null,
            currencyId = null
        )
        val description = ProductDescriptionResponse(plainText = "desc")
        coEvery { apiService.queryProductDetails("2") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("2") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("2")
        assertTrue(result is NetworkResponse.GenericError)
    }

    @Test
    fun `getProductDetails returns network error when api returns network error`() = runTest {
        coEvery { apiService.queryProductDetails("3") } returns NetworkResponse.NetworkError(
            message = "Network error",
            code = 500
        )
        coEvery { apiService.queryProductDescription("3") } returns NetworkResponse.Success(ProductDescriptionResponse(plainText = "desc"))

        val result = repository.getProductDetails("3")
        assertTrue(result is NetworkResponse.NetworkError)
        assertEquals("Network error", (result as NetworkResponse.NetworkError).message)
        assertEquals(500, result.code)
    }

    @Test
    fun `getProductDetails returns error when id is empty`() = runTest {
        val details = ProductDetailsResponse(
            id = "",
            title = "Produto Teste",
            price = 100.0,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição do produto")
        coEvery { apiService.queryProductDetails("4") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("4") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("4")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Product details not found", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns error when title is empty`() = runTest {
        val details = ProductDetailsResponse(
            id = "5",
            title = "",
            price = 100.0,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição do produto")
        coEvery { apiService.queryProductDetails("5") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("5") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("5")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Product details not found", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns error when price is null`() = runTest {
        val details = ProductDetailsResponse(
            id = "6",
            title = "Produto Teste",
            price = null,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição do produto")
        coEvery { apiService.queryProductDetails("6") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("6") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("6")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Product details not found", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns success with minimum valid data`() = runTest {
        val details = ProductDetailsResponse(
            id = "7",
            title = "Produto Mínimo",
            price = 50.0,
            originalPrice = null,
            pictures = null,
            shipping = null,
            attributes = null,
            currencyId = null
        )
        val description = ProductDescriptionResponse(plainText = null)
        coEvery { apiService.queryProductDetails("7") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("7") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("7")
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success<ProductDetailUiModel>).data
        assertEquals("7", data.id)
        assertEquals("Produto Mínimo", data.title)
        assertEquals(50.0, data.price.toDouble(), 0.01)
        assertNull(data.originalPrice)
        assertTrue(data.images.isEmpty())
        assertFalse(data.freeShipping)
        assertTrue(data.attributes.isEmpty())
        assertNull(data.currencyId)
        assertNull(data.description)
    }

    @Test
    fun `getProductDetails returns error when details request fails`() = runTest {
        coEvery { apiService.queryProductDetails("8") } returns NetworkResponse.GenericError("Details not found")
        coEvery { apiService.queryProductDescription("8") } returns NetworkResponse.Success(ProductDescriptionResponse(plainText = "desc"))

        val result = repository.getProductDetails("8")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Details not found", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns error when description request fails`() = runTest {
        val details = ProductDetailsResponse(
            id = "9",
            title = "Produto Teste",
            price = 100.0,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        coEvery { apiService.queryProductDetails("9") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("9") } returns NetworkResponse.GenericError("Description not found")

        val result = repository.getProductDetails("9")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Description not found", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns error when both requests fail`() = runTest {
        coEvery { apiService.queryProductDetails("10") } returns NetworkResponse.GenericError("Details error")
        coEvery { apiService.queryProductDescription("10") } returns NetworkResponse.GenericError("Description error")

        val result = repository.getProductDetails("10")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Details error", (result as NetworkResponse.GenericError).message)
    }

    @Test
    fun `getProductDetails returns network error when details request fails with network error`() = runTest {
        coEvery { apiService.queryProductDetails("11") } returns NetworkResponse.NetworkError("Connection failed", 404)
        coEvery { apiService.queryProductDescription("11") } returns NetworkResponse.Success(ProductDescriptionResponse(plainText = "desc"))

        val result = repository.getProductDetails("11")
        assertTrue(result is NetworkResponse.NetworkError)
        assertEquals("Connection failed", (result as NetworkResponse.NetworkError).message)
        assertEquals(404, result.code)
    }

    @Test
    fun `getProductDetails returns network error when description request fails with network error`() = runTest {
        val details = ProductDetailsResponse(
            id = "12",
            title = "Produto Teste",
            price = 100.0,
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        coEvery { apiService.queryProductDetails("12") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("12") } returns NetworkResponse.NetworkError("Description connection failed", 503)

        val result = repository.getProductDetails("12")
        assertTrue(result is NetworkResponse.NetworkError)
        assertEquals("Description connection failed", (result as NetworkResponse.NetworkError).message)
        assertEquals(503, result.code)
    }

    @Test
    fun `getProductDetails returns success with complete and complex data`() = runTest {
        val details = ProductDetailsResponse(
            id = "13",
            title = "Produto Completo",
            price = 299.99,
            originalPrice = 399.99,
            pictures = listOf(
                PicturesItem(secureUrl = "https://example.com/image1.jpg"),
                PicturesItem(secureUrl = "https://example.com/image2.jpg"),
                PicturesItem(secureUrl = null)
            ),
            shipping = Shipping(freeShipping = false),
            attributes = listOf(
                AttributesItem(id = "BRAND", name = "Marca", valueName = "Samsung"),
                AttributesItem(id = "COLOR", name = "Cor", valueName = "Preto"),
                AttributesItem(id = "MODEL", name = "Modelo", valueName = "Galaxy S21")
            ),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição completa do produto com detalhes técnicos")
        coEvery { apiService.queryProductDetails("13") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("13") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("13")
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success<ProductDetailUiModel>).data
        assertEquals("13", data.id)
        assertEquals("Produto Completo", data.title)
        assertEquals(299.99, data.price.toDouble(), 0.01)
        assertEquals(399.99, data.originalPrice?.toDouble() ?: 0.0, 0.01)
        assertEquals(2, data.images.size) // Only non-null URLs
        assertEquals("https://example.com/image1.jpg", data.images[0])
        assertEquals("https://example.com/image2.jpg", data.images[1])
        assertFalse(data.freeShipping)
        assertEquals(3, data.attributes.size)
        assertEquals("BRAND", data.attributes[0].attributeId)
        assertEquals("Marca", data.attributes[0].attributeName)
        assertEquals("Samsung", data.attributes[0].valueName)
        assertEquals("BRL", data.currencyId)
        assertEquals("Descrição completa do produto com detalhes técnicos", data.description)
    }

    @Test
    fun `getProductDetails returns generic error when both requests return success but data is invalid`() = runTest {
        val details = ProductDetailsResponse(
            id = "14",
            title = "", // Empty title
            price = null, // Null price
            originalPrice = 120.0,
            pictures = listOf(PicturesItem(secureUrl = "https://example.com/image.jpg")),
            shipping = Shipping(freeShipping = true),
            attributes = listOf(AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")),
            currencyId = "BRL"
        )
        val description = ProductDescriptionResponse(plainText = "Descrição válida")
        coEvery { apiService.queryProductDetails("14") } returns NetworkResponse.Success(details)
        coEvery { apiService.queryProductDescription("14") } returns NetworkResponse.Success(description)

        val result = repository.getProductDetails("14")
        assertTrue(result is NetworkResponse.GenericError)
        assertEquals("Product details not found", (result as NetworkResponse.GenericError).message)
    }
}
