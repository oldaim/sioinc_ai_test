package org.test.sioinc_ai_test.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.test.sioinc_ai_test.constant.RoleType
import java.time.LocalDateTime

data class UserWrapper(
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null,
    val passwordEncoded: String? = null,
    val timestamp: LocalDateTime? = null,
    val role: RoleType? = null
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role?.name))
    }

    override fun getPassword(): String {
        return this.passwordEncoded?:""
    }

    override fun getUsername(): String {
        return this.email?:""
    }
}
