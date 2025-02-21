package org.test.sioinc_ai_test.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.test.sioinc_ai_test.entity.User

interface UserRepository: JpaRepository<User, Long> {

    fun findUserByEmail(email: String): User?

}