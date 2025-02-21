package org.test.sioinc_ai_test.service

import org.test.sioinc_ai_test.dto.ChatListRequestDto
import org.test.sioinc_ai_test.dto.ChatListResponseDto
import org.test.sioinc_ai_test.dto.ChatRequestDto
import org.test.sioinc_ai_test.dto.ChatResponseDto
import org.test.sioinc_ai_test.entity.Chat
import java.util.*

interface ChatService {

    fun createChat(dto: ChatRequestDto): ChatResponseDto

    fun createStreamingChat()

    fun getAllThread(dto: ChatListRequestDto): ChatListResponseDto

    fun deleteThread()
}