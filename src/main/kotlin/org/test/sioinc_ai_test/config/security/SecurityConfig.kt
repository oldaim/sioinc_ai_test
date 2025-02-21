package org.test.sioinc_ai_test.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.test.sioinc_ai_test.service.UserService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val successHandler: UserAuthenticationSuccessHandler
) {


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .authorizeHttpRequests {
                it.requestMatchers( "/api/auth/**").permitAll()
                .anyRequest().authenticated()
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .csrf { it.disable() }
            .cors{ it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(getJwtAuthenticationFilter(), BasicAuthenticationFilter::class.java)
            .addFilterBefore(getCustomUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }

    fun getCustomUsernamePasswordAuthenticationFilter(): CustomUserAuthenticationFilter{
        val customUserAuthenticationFilter = CustomUserAuthenticationFilter()
        customUserAuthenticationFilter.setAuthenticationManager(getAuthenticationManager())
        customUserAuthenticationFilter.setFilterProcessesUrl("/api/auth/loginUser")
        customUserAuthenticationFilter.setAuthenticationSuccessHandler(successHandler)
        return customUserAuthenticationFilter
    }

    fun getJwtAuthenticationFilter(): JwtAuthenticationFilter{
        return JwtAuthenticationFilter(jwtTokenProvider, userService)
    }

    private fun getAuthenticationManager(): AuthenticationManager {

        val passwordEncoder = passwordEncoder()

        val authenticationProvider = DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder)
            setUserDetailsService(userService)
        }

        return ProviderManager(listOf(authenticationProvider))

    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()


}