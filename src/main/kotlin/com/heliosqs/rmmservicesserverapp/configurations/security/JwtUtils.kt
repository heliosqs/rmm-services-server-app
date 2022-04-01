package com.heliosqs.rmmservicesserverapp.configurations.security

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest

@Component
class JwtUtils {
    @Value("\${rmm.app.jwt.token.secretKey}")
    private lateinit var jwtSecret: String

    @Value("\${rmm.app.jwt.token.expiration}")
    private var jwtExpirationMs = 0

    val logger: Logger = LoggerFactory.getLogger(AuthFilter::class.toString())

    /**
     * Creates a jwt token for a customer using its id as an element for the claim
     * @param customer [Customer] Customer that will receive the token
     * @return jwt token [String]
     */
    fun generateJwtToken(customer: Customer): String? {
        val claims: Claims = Jwts.claims()
        claims["customerId"] = customer.id
        claims.subject = customer.name
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS512
        val key: Key = SecretKeySpec(
            Base64.getDecoder().decode(getSecretKey(jwtSecret)),
            signatureAlgorithm.jcaName
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(
                Date(
                    Date().time + jwtExpirationMs
                )
            )
            .signWith(key, signatureAlgorithm)
            .compact()
    }

    /**
     * Creates the encoded secret key of the jwt
     * @return secret key
     */
    fun getSecretKey(secret: String): String? {
        var encodedSecret: String? = null
        try {
            val md = MessageDigest.getInstance(SecurityConstants.ENCODING_ALGORITHM)
            md.update(secret.toByteArray())
            encodedSecret = Base64.getEncoder().encodeToString(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            logger.error("Error", e.message, e)
        }
        return encodedSecret
    }

    /**
     * Gets the company id from the token claims
     * @param token [String]
     * @return username [String]
     * @throws ExpiredJwtException Validates if the token has expired
     */
    fun getCustomerFromJwtToken(token: String?, request: HttpServletRequest): Long {
        try {
            val customerIdStr = Jwts.parserBuilder().setSigningKey(getSecretKey(jwtSecret)).build()
                .parseClaimsJws(token).body["customerId"].toString()
            return customerIdStr.toLong()
        } catch (e: ExpiredJwtException) {
            request.setAttribute("expired", java.lang.Boolean.TRUE)
            throw e
        }
    }

    /**
     * Validates the token information
     * @param token [String]
     * @param request   [HttpServletRequest]
     * @return [Boolean]
     */
    fun validateJwtToken(token: String?, request: HttpServletRequest): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey(jwtSecret)).build()
                .parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            request.setAttribute("invalid", java.lang.Boolean.TRUE)
        } catch (e: MalformedJwtException) {
            request.setAttribute("invalid", java.lang.Boolean.TRUE)
        } catch (e: ExpiredJwtException) {
            request.setAttribute("expired", java.lang.Boolean.TRUE)
        }
        return false
    }
}
