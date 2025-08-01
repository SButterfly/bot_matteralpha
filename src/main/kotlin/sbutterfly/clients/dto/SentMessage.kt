package sbutterfly.clients.dto

data class SentMessage(
    val messageId: Long,
    val chat: Chat,
    val text: String? = null
)