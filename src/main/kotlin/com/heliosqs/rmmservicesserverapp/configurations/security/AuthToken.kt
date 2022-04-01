package com.heliosqs.rmmservicesserverapp.configurations.security

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthToken(private val customer: Customer, private val token: String, authorities: MutableCollection<out GrantedAuthority>?) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return customer
    }
}
