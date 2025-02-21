package org.test.sioinc_ai_test.service

import org.test.sioinc_ai_test.dto.ChatRequestDto
import org.test.sioinc_ai_test.dto.ChatResponseDto

interface ChatService {

    fun createChat(dto: ChatRequestDto): ChatResponseDto

    fun createStreamingChat()

    fun getAllThread()

    fun deleteThread()
}