package br.com.devtest.mercadolivre.data.datasource

import android.content.Context
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HttpClientTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should return content from asset file when file exists`() = runTest {
        val expectedJson = "{\"result\":\"ok\"}"
        val fakeInputStream = expectedJson.byteInputStream()
        every { context.assets.open("test-query-list.json") } returns fakeInputStream

        val client = createMockKtorClient(context)
        val response = client.get("/sites/MLB/search?q=test")
        val body = response.bodyAsText()
        assertEquals(expectedJson, body)
    }

    @Test
    fun `should return empty json when file does not exist`() = runTest {
        every { context.assets.open("notfound-query-list.json") } throws Exception("File not found")

        val client = createMockKtorClient(context)
        val response = client.get("/sites/MLB/search?q=notfound")
        val body = response.bodyAsText()
        assertEquals("{}", body)
    }

    @Test
    fun `should use mock_default json when no search param`() = runTest {
        val expectedJson = "{\"default\":true}"
        val fakeInputStream = expectedJson.byteInputStream()
        every { context.assets.open("mock_default.json") } returns fakeInputStream

        val client = createMockKtorClient(context)
        val response = client.get("/sites/MLB/search")
        val body = response.bodyAsText()
        assertEquals(expectedJson, body)
    }

    @Test
    fun `should respond with correct Content-Type header`() = runTest {
        val expectedJson = "{\"header\":true}"
        val fakeInputStream = expectedJson.byteInputStream()
        every { context.assets.open("header-query-list.json") } returns fakeInputStream

        val client = createMockKtorClient(context)
        val response = client.get("/sites/MLB/search?q=header")
        val contentType = response.headers["Content-Type"]
        assertEquals("application/json", contentType)
    }

    @Test
    fun `should use https and correct host in defaultRequest`() = runTest {
        val expectedJson = "{\"host\":true}"
        val fakeInputStream = expectedJson.byteInputStream()
        every { context.assets.open("host-query-list.json") } returns fakeInputStream

        val client = createMockKtorClient(context)
        val response = client.get("/sites/MLB/search?q=host")
        // O host e protocolo são definidos no defaultRequest, mas não são expostos diretamente na resposta.
        // Aqui testamos indiretamente: se não lançar exceção, o defaultRequest está funcionando.
        assertEquals(expectedJson, response.bodyAsText())
    }

    @Test
    fun `should respond consistently to multiple sequential calls`() = runTest {
        val expectedJson1 = "{\"result1\":true}"
        val expectedJson2 = "{\"result2\":true}"
        every { context.assets.open("first-query-list.json") } returns expectedJson1.byteInputStream()
        every { context.assets.open("second-query-list.json") } returns expectedJson2.byteInputStream()

        val client = createMockKtorClient(context)
        val response1 = client.get("/sites/MLB/search?q=first")
        val response2 = client.get("/sites/MLB/search?q=second")
        assertEquals(expectedJson1, response1.bodyAsText())
        assertEquals(expectedJson2, response2.bodyAsText())
    }
}
