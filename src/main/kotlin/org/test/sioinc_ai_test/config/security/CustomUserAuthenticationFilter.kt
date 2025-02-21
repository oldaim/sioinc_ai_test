package org.test.sioinc_ai_test.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.test.sioinc_ai_test.dto.UserLoginDto

class CustomUserAuthenticationFilter(): UsernamePasswordAuthenticationFilter() {

    companion object{
        private val objectMapper: ObjectMapper = ObjectMapper()
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        val byteArray: ByteArray? = request?.inputStream?.use { it.readAllBytes() }

        val userLoginDto = objectMapper.readValue(byteArray, UserLoginDto::class.java)

        val email: String = userLoginDto.email
        val password: String = userLoginDto.password

        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)

        val authentication = authenticationManager.authenticate(authenticationToken)

        return if(authentication.isAuthenticated) authentication
        else throw RuntimeException("Authentication is Failed")

    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        super.successfulAuthentication(request, response, chain, authResult)
    }

}