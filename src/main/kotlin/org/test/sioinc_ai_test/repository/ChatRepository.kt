package org.test.sioinc_ai_test.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.test.sioinc_ai_test.entity.Chat
import java.util.UUID

interface ChatRepository: JpaRepository<Chat, Long> {

    fun findAllByThreadId(threadId: UUID): List<Chat>

}