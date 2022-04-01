package com.heliosqs.rmmservicesserverapp.gateway

import com.heliosqs.rmmservicesserverapp.configurations.security.AuthToken
import com.heliosqs.rmmservicesserverapp.configurations.security.JwtUtils
import com.heliosqs.rmmservicesserverapp.constants.ErrorConstants
import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.exception.UnauthorizedException
import com.heliosqs.rmmservicesserverapp.model.request.CustomerServiceRequest
import com.heliosqs.rmmservicesserverapp.model.response.common.AppResponse
import com.heliosqs.rmmservicesserverapp.services.Interfaces.CustomerServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Pattern

@RestController
@RequestMapping("/api/customer")
class CustomerGateway {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var customerService: CustomerServiceI

    @PostMapping("/auth/{id}")
    fun getToken(
        @PathVariable("id")
        @Pattern(regexp = "[0-9]+", message = ErrorConstants.NOT_NUMERIC)
        id: String
    ): String? {
        val customer = customerService.findById(id.toLong())
        if (customer.isEmpty) {
            throw UnauthorizedException(message = "Customer does not exist or was deleted", details = null)
        }
        return jwtUtils.generateJwtToken(customer.get())
    }

    @GetMapping("/services")
    fun getToken(): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        return ResponseEntity.ok(AppResponse(customer.services, null))
    }

    @PostMapping("/services")
    fun addService(@RequestBody @Valid customerServiceRequest: CustomerServiceRequest): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        val services = customerServiceRequest.serviceIds?.let { customerService.addServices(it.toSet(), customer) }
        return ResponseEntity.ok(AppResponse(services, null))
    }

    @DeleteMapping("/services")
    fun deleteService(@RequestBody @Valid customerServiceRequest: CustomerServiceRequest): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        val services = customerServiceRequest.serviceIds?.let { customerService.deleteServices(it.toSet(), customer) }
        return ResponseEntity.ok(AppResponse(services, null))
    }
}
