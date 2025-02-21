package org.test.sioinc_ai_test.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.test.sioinc_ai_test.dto.UserWrapper
import org.test.sioinc_ai_test.entity.User
import org.test.sioinc_ai_test.repository.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun loadUserByUsername(username: String?): UserDetails {

        val user: User? = userRepository.findUserByEmail(username?:"")

        val userWrapper = UserWrapper(
            email = user?.email,
            name = user?.name,
            passwordEncoded = user?.password,
            timestamp = user?.timestamp,
            role = user?.role
        )

        return userWrapper
    }
}