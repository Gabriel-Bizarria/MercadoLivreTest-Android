package br.com.devtest.mercadolivre.data.repository

import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.models.search.ResultsItem
import br.com.devtest.mercadolivre.data.models.search.SearchResponse
import br.com.devtest.mercadolivre.data.models.search.Shipping
import br.com.devtest.mercadolivre.data.models.search.AttributesItem
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {
    private lateinit var apiService: ApiService
    private lateinit var repository: SearchRepositoryImpl

    private fun makeResult(
        id: String? = "1",
        title: String? = "Produto 1",
        price: Double? = 10.0,
        thumbnail: String? = "img.png",
        availableQuantity: Int? = 2,
        originalPrice: Double? = null,
        currencyId: String? = null,
        shipping: Shipping? = null,
        attributes: List<AttributesItem?>? = null
    ) = ResultsItem(
        id = id,
        title = title,
        price = price,
        thumbnail = thumbnail,
        availableQuantity = availableQuantity,
        originalPrice = originalPrice,
        currencyId = currencyId,
        shipping = shipping,
        attributes = attributes
    )

    @Before
    fun setUp() {
        apiService = mockk()
        repository = SearchRepositoryImpl(apiService)
    }

    @Test
    fun `should return mapped ProductUiModel list on success`() = runTest {
        val searchResult = makeResult()
        val response = SearchResponse(results = listOf(searchResult))
        coEvery { apiService.queryProducts("notebook") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("notebook")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(
            ProductListItemUiModel(
                id = "1",
                title = "Produto 1",
                price = (10.0).toBigDecimalMonetary(),
                isFreeShipping = false,
                originalPrice = null,
                currencyId = null,
                productImage = "img.png"
            ),
            list[0]
        )
    }

    @Test
    fun `should return empty list when results is null or empty`() = runTest {
        coEvery { apiService.queryProducts("empty") } returns NetworkResponse.Success(
            SearchResponse(results = null)
        )
        val resultNull = repository.getSearchResults("empty")
        assert(resultNull is NetworkResponse.Success)
        assertEquals(0, (resultNull as NetworkResponse.Success).data.size)

        coEvery { apiService.queryProducts("empty2") } returns NetworkResponse.Success(
            SearchResponse(results = listOf())
        )
        val resultEmpty = repository.getSearchResults("empty2")
        assert(resultEmpty is NetworkResponse.Success)
        assertEquals(0, (resultEmpty as NetworkResponse.Success).data.size)
    }

    @Test
    fun `should filter out invalid items`() = runTest {
        val valid = makeResult()
        val noId = makeResult(id = null)
        val noTitle = makeResult(id = "2", title = null)
        val blankTitle = makeResult(id = "3", title = "")
        val noPrice = makeResult(id = "4", price = null)
        val response = SearchResponse(results = listOf(valid, noId, noTitle, blankTitle, noPrice))
        coEvery { apiService.queryProducts("filter") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("filter")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(
            ProductListItemUiModel(
                id = "1",
                title = "Produto 1",
                price = (10.0).toBigDecimalMonetary(),
                isFreeShipping = false,
                originalPrice = null,
                currencyId = null,
                productImage = "img.png"
            ), list[0]
        )
    }

    @Test
    fun `should propagate NetworkError and GenericError`() = runTest {
        coEvery { apiService.queryProducts("neterr") } returns NetworkResponse.NetworkError(
            "fail",
            500
        )
        val netResult = repository.getSearchResults("neterr")
        assert(netResult is NetworkResponse.NetworkError)
        assertEquals(500, (netResult as NetworkResponse.NetworkError).code)

        coEvery { apiService.queryProducts("generr") } returns NetworkResponse.GenericError("fail")
        val genResult = repository.getSearchResults("generr")
        assert(genResult is NetworkResponse.GenericError)
        assertEquals("fail", (genResult as NetworkResponse.GenericError).message)
    }

    @Test
    fun `should handle large lists`() = runTest {
        val items = (1..1000).map { makeResult(id = it.toString(), title = "Produto $it") }
        val response = SearchResponse(results = items)
        coEvery { apiService.queryProducts("biglist") } returns NetworkResponse.Success(response)
        val result = repository.getSearchResults("biglist")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1000, list.size)
    }

    // Additional comprehensive tests

    @Test
    fun `should handle products with free shipping`() = runTest {
        val shipping = Shipping(freeShipping = true)
        val productWithFreeShipping = makeResult(
            id = "1",
            title = "Produto com Frete Grátis",
            price = 50.0,
            shipping = shipping
        )
        val response = SearchResponse(results = listOf(productWithFreeShipping))
        coEvery { apiService.queryProducts("freeshipping") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("freeshipping")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertTrue(list[0].isFreeShipping)
    }

    @Test
    fun `should handle products with original price`() = runTest {
        val productWithOriginalPrice = makeResult(
            id = "1",
            title = "Produto com Preço Original",
            price = 80.0,
            originalPrice = 100.0
        )
        val response = SearchResponse(results = listOf(productWithOriginalPrice))
        coEvery { apiService.queryProducts("originalprice") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("originalprice")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals((100.0).toBigDecimalMonetary(), list[0].originalPrice)
    }

    @Test
    fun `should handle products with currency ID`() = runTest {
        val productWithCurrency = makeResult(
            id = "1",
            title = "Produto com Moeda",
            price = 25.0,
            currencyId = "BRL"
        )
        val response = SearchResponse(results = listOf(productWithCurrency))
        coEvery { apiService.queryProducts("currency") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("currency")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals("BRL", list[0].currencyId)
    }

    @Test
    fun `should handle products with attributes`() = runTest {
        val attributes = listOf(
            AttributesItem(id = "BRAND", name = "Marca", valueName = "Samsung"),
            AttributesItem(id = "COLOR", name = "Cor", valueName = "Azul")
        )
        val productWithAttributes = makeResult(
            id = "1",
            title = "Produto com Atributos",
            price = 30.0,
            attributes = attributes
        )
        val response = SearchResponse(results = listOf(productWithAttributes))
        coEvery { apiService.queryProducts("attributes") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("attributes")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(2, list[0].attributes.size)
        assertEquals("BRAND", list[0].attributes[0].attributeId)
        assertEquals("Marca", list[0].attributes[0].attributeName)
        assertEquals("Samsung", list[0].attributes[0].valueName)
    }

    @Test
    fun `should handle products with null attributes`() = runTest {
        val productWithNullAttributes = makeResult(
            id = "1",
            title = "Produto sem Atributos",
            price = 15.0,
            attributes = null
        )
        val response = SearchResponse(results = listOf(productWithNullAttributes))
        coEvery { apiService.queryProducts("nullattributes") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("nullattributes")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertTrue(list[0].attributes.isEmpty())
    }

    @Test
    fun `should handle products with mixed valid and invalid attributes`() = runTest {
        val attributes = listOf(
            AttributesItem(id = "BRAND", name = "Marca", valueName = "Apple"),
            null,
            AttributesItem(id = "MODEL", name = null, valueName = "iPhone"),
            AttributesItem(id = "COLOR", name = "Cor", valueName = null)
        )
        val productWithMixedAttributes = makeResult(
            id = "1",
            title = "Produto com Atributos Mistos",
            price = 40.0,
            attributes = attributes
        )
        val response = SearchResponse(results = listOf(productWithMixedAttributes))
        coEvery { apiService.queryProducts("mixedattributes") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("mixedattributes")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(4, list[0].attributes.size)
        // Should handle null attributes gracefully
        assertEquals("", list[0].attributes[1].attributeId) // null attribute becomes empty string
    }

    @Test
    fun `should handle products with HTTP thumbnail URLs`() = runTest {
        val productWithHttpThumbnail = makeResult(
            id = "1",
            title = "Produto com Thumbnail HTTP",
            price = 20.0,
            thumbnail = "http://example.com/image.jpg"
        )
        val response = SearchResponse(results = listOf(productWithHttpThumbnail))
        coEvery { apiService.queryProducts("httpthumbnail") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("httpthumbnail")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals("https://example.com/image.jpg", list[0].productImage)
    }

    @Test
    fun `should handle products with HTTPS thumbnail URLs`() = runTest {
        val productWithHttpsThumbnail = makeResult(
            id = "1",
            title = "Produto com Thumbnail HTTPS",
            price = 25.0,
            thumbnail = "https://example.com/image.jpg"
        )
        val response = SearchResponse(results = listOf(productWithHttpsThumbnail))
        coEvery { apiService.queryProducts("httpsthumbnail") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("httpsthumbnail")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals("https://example.com/image.jpg", list[0].productImage)
    }

    @Test
    fun `should handle products with null thumbnail`() = runTest {
        val productWithNullThumbnail = makeResult(
            id = "1",
            title = "Produto sem Thumbnail",
            price = 35.0,
            thumbnail = null
        )
        val response = SearchResponse(results = listOf(productWithNullThumbnail))
        coEvery { apiService.queryProducts("nullthumbnail") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("nullthumbnail")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertTrue(list[0].productImage.isNullOrEmpty())
    }

    @Test
    fun `should handle products with zero price`() = runTest {
        val productWithZeroPrice = makeResult(
            id = "1",
            title = "Produto Gratuito",
            price = 0.0
        )
        val response = SearchResponse(results = listOf(productWithZeroPrice))
        coEvery { apiService.queryProducts("zeroprice") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("zeroprice")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals((0.0).toBigDecimalMonetary(), list[0].price)
    }

    @Test
    fun `should handle products with negative price`() = runTest {
        val productWithNegativePrice = makeResult(
            id = "1",
            title = "Produto com Preço Negativo",
            price = -10.0
        )
        val response = SearchResponse(results = listOf(productWithNegativePrice))
        coEvery { apiService.queryProducts("negativeprice") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("negativeprice")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals((-10.0).toBigDecimalMonetary(), list[0].price)
    }

    @Test
    fun `should handle products with very large price`() = runTest {
        val productWithLargePrice = makeResult(
            id = "1",
            title = "Produto Caro",
            price = 999999.99
        )
        val response = SearchResponse(results = listOf(productWithLargePrice))
        coEvery { apiService.queryProducts("largeprice") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("largeprice")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals((999999.99).toBigDecimalMonetary(), list[0].price)
    }

    @Test
    fun `should handle products with decimal price`() = runTest {
        val productWithDecimalPrice = makeResult(
            id = "1",
            title = "Produto com Preço Decimal",
            price = 15.75
        )
        val response = SearchResponse(results = listOf(productWithDecimalPrice))
        coEvery { apiService.queryProducts("decimalprice") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("decimalprice")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals((15.75).toBigDecimalMonetary(), list[0].price)
    }

    @Test
    fun `should handle products with very long title`() = runTest {
        val longTitle = "A".repeat(1000)
        val productWithLongTitle = makeResult(
            id = "1",
            title = longTitle,
            price = 45.0
        )
        val response = SearchResponse(results = listOf(productWithLongTitle))
        coEvery { apiService.queryProducts("longtitle") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("longtitle")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(longTitle, list[0].title)
    }

    @Test
    fun `should handle products with special characters in title`() = runTest {
        val specialTitle = "Produto com Caracteres Especiais: áéíóú çãõ ñ"
        val productWithSpecialTitle = makeResult(
            id = "1",
            title = specialTitle,
            price = 55.0
        )
        val response = SearchResponse(results = listOf(productWithSpecialTitle))
        coEvery { apiService.queryProducts("specialchars") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("specialchars")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(specialTitle, list[0].title)
    }

    @Test
    fun `should handle products with whitespace-only title`() = runTest {
        val productWithWhitespaceTitle = makeResult(
            id = "1",
            title = "   ",
            price = 65.0
        )
        val response = SearchResponse(results = listOf(productWithWhitespaceTitle))
        coEvery { apiService.queryProducts("whitespacetitle") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("whitespacetitle")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(0, list.size) // Should be filtered out
    }

    @Test
    fun `should handle query with only numbers`() = runTest {
        val response = SearchResponse(results = listOf(makeResult()))
        coEvery { apiService.queryProducts("12345") } returns NetworkResponse.Success(response)
        
        val result = repository.getSearchResults("12345")
        assert(result is NetworkResponse.Success)
    }

    @Test
    fun `should handle empty query string`() = runTest {
        val response = SearchResponse(results = listOf(makeResult()))
        coEvery { apiService.queryProducts("") } returns NetworkResponse.Success(response)
        
        val result = repository.getSearchResults("")
        assert(result is NetworkResponse.Success)
    }

    @Test
    fun `should handle query with leading and trailing spaces`() = runTest {
        val response = SearchResponse(results = listOf(makeResult()))
        coEvery { apiService.queryProducts("notebook") } returns NetworkResponse.Success(response)
        
        val result = repository.getSearchResults("  notebook  ")
        assert(result is NetworkResponse.Success)
    }

    @Test
    fun `should handle products with all optional fields populated`() = runTest {
        val shipping = Shipping(freeShipping = true)
        val attributes = listOf(
            AttributesItem(id = "BRAND", name = "Marca", valueName = "Nike"),
            AttributesItem(id = "SIZE", name = "Tamanho", valueName = "42")
        )
        val completeProduct = makeResult(
            id = "MLB123456789",
            title = "Tênis Nike Air Max Completo",
            price = 299.99,
            thumbnail = "http://example.com/tenis.jpg",
            availableQuantity = 10,
            originalPrice = 399.99,
            currencyId = "BRL",
            shipping = shipping,
            attributes = attributes
        )
        val response = SearchResponse(results = listOf(completeProduct))
        coEvery { apiService.queryProducts("completeproduct") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("completeproduct")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        
        val product = list[0]
        assertEquals("MLB123456789", product.id)
        assertEquals("Tênis Nike Air Max Completo", product.title)
        assertEquals((299.99).toBigDecimalMonetary(), product.price)
        assertEquals("https://example.com/tenis.jpg", product.productImage)
        assertEquals((399.99).toBigDecimalMonetary(), product.originalPrice)
        assertEquals("BRL", product.currencyId)
        assertTrue(product.isFreeShipping)
        assertEquals(2, product.attributes.size)
        assertEquals("BRAND", product.attributes[0].attributeId)
        assertEquals("Marca", product.attributes[0].attributeName)
        assertEquals("Nike", product.attributes[0].valueName)
    }

    @Test
    fun `should handle products with null shipping`() = runTest {
        val productWithNullShipping = makeResult(
            id = "1",
            title = "Produto sem Shipping",
            price = 75.0,
            shipping = null
        )
        val response = SearchResponse(results = listOf(productWithNullShipping))
        coEvery { apiService.queryProducts("nullshipping") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("nullshipping")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertFalse(list[0].isFreeShipping)
    }

    @Test
    fun `should handle products with shipping but freeShipping is null`() = runTest {
        val shipping = Shipping(freeShipping = null)
        val productWithNullFreeShipping = makeResult(
            id = "1",
            title = "Produto com Shipping Null",
            price = 85.0,
            shipping = shipping
        )
        val response = SearchResponse(results = listOf(productWithNullFreeShipping))
        coEvery { apiService.queryProducts("nullfreeshipping") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("nullfreeshipping")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertFalse(list[0].isFreeShipping)
    }

    @Test
    fun `should handle products with shipping but freeShipping is false`() = runTest {
        val shipping = Shipping(freeShipping = false)
        val productWithFalseFreeShipping = makeResult(
            id = "1",
            title = "Produto sem Frete Grátis",
            price = 95.0,
            shipping = shipping
        )
        val response = SearchResponse(results = listOf(productWithFalseFreeShipping))
        coEvery { apiService.queryProducts("falsefreeshipping") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("falsefreeshipping")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertFalse(list[0].isFreeShipping)
    }
}
