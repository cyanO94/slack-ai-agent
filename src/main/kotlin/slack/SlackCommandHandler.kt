package slack

import com.slack.api.bolt.App
import com.slack.api.bolt.context.builtin.MessageShortcutContext
import com.slack.api.bolt.request.builtin.MessageShortcutRequest
import domain.SummarizeThreadUseCase
import kotlinx.coroutines.runBlocking

class SlackCommandHandler(
    private val summarizeThreadUseCase: SummarizeThreadUseCase
) {
    fun register(app: App) {
        app.messageShortcut("summarize_thread") {
            req: MessageShortcutRequest, ctx: MessageShortcutContext ->
            ctx.ack()

            val channelId = req.payload.channel?.id ?: return@messageShortcut ctx.ack()
            val messageTs = req.payload.message?.ts ?: return@messageShortcut ctx.ack()
            // 스레드 전체 메시지 조회
            val repliesResp = ctx.client().conversationsReplies {
                it.channel(channelId).ts(messageTs)
            }
            if (!repliesResp.isOk) {
                ctx.client().chatPostEphemeral { b ->
                    b.channel(channelId)
                        .user(req.payload.user.id)
                        .text("⚠️ 스레드 조회 실패: ${repliesResp.error}")
                }
                return@messageShortcut ctx.ack()
            }

            // 요약
            val threadText = repliesResp.messages
                .joinToString("\n") { m -> "${m.user ?: "unknown"}: ${m.text}" }
            val summary = runBlocking {
                summarizeThreadUseCase(threadText)
            }

            // 요약 결과 답변
            ctx.client().chatPostMessage { b ->
                b.channel(channelId)
                    .threadTs(messageTs)
                    .text(summary)
            }

            ctx.ack()
        }
    }
}