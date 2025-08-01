package sbutterfly.clients

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.requiredBody
import org.springframework.web.util.UriComponentsBuilder
import sbutterfly.clients.dto.Chat
import sbutterfly.clients.dto.Message
import sbutterfly.clients.dto.SentMessage
import sbutterfly.clients.dto.TelegramResponse
import sbutterfly.clients.dto.Update
import sbutterfly.clients.dto.UpdatesResponse

/**
 * Telegram Bot API client.
 */
@Service
@RegisterReflectionForBinding(
    Chat::class,
    Message::class,
    SentMessage::class,
    TelegramResponse::class,
    Update::class,
    UpdatesResponse::class,
)
class TelegramClient(
    @Value("\${telegram.bot.token}") private val token: String,
): AutoCloseable {
    private val apiUrl: String = "https://api.telegram.org/bot$token"
    private val client: RestClient = RestClient.builder()
        .build()

    /**
     * Sends a message to the specified chat.
     *
     * @param chatId The ID of the chat to send the message to.
     * @param text The text of the message to send.
     * @return True if the message was sent successfully, false otherwise.
     */
    fun sendMessage(chatId: Long, text: String): SentMessage {
        val responseSpec = client.post()
            .uri("$apiUrl/sendMessage")
            .contentType(APPLICATION_JSON)
            .body(mapOf("chat_id" to chatId, "text" to text))
            .retrieve()

        val body = responseSpec.requiredBody<TelegramResponse<SentMessage>>()
        if (!body.ok) {
            throw RuntimeException("Failed to send message: ${body.description}")
        }

        return body.result ?: throw RuntimeException("No result returned from Telegram API.")
    }

    fun getUpdates(offset: Long? = null): List<Update> {
        val uri = UriComponentsBuilder
            .fromUriString("$apiUrl/getUpdates")
            .queryParam("timeout", 30)
            .queryParam("offset", offset)
            .build()
            .toUri()

        val responseSpec = client.get()
            .uri(uri)
            .accept(APPLICATION_JSON)
            .retrieve()

        val body: TelegramResponse<List<Update>> = responseSpec.requiredBody<TelegramResponse<List<Update>>>()
        if (!body.ok) {
            throw RuntimeException("Failed to send message: ${body.description}")
        }
        return body.result ?: emptyList()
    }

    override fun close() {
    }
}
