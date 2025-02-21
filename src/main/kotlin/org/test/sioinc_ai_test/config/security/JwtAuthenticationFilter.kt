package org.test.sioinc_ai_test.config.security


import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.test.sioinc_ai_test.service.UserService


class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService,
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val log = LoggerFactory.getLogger(this::class.java)

        val accessToken = runCatching { request.getHeader("Authorization") }.getOrNull()?.substringAfter(" ")

        if (accessToken.isNullOrBlank()) {
            log.error("Access Token is missing!")
            filterChain.doFilter(request, response)
        }

        else {

            if (jwtTokenProvider.validateAccessToken(accessToken) ){

                val userId = jwtTokenProvider.getUserIdFromToken(accessToken)

                val user = userService.loadUserByUsername(userId)

                val authentication =
                    UsernamePasswordAuthenticationToken.authenticated(user.username, "", user.authorities)

                SecurityContextHolder.getContext().authentication = authentication

            }

            filterChain.doFilter(request, response)
        }

    }

}