package com.heliosqs.rmmservicesserverapp.gateway

import com.heliosqs.rmmservicesserverapp.configurations.security.AuthToken
import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.response.common.AppResponse
import com.heliosqs.rmmservicesserverapp.services.Interfaces.InvoiceServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/invoices")
class InvoiceGateway {
    @Autowired
    private lateinit var invoiceService: InvoiceServiceI

    @GetMapping
    fun get(): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        return ResponseEntity.ok(AppResponse(invoiceService.calculateMonthlyPrice(customer), null))
    }
}
