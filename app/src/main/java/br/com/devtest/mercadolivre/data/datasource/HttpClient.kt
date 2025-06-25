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
                // Simula delay de rede
                delay(NETWORK_DELAY_MS)
                val searchParam = request.url.parameters["q"]
                val idsParam = request.url.parameters["ids"]
                val isDescription = request.url.encodedPath.endsWith("description")
                val fileName = when {
                    !idsParam.isNullOrBlank() -> {
                        val id = idsParam
                        if (isDescription) {
                            "item-${id}-description.json"
                        } else {
                            "item-${id}.json"
                        }
                    }
                    !searchParam.isNullOrBlank() -> {
                        "${searchParam}-query-list.json"
                    }
                    else -> {
                        "mock_default.json"
                    }
                }
                val (json, status) = try {
                    val content = context.assets.open(fileName).bufferedReader().use { it.readText() }
                    content to HttpStatusCode.OK
                } catch (_: Exception) {
                    if (isDescription) {
                        "" to HttpStatusCode.NotFound
                    } else {
                        "{}" to HttpStatusCode.OK
                    }
                }

                respond(
                    content = json,
                    status = status,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
    }
}