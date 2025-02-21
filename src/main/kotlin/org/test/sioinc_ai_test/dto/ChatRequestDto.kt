package org.test.sioinc_ai_test.dto

data class ChatRequestDto(
    val model: String,
    val question: String
)

data class ChatResponseDto(
    val answer: String
)
