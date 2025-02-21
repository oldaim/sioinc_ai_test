package org.test.sioinc_ai_test.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chat")
data class Chat(
    @Id val id: Long? = null,
    @Column(unique = true) val threadId: UUID? = null,
    @Column(unique = true) val userId: Long ? = null,
    @Column val question: String? = null,
    @Column val answer: String? = null,
    @Column val timestamp: LocalDateTime? = null
)
