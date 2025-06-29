package domain

import api.AiClient

class SummarizeThreadUseCase(
    private val aiClient: AiClient
) {
    suspend operator fun invoke(threadText: String): String {
        val prompt = """
            아래 대화 스레드를 한국어로 간결하게 요약해줘:

            $threadText
        """.trimIndent()
        return aiClient.chatCompletion(prompt)
    }
}