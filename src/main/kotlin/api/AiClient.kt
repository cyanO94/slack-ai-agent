package api

interface AiClient {
    suspend fun chatCompletion(prompt: String): String
}