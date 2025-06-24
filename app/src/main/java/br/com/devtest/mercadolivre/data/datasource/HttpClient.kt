package br.com.devtest.mercadolivre.data.datasource

import android.content.Context
import br.com.devtest.mercadolivre.data.datasource.service.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import kotlinx.coroutines.delay

private const val NETWORK_DELAY_MS = 1000L

fun createMockKtorClient(context: Context): HttpClient {
    return HttpClient(MockEngine) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = NetworkConstants.BASE_URL
            }
        }
        engine {
            addHandler { request ->
                // Simulate a delay to mimic network latency
                delay(NETWORK_DELAY_MS)
                // Extracts the search parameter from the request URL
                val searchParam = request.url.parameters["q"]
                // Defines the file name based on the search parameter
                val fileName = if (!searchParam.isNullOrBlank()) {
                    "${searchParam}-query-list.json"
                } else {
                    "mock_default.json"
                }
                // Read the JSON file from the assets folder
                val json = try {
                    context.assets.open(fileName).bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    // If the file is not found, return an empty JSON object
                    "{}"
                }
                respond(
                    content = json,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
    }
}