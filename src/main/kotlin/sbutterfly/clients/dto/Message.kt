package sbutterfly.clients.dto

data class Message(
    val messageId: Long,
    val chat: Chat,
    val text: String? = null
)