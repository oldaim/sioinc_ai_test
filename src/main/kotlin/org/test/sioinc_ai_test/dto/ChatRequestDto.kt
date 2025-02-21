package org.test.sioinc_ai_test.dto

import org.test.sioinc_ai_test.constant.SortByCreated
import org.test.sioinc_ai_test.entity.Chat
import java.util.UUID

data class ChatRequestDto(
    val model: String,
    val question: String ,
)

data class ChatListRequestDto(
    val page: Int,
    val size: Int,
    val sort: SortByCreated
)

data class ChatResponseDto(
    val answer: String
)

data class ChatListResponseDto(
    val groupedChatList: Map<UUID?, List<Chat>>
)
