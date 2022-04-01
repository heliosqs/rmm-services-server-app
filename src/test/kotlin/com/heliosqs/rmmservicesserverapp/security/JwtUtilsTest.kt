package com.heliosqs.rmmservicesserverapp.security

import com.heliosqs.rmmservicesserverapp.configurations.security.JwtUtils
import com.heliosqs.rmmservicesserverapp.model.core.Customer
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.mock.web.MockHttpServletRequest
import java.security.Key
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@SpringBootTest
class JwtUtilsTest {
    private lateinit var request: MockHttpServletRequest

    @Value("\${rmm.app.jwt.token.secretKey}")
    private lateinit var jwtSecret: String

    @SpyBean
    private lateinit var jwtUtils: JwtUtils

    @Test
    fun `Creates a valid token`() {
        request = MockHttpServletRequest()
        request.requestURI = "rmm/api"
        val customer = Customer(id = 1, name = "Tester")
        val token: String? = jwtUtils.generateJwtToken(customer)
        if (token != null) {
            Assertions.assertFalse(token.isEmpty())
        }
        Assertions.assertTrue(jwtUtils.validateJwtToken(token, request))
    }

    @Test
    fun `Validates that a token has expired`() {
        val customer = Customer(id = 1, name = "Tester")
        // Build a valid token that has already expired
        val claims: Claims = Jwts.claims()
        claims["customerId"] = customer.id
        claims.subject = customer.name
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS512
        val key: Key = SecretKeySpec(
            Base64.getDecoder().decode(jwtUtils.getSecretKey(jwtSecret)),
            signatureAlgorithm.jcaName
        )
        val token: String? = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(
                Date(Instant.now().atZone(UTC).minusDays(2).toEpochSecond())
            )
            .setExpiration(
                Date(Instant.now().atZone(UTC).minusDays(1).toEpochSecond())
            )
            .signWith(key, signatureAlgorithm)
            .compact()
        // Validate if the token expired
        request = MockHttpServletRequest()
        request.requestURI = "rmm/api"
        if (token != null) {
            Assertions.assertFalse(token.isEmpty())
        }
        Assertions.assertFalse(jwtUtils.validateJwtToken(token, request))
        Assertions.assertTrue(request.getAttribute("expired") as Boolean)
    }
}
