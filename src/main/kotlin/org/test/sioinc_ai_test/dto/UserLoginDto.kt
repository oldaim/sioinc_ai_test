package org.test.sioinc_ai_test.dto

import org.test.sioinc_ai_test.constant.RoleType

data class UserLoginDto(
    val email: String,
    val name: String,
    val password: String,
    val role: String
)
