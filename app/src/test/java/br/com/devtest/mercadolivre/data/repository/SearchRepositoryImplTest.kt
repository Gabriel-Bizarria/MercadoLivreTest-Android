package br.com.devtest.mercadolivre.data.repository

import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.models.ResultsItem
import br.com.devtest.mercadolivre.data.models.SearchResponse
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.ui.models.ProductUiModel
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {
    private lateinit var apiService: ApiService
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = SearchRepositoryImpl(apiService)
    }

    @Test
    fun `should return mapped ProductUiModel list on success`() = runTest {
        val searchResult = ResultsItem(
            id = "1",
            title = "Produto 1",
            price = 10.0,
            thumbnail = "img.png",
            availableQuantity = 2
        )
        val response = SearchResponse(results = listOf(searchResult))
        coEvery { apiService.queryProducts("notebook") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("notebook")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(
            ProductUiModel(
                id = "1",
                title = "Produto 1",
                price = (10.0).toBigDecimalMonetary(),
                isFreeShipping = false,
                originalPrice = null,
                brand = null,
                currencyId = null,
                productImage = "img.png"
            ),
            list[0]
        )
    }

    @Test
    fun `should return empty list when results is null or empty`() = runTest {
        coEvery { apiService.queryProducts("empty") } returns NetworkResponse.Success(
            SearchResponse(
                results = null
            )
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
        val valid = ResultsItem(
            id = "1",
            title = "Produto 1",
            price = 10.0,
            thumbnail = "img.png",
            availableQuantity = 1
        )
        val noId = ResultsItem(
            id = null,
            title = "Produto",
            price = 10.0,
            thumbnail = "img.png",
            availableQuantity = 1
        )
        val noTitle = ResultsItem(
            id = "2",
            title = null,
            price = 10.0,
            thumbnail = "img.png",
            availableQuantity = 1
        )
        val blankTitle = ResultsItem(
            id = "3",
            title = "",
            price = 10.0,
            thumbnail = "img.png",
            availableQuantity = 1
        )
        val noPrice = ResultsItem(
            id = "4",
            title = "Produto",
            price = null,
            thumbnail = "img.png",
            availableQuantity = 1
        )
        val response = SearchResponse(results = listOf(valid, noId, noTitle, blankTitle, noPrice))
        coEvery { apiService.queryProducts("filter") } returns NetworkResponse.Success(response)

        val result = repository.getSearchResults("filter")
        assert(result is NetworkResponse.Success)
        val list = (result as NetworkResponse.Success).data
        assertEquals(1, list.size)
        assertEquals(
            ProductUiModel(
                id = "1",
                title = "Produto 1",
                price = (10.0).toBigDecimalMonetary(),
                isFreeShipping = false,
                originalPrice = null,
                brand = null,
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
}
