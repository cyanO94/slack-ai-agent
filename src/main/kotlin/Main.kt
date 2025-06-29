import api.openai.OpenAiClientImpl
import domain.SummarizeThreadUseCase
import io.github.cdimascio.dotenv.dotenv
import slack.SlackCommandHandler
import slack.SlackService

fun main() {
    val env = dotenv()

    val aiClient = OpenAiClientImpl(env["OPEN_API_KEY"])
    val summarizeUseCase = SummarizeThreadUseCase(aiClient)
    val slackHandler = SlackCommandHandler(summarizeUseCase)
    SlackService(
        signingSecret = env["SLACK_SIGNING_SECRET"],
        botToken = env["SLACK_BOT_TOKEN"],
        useCaseHandler = slackHandler
    ).start()
}