package org.test.sioinc_ai_test.model

data class ModelRequest(
    val model: String,
    val store: Boolean = true,
    val message: List<ChatMessage>
)

data class ChatMessage(
    val role: String = "user",
    val content: String,
    val refusal: String? = null
)


data class ModelResponse(
    val id: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val message: ChatMessage,
    val finish_reason: String
)
