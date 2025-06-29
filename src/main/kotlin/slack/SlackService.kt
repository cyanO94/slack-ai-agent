package slack

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.ktor.respond
import com.slack.api.bolt.ktor.toBoltRequest
import com.slack.api.bolt.util.SlackRequestParser
import io.ktor.server.application.call
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class SlackService(
    signingSecret: String,
    botToken: String,
    port: Int = 8080,
    private val useCaseHandler: SlackCommandHandler
) {
    private val config = AppConfig().apply {
        this.signingSecret = signingSecret
        this.singleTeamBotToken = botToken
    }

    private val app = App(config).apply {
        useCaseHandler.register(this)
    }
    val parser = SlackRequestParser(app.config())
    val engine: ApplicationEngine = embeddedServer(Netty, port) {
        routing {
            post("/slack/events") {
                val boltReq = toBoltRequest(call, parser)
                val boltRes = app.run(boltReq)
                respond(call, boltRes)
            }
        }
    }

    fun start() = engine.start(wait = true)
    fun stop() = engine.stop(1_000, 5_000)
}