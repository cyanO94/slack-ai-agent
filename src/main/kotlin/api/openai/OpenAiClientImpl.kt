package api.openai

import api.AiClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

class OpenAiClientImpl(
    openAiKey: String,
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
    }
): AiClient {
    override suspend fun chatCompletion(prompt: String): String {
        TODO("Not yet implemented")
    }
}
