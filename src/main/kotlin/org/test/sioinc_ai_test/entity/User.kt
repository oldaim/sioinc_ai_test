package org.test.sioinc_ai_test.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.test.sioinc_ai_test.constant.RoleType
import java.sql.Timestamp

@Entity
data class User(
    @Id val id: Long? = null,
    @Column val email: String? = null,
    @Column val name: String? = null,
    @CreatedDate @Column val timestamp: Timestamp? = null,
    @Column val role: RoleType? = null
)


