package org.test.sioinc_ai_test.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.test.sioinc_ai_test.constant.RoleType
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
@Table(name = "ai_user")
data class User(
    @Id val id: Long? = null,
    @Column val email: String? = null,
    @Column val name: String? = null,
    @Column val password: String? = null,
    @Column val timestamp: LocalDateTime? = null,
    @Column val role: RoleType? = null
){

}


