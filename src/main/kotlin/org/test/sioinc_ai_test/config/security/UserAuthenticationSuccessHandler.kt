package org.test.sioinc_ai_test.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.http.MediaType
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.test.sioinc_ai_test.dto.ApiResponse
import org.test.sioinc_ai_test.entity.User
import java.nio.charset.StandardCharsets

@Component
class UserAuthenticationSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
): AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: org.springframework.security.core.Authentication?
    ) {
        val code = 200
        val message = "success Authentication"

        val objectMapper = ObjectMapper()
        val userWrapper = authentication?.principal as User

        val email = userWrapper.email?:""

        val accessToken = jwtTokenProvider.generateAccessToken(email)

        val apiResponse = ApiResponse(
            code = code,
            message = message,
            data = accessToken
        )

        val responseBody = objectMapper.writeValueAsString(apiResponse)

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = StandardCharsets.UTF_8.name()
        response?.writer?.use { it.write(responseBody)}
    }
}

