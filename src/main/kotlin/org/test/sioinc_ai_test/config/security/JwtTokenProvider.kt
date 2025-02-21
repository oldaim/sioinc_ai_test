package org.test.sioinc_ai_test.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.test.sioinc_ai_test.repository.UserRepository
import java.util.*
import javax.crypto.SecretKey
import javax.security.sasl.AuthenticationException

@Component
class JwtTokenProvider(
    private val userRepository: UserRepository,
    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${jwt.expiration}")
    private val accessTokenExpirationTime: Long
) {

    companion object{

        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun generateAccessToken(email: String): String =
        Jwts.builder()
        .setSubject(email)
        .setIssuedAt(Date())
        .setExpiration(getExpirationTime(accessTokenExpirationTime))
        .signWith(getSecretKey())
        .compact()


    fun validateAccessToken(accessToken: String): Boolean {
        try {

            val parsedClaims = getParsedClaims(accessToken) ?: return false

            val email = parsedClaims.body.subject

            userRepository.findUserByEmail(email) ?: return false

            val expirationTime = parsedClaims.body.expiration

            return expirationTime.after(Date())

        } catch (e: Exception){
            log.error("Access Token is Invalid")
            return false
        }
    }



    fun getParsedClaims(jwtToken: String): Jws<Claims>? {

        return  Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(jwtToken)

    }

    fun getUserIdFromToken(accessToken: String): String? {
        val parsedClaims = getParsedClaims(accessToken)
        return parsedClaims?.body?.subject
    }


    private fun getExpirationTime(expirationTime: Long): Date {
        val now = Date().time
        val expirationDate = now + expirationTime
        return Date(expirationDate)
    }

    private fun getSecretKey(): SecretKey {
        val keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.toByteArray())

        return Keys.hmacShaKeyFor(keyBase64Encoded.toByteArray())

    }

}