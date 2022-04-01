package com.heliosqs.rmmservicesserverapp.configurations.security

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.services.Interfaces.CustomerServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Optional
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var customerService: CustomerServiceI

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = parseJwt(request)
            if (jwtUtils.validateJwtToken(jwt, request)) {
                val customerId: Long = jwtUtils.getCustomerFromJwtToken(jwt, request)
                val userDetails: Optional<Customer> = customerService.findById(customerId)
                if (userDetails.isPresent && jwt != null) {
                    val auth = AuthToken(userDetails.get(), jwt, null)
                    auth.isAuthenticated = true
                    SecurityContextHolder.getContext().authentication = auth
                } else {
                    logger.error("Token is not valid")
                    return
                }
            }
        } catch (e: RuntimeException) {
            logger.error("Cannot set user authentication: $e")
        }
        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else null
    }
}
