import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.util.SlackRequestParser
import com.slack.api.bolt.ktor.toBoltRequest
import com.slack.api.bolt.ktor.respond
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.call

fun main() {
    val env = dotenv()
    val config = AppConfig().apply {
        signingSecret = env["SLACK_SIGNING_SECRET"]
        singleTeamBotToken = env["SLACK_BOT_TOKEN"]
    }
    val app = App(config).apply {
        command("/hello") { _, ctx -> ctx.ack("안녕하세요! Bolt+Ktor 입니다.") }
    }
    val parser = SlackRequestParser(app.config())

    embeddedServer(Netty, port = 8080) {
        routing {
            post("/slack/events") {
                val boltReq = toBoltRequest(call, parser)
                val boltRes = app.run(boltReq)
                respond(call, boltRes)
            }
        }
    }.start(wait = true)
}