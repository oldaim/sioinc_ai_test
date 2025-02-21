package org.test.sioinc_ai_test.service

import org.springframework.security.core.userdetails.UserDetails
import org.test.sioinc_ai_test.entity.User
import java.time.LocalDateTime
import java.util.*

interface CacheService {

    // 유저 이메일 + UUID <- currentThreadCache
    // UUID + time <= uuidTimeCache

    fun getOrPutThreadUUID(user: UserDetails): UUID

    fun getUuidTime(currentThreadId: UUID): LocalDateTime
}