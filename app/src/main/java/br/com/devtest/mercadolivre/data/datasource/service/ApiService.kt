package br.com.devtest.mercadolivre.data.datasource.service

import br.com.devtest.mercadolivre.data.models.SearchResponse
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.utils.AppLog
import br.com.devtest.mercadolivre.utils.LogTags
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class ApiService(private val client: HttpClient) {
    val jsonHandler = Json {
        ignoreUnknownKeys = true // Ignore unknown keys in the JSON response
        isLenient = true // Allow lenient parsing
        prettyPrint = true // Format the JSON output for readability
    }

    suspend fun queryProducts(query: String): NetworkResponse<SearchResponse> {
        return try {
            val response = client.get("/sites/MLA/search") {
                parameter("q", query)
            }

            if (response.status.isSuccess()) {
                NetworkResponse.Success(jsonHandler.decodeFromString<SearchResponse>(response.bodyAsText()))
            } else {
                NetworkResponse.NetworkError(
                    message = response.status.description,
                    code = response.status.value
                )
            }
        } catch (e: Exception) {
            AppLog.e(LogTags.NETWORK, "Error on query products: ${e.message}")
            NetworkResponse.GenericError(message = e.message ?: "Unknown error")
        }
    }
}